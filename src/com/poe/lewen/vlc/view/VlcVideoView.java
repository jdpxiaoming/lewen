package com.poe.lewen.vlc.view;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.libvlc.Media;
import com.poe.lewen.vlc.Util;
import com.poe.lewen.vlc.WeakHandler;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;


@SuppressLint({ "ViewConstructor", "HandlerLeak" })
public class VlcVideoView extends SurfaceView implements IVideoPlayer, Callback {

	private static String TAG = "VLCVideoView";
	@SuppressWarnings("unused")
	private Context mContext;
	private Uri mUri;
	private int mLayoutWidth, mLayoutHeight;
	private int mSarNum, mSarDen;
	private int mVideoWidth, mVideoHeight;

	// 容器
	private SurfaceHolder mSurfaceHolder;
	private LibVLC mLibVLC;
	private VlcVideoView mView;
	//private long mLastTimeMillion = 0;
	//private Handler mCheckHandler;
	//private HandlerThread mCheckThread;
	@SuppressWarnings("unused")
	private FrameLayout mParentLayout = null;
	private ProgressBar mProgressBar = null;
	/**
	 * Handle libvlc asynchronous events
	 */
	private Handler mEventHandler;
	private Handler mHandler;
	
	/*
	private Runnable mCheckRunnable = new Runnable() {
		@Override
		public void run() {
			long deltaTime = System.currentTimeMillis() - mLastTimeMillion;
			if (deltaTime > 2 * 1000) {
				((Activity) mContext).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressBar();
					}
				});
			}
			if (deltaTime > 30 * 1000) {
				if (mLibVLC != null) {
					mLibVLC.stop();
					openVideo();
				}
			}
			mCheckHandler.postDelayed(mCheckRunnable, 2 * 1000);
		}
	};
	*/
	public VlcVideoView(Context context, long blockId, FrameLayout parentLayout, Uri uri, ProgressBar progressBar) {
		super(context);
		mContext = context;
		mProgressBar = progressBar;
		mParentLayout = parentLayout;
		mView = this;
		this.setTag("VideoRTSP" + blockId);
		this.mLayoutHeight = parentLayout.getHeight();
		this.mLayoutWidth = parentLayout.getWidth();
		this.mUri = uri;
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
		try {
			mLibVLC = Util.getLibVlcInstance();
		} catch (LibVlcException e) {
			return;
		}
		mEventHandler = new VideoPlayerEventHandler(this);
		mHandler = new VideoPlayerHandler(this);
		EventHandler eventHandler = EventHandler.getInstance();
		eventHandler.addHandler(mEventHandler);

		/*
		mCheckThread = new HandlerThread("CheckVLCPlaying");
		mCheckThread.start();
		mCheckHandler = new Handler(mCheckThread.getLooper());
		mCheckHandler.removeCallbacks(mCheckRunnable);
		mCheckHandler.postDelayed(mCheckRunnable, 1 * 60 * 1000);
		*/
	}

	@Override
	public void setSurfaceSize(int width, int height, int visible_width, int visible_height, int sar_num, int sar_den) {
		if (width * height == 0)
			return;
		// store video size
		mVideoWidth = width;
		mVideoHeight = height;
		mSarNum = sar_num;
		mSarDen = sar_den;
		Message msg = mHandler.obtainMessage(0);
		mHandler.sendMessage(msg);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mMeasureWidth = getDefaultSize(mLayoutWidth, widthMeasureSpec);
		int mMesasureHeight = getDefaultSize(mLayoutHeight, heightMeasureSpec);
		setMeasuredDimension(mMeasureWidth, mMesasureHeight);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		openVideo();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mLibVLC.attachSurface(holder.getSurface(), this.mView);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mLibVLC != null) {
			mLibVLC.stop();
		}
		removeProgerssCheckCallBack();
		mLibVLC.detachSurface();
	}

	private void openVideo() {
		if (mUri == null || mSurfaceHolder == null) {
			return;
		}
		try {
			setKeepScreenOn(true);
			mLibVLC.setMediaList();
			mLibVLC.getMediaList().clear();
			mLibVLC.getMediaList().add(new Media(mLibVLC, mUri.toString()));
			mLibVLC.playIndex(0);
		} catch (IllegalArgumentException ex) {
			return;
		}
	}

