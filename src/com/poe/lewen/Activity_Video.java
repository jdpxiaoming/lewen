package com.poe.lewen;

import java.util.List;
import com.mm.android.avnetsdk.AVNetSDK;
import com.mm.android.avnetsdk.param.AV_HANDLE;
import com.mm.android.avnetsdk.param.AV_IN_RealPlay;
import com.mm.android.avnetsdk.param.AV_MediaInfo;
import com.mm.android.avnetsdk.param.AV_OUT_RealPlay;
import com.mm.android.avnetsdk.param.AV_PlayPosInfo;
import com.mm.android.avnetsdk.param.AV_Time;
import com.mm.android.avnetsdk.param.IAV_DataListener;
import com.mm.android.avnetsdk.param.IAV_NetWorkListener;
import com.mm.android.avnetsdk.param.IAV_PlayerEventListener;
import com.mm.android.avnetsdk.param.RecordInfo;
import com.mm.android.avplaysdk.render.BasicGLSurfaceView;
import com.poe.lewen.MyApplication.loaded4login;
import com.poe.lewen.util.Tool;
import com.poe.lewen.vlc.Util;

import android.R.integer;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class Activity_Video extends BaseActivity {

//---------------------------通道一
	private AV_IN_RealPlay playINParam = null; // 实时监视输入参数
	private AV_OUT_RealPlay playOutParam = null; // 实时监视输出参数
	private BasicGLSurfaceView bsView = null; // 播放的视图
	private AV_HANDLE realPlay = null; // 实时监测句柄
	private LinearLayout loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_video);

	}

	@Override
	public void init() {
		loading	=	(LinearLayout) findViewById(R.id.loadingOfVideo);
		
		lin_video.setBackgroundResource(R.drawable.btn_bg_press	);
		image_video.setImageResource(R.drawable.icon_video_press);
		text_video.setTextColor(Color.WHITE);
		bsView = (BasicGLSurfaceView) findViewById(R.id.screenOfVideo);
		
		bsView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				float mTouchY=0;
//				 switch (event.getAction()) {
//
//			        case MotionEvent.ACTION_DOWN:
//			            mTouchY = event.getRawY();
//			            break;
//
//			        case MotionEvent.ACTION_MOVE:
//			            // No volume/brightness action if coef < 2
//			            if (coef > 2) {
//			                // Volume (Up or Down - Right side)
//			                if (!mEnableBrightnessGesture || mTouchX > (screen.widthPixels / 2)){
//			                    doVolumeTouch(y_changed);
//			                }
//			                // Extend the overlay for a little while, so that it doesn't
//			                // disappear on the user if more adjustment is needed. This
//			                // is because on devices with soft navigation (e.g. Galaxy
//			                // Nexus), gestures can't be made without activating the UI.
//			                if(Util.hasNavBar())
//			                    showOverlay();
//			            }
//			            // Seek (Right or Left move)
//			            doSeekTouch(coef, xgesturesize, false);
//			            break;
//
//			        case MotionEvent.ACTION_UP:
//			            // Audio or Brightness
//			            break;
//			        }
				float y1=(float) 0.0,y2=(float) 0.0;
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					y1 = event.getY();
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					y2 =event.getY();
					
					System.out.println(y2-y1);
					if(y2-y1>30.0){
						MyApplication.selectChannel=MyApplication.selectChannel+1;
					}
					
					if(y1-y2>30){
						MyApplication.selectChannel=MyApplication.selectChannel-1;
					}
					
					if(MyApplication.selectChannel<0){
						MyApplication.selectChannel = 0;
					}
					
					if(MyApplication.selectChannel>MyApplication.mChannelList.size()){
						MyApplication.selectChannel = MyApplication.selectChannel%(MyApplication.mChannelList.size()==0?1:MyApplication.mChannelList.size());
					}
					
					new playTask().execute();
					
				}
				return true;
			}
		});
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}
	
	@Override
		protected void onResume() {
		if(bsView.getRenderer()==null){
			bsView.init(Activity_Video.this);
		}
			//start video defalut channel is 0
			new playTask().execute();
			super.onResume();
		}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
//		bsView.uninit();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		if (realPlay != null) {
			AVNetSDK.AV_StopRealPlay(realPlay); // 停止实时监视
			realPlay = null;
		}
		if (MyApplication.log_handle != null) {
			AVNetSDK.AV_Logout(MyApplication.log_handle);
			MyApplication.log_handle = null;
		}
		AVNetSDK.AV_Cleanup(); // 清理网络SDK
		bsView.uninit();	//反初始化播放视图
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Tool.showMsg(mContext, "退出...");
			AVNetSDK.AV_StopRealPlay(realPlay); // 停止实时监视
			realPlay = null;
			AVNetSDK.AV_Logout(MyApplication.log_handle);
			MyApplication.log_handle = null;
			//AVNetSDK.AV_Cleanup(); // 清理网络SDK
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	class playTask extends AsyncTask<Void, integer, String>{
		
		@Override
		protected void onPreExecute() {
			loading.setVisibility(View.VISIBLE);
			
			playINParam = new AV_IN_RealPlay();
			playINParam.nChannelID =MyApplication.selectChannel; // 测试零号通道
			playINParam.nSubType = 1;
			playINParam.playView = bsView;
			playINParam.dataListener = new IAV_DataListener() {

				@Override
				public int onData(AV_HANDLE arg0, byte[] arg1, int arg2, int arg3,
						AV_MediaInfo arg4, Object arg5) {
					// TODO Auto-generated method stub
					return 0;
				}
			};
			
			playINParam.netWorkListener = new IAV_NetWorkListener() {

				@Override
				public int onConnectStatus(AV_HANDLE arg0, boolean arg1,
						AV_HANDLE arg2, Object arg3) {
					// TODO Auto-generated method stub
					return 0;
				}
			};
			playINParam.playerEventListener = new IAV_PlayerEventListener() {

				@Override
				public void onResolutionChange(AV_HANDLE arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onRecordInfo(Object arg0, AV_Time arg1, AV_Time arg2,
						List<RecordInfo> arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public int onPlayPos(AV_HANDLE arg0, AV_PlayPosInfo arg1,
						Object arg2) {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public void onNotSupportedEncode(AV_HANDLE arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFrameRateChange(AV_HANDLE arg0, int arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFrameLost(AV_HANDLE arg0) {
					// TODO Auto-generated method stub

				}
			};
			//构造实时监视输出参数
			playOutParam = new AV_OUT_RealPlay();
		}

		@Override
		protected String doInBackground(Void... params) {
			if (MyApplication.log_handle != null) // 登陆成功才能播放
				realPlay = AVNetSDK.AV_RealPlay(MyApplication.log_handle, playINParam,	playOutParam);
			else {
				return "请先登陆！";
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			loading.setVisibility(View.GONE);
			if(null!=result){
//				Tool.showMsg(MyApplication.getInstance().getApplicationContext(), result);
				MyApplication.getInstance().reLogin(new loaded4login() {
					
					@Override
					public void done() {
						// TODO Auto-generated method stub
						new playTask().execute();
					}
				});
			}
		}
	}
}