//	// 设置全屏显示
//	private void changeSurfaceSize() {
//		mSurfaceHolder.setFixedSize(mLayoutWidth, mLayoutHeight);
//		this.invalidate();
//	}

	// 设置原始比例
	private void changeSurfaceSize() {
		int surfaceWidth = mLayoutWidth;
		int surfaceHeight = mLayoutHeight;
		boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
		if (surfaceWidth > surfaceHeight && isPortrait || surfaceWidth < surfaceHeight && !isPortrait) {
			int d = surfaceWidth;
			surfaceWidth = surfaceHeight;
			surfaceHeight = d;
		}
		if (surfaceWidth * surfaceHeight == 0)
			return;
		// compute the aspect ratio
		@SuppressWarnings("unused")
		double videoAspectRatio, realWidth;
		double density = (double) mSarNum / (double) mSarDen;
		if (density == 1.0) {
			/* No indication about the density, assuming 1:1 */
			realWidth = mVideoWidth;
			videoAspectRatio = (double) mVideoWidth / (double) mVideoHeight;
		} else {
			/* Use the specified aspect ratio */
			realWidth = mVideoWidth * density;
			videoAspectRatio = realWidth / mVideoHeight;
		}
		// compute the display aspect ratio
		@SuppressWarnings("unused")
		double displayAspectRatior = (double) surfaceWidth / (double) surfaceHeight;

		getHolder().setFixedSize(mVideoWidth, mVideoHeight);

		LayoutParams videoLayoutParams = (LayoutParams) getLayoutParams();
		videoLayoutParams.width = mLayoutWidth;// (displayAspectRatior < videoAspectRatio) ? mLayoutWidth : (int) (videoAspectRatio * mLayoutHeight);
		videoLayoutParams.height = mLayoutHeight;// (displayAspectRatior > videoAspectRatio) ? mLayoutHeight : (int) (mLayoutWidth / videoAspectRatio);
		videoLayoutParams.gravity = Gravity.CENTER;
		setLayoutParams(videoLayoutParams);
		invalidate();
	}

	private class VideoPlayerHandler extends WeakHandler<VlcVideoView> {
		public VideoPlayerHandler(VlcVideoView owner) {
			super(owner);
		}

		@Override
		public void handleMessage(Message msg) {
			VlcVideoView vlcVideoView = getOwner();
			if (vlcVideoView == null) // WeakReference could be GC'ed early
				return;
			vlcVideoView.changeSurfaceSize();
		}
	};

	private class VideoPlayerEventHandler extends WeakHandler<VlcVideoView> {
		VlcVideoView mVlcVideoView;

		public VideoPlayerEventHandler(VlcVideoView vlcVideoView) {
			super(vlcVideoView);
			mVlcVideoView = vlcVideoView;
		}

		@Override
		public void handleMessage(Message msg) {
			VlcVideoView vlcVideoView = getOwner();
			if (vlcVideoView == null)
				return;
			//mVlcVideoView.mLastTimeMillion = System.currentTimeMillis();
//			Logger.d(TAG, String.format("Event not handled (0x%x)", msg.getData().getInt("event")));
			//mVlcVideoView.dismissProgressBar();
			switch (msg.getData().getInt("event")) {
			case EventHandler.MediaPlayerPlaying:
				mVlcVideoView.dismissProgressBar();
				// vlcVideoView.setESTracks();
				break;
			case EventHandler.MediaPlayerPaused:
				mVlcVideoView.showProgressBar();
				break;
			case EventHandler.MediaPlayerStopped:
				mVlcVideoView.showProgressBar();
				break;
			case EventHandler.MediaPlayerEndReached:
				// vlcVideoView.endReached();
				break;
			case EventHandler.MediaPlayerVout:
				// vlcVideoView.handleVout(msg);
				//if (deltaTime > 2 * 1000) {
				//mVlcVideoView.showProgressBar();
				//}
				break;
			case EventHandler.MediaPlayerPositionChanged:
				// don't spam the logs
				break;
			case EventHandler.MediaPlayerEncounteredError:
//				Logger.d(TAG,"VLC播放错误，停止加载！");
				mVlcVideoView.dealWithError();
				break;
			case EventHandler.MediaPlayerBuffering:
				mVlcVideoView.showProgressBar();
				break;
			}
		}
	};

	private void dealWithError(){
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.VISIBLE);
		}
		if (mLibVLC != null) {
			mLibVLC.stop();
		}
		mHandler.postDelayed(new Runnable() {		
			@Override
			public void run() {
				openVideo();
			}
		}, 15000);
	}
	
	private void dismissProgressBar() {
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.GONE);
		}
	}

	private void showProgressBar() {
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.VISIBLE);
		}
		
	}

	public void removeProgerssCheckCallBack() {
		/*
		if (mCheckHandler != null && mCheckRunnable != null) {
			mCheckHandler.removeCallbacks(mCheckRunnable);
		}
		*/
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.GONE);
		}
		mProgressBar = null;
	}
}
