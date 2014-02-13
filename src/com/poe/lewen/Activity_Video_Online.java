package com.poe.lewen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import com.mm.android.avnetsdk.AVNetSDK;
import com.mm.android.avnetsdk.param.AV_HANDLE;
import com.mm.android.avnetsdk.param.AV_IN_Capture;
import com.mm.android.avnetsdk.param.AV_IN_RealPlay;
import com.mm.android.avnetsdk.param.AV_MediaInfo;
import com.mm.android.avnetsdk.param.AV_OUT_Capture;
import com.mm.android.avnetsdk.param.AV_OUT_RealPlay;
import com.mm.android.avnetsdk.param.AV_PlayPosInfo;
import com.mm.android.avnetsdk.param.AV_Time;
import com.mm.android.avnetsdk.param.IAV_CaptureDataListener;
import com.mm.android.avnetsdk.param.IAV_DataListener;
import com.mm.android.avnetsdk.param.IAV_NetWorkListener;
import com.mm.android.avnetsdk.param.IAV_PlayerEventListener;
import com.mm.android.avnetsdk.param.RecordInfo;
import com.mm.android.avplaysdk.render.BasicGLSurfaceView;
import com.poe.lewen.MyApplication.loaded4login;
import com.poe.lewen.bean.channelOnLine;
import com.poe.lewen.bean.history_video;
import com.poe.lewen.bean.resutl_parise;
import com.poe.lewen.service.XmlToListService;
import com.poe.lewen.util.DateUtil;
import com.poe.lewen.util.Tool;
import com.poe.lewen.vlc.VideoPlayerActivity;
import android.R.integer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_Video_Online extends BaseActivity implements IAV_CaptureDataListener {
	// ---------------------------通道一
	private AV_IN_RealPlay playINParam = null; // 实时监视输入参数
	private AV_OUT_RealPlay playOutParam = null; // 实时监视输出参数
	private BasicGLSurfaceView bsView = null; // 播放的视图
	private AV_HANDLE realPlay = null; // 实时监测句柄
	private LinearLayout loading;
	// screen capture
	private AV_HANDLE av_capture_handler = null;// 开启截屏的句柄
	private ImageButton btn_capture, btn_add_save;

	private LinearLayout linear_volue_parent, linear_volume;
	private int login_failed = 0;// 加载失败次数

	// 开始、暂停
	private ImageButton btn_play, btn_stop, btn_play_history;

	PointF start = new PointF();
	PointF end = new PointF();
	
	//top bar praise
	private TextView text_all,text_now,text_praise;
	private Button btn_praise;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_video);
	}

	@Override
	public void init() {
		
		//top bar
		text_all		=	(TextView) findViewById(R.id.textCount1OfToperBarYuntai);
		text_now		=	(TextView) findViewById(R.id.textCount2OfToperBarYuntai);
		text_praise	=	(TextView) findViewById(R.id.textCount3OfToperBarYuntai);
		btn_praise	=	(Button) findViewById(R.id.rightButtonOfToperBarYuntai);
		btn_praise.setOnClickListener(this);
		
		linear_volue_parent = (LinearLayout) findViewById(R.id.linearVolume1);
		linear_volume = (LinearLayout) findViewById(R.id.lin_voice);

		loading = (LinearLayout) findViewById(R.id.loadingOfVideo);
		btn_capture = (ImageButton) findViewById(R.id.imgScreenCaptureOfVideo);
		btn_add_save = (ImageButton) findViewById(R.id.imgAddSaveOfVideo);
		btn_play = (ImageButton) findViewById(R.id.imgPlayOfVideo);
		btn_stop = (ImageButton) findViewById(R.id.imgStopOfVideo);
		btn_play_history = (ImageButton) findViewById(R.id.imgPlayHistoryOfVideo);

		btn_play_history.setOnClickListener(this);
		btn_play.setOnClickListener(this);
		btn_stop.setOnClickListener(this);

		btn_capture.setOnClickListener(this);
		btn_add_save.setOnClickListener(this);

		lin_video.setBackgroundResource(R.drawable.btn_bg_press);
		image_video.setImageResource(R.drawable.icon_video_press);
		text_video.setTextColor(Color.WHITE);
		bsView = (BasicGLSurfaceView) findViewById(R.id.screenOfVideo);

		// bsView.seton
		bsView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				System.out.println(event.getAction() + "x:" + event.getX());
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					System.out.println("ACTION_DOWN");
					start = new PointF(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_MOVE:
					System.out.println("ACTION_MOVE");
					break;
				case MotionEvent.ACTION_UP:
					System.out.println("ACTION_UP");
					end = new PointF(event.getX(), event.getY());
					float x = end.x - start.x;
					if (Math.abs(x) > 10) {
						if (x > 0) {
							System.out.println("+1：" + x);
							MyApplication.selectChannel = MyApplication.selectChannel + 1;
						} else {
							System.out.println("-1:" + x);
							MyApplication.selectChannel = MyApplication.selectChannel - 1;
						}

						// 数组边界验证
						if (MyApplication.selectChannel < 0) {
							MyApplication.selectChannel = 0;
						}
						if (MyApplication.selectChannel > MyApplication.mChannelList.size()) {
							MyApplication.selectChannel = MyApplication.selectChannel % (MyApplication.mChannelList.size() == 0 ? 1 : MyApplication.mChannelList.size());
						}

						new playTask().execute();
					}
					break;
				default:
					System.out.println(event.getAction() + "");
					break;
				}
				return true;
			}
		});
		// volume
		linear_volue_parent.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					float x = event.getX();
					linear_volume.setLayoutParams(new LinearLayout.LayoutParams((int) x, 5));
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
		
		if (bsView.getRenderer() == null) {
			bsView.init(Activity_Video_Online.this);
		}

		if(MyApplication.cOnline!=null){
			text_now.setText(MyApplication.cOnline.getWatch());
			
			text_praise.setText(MyApplication.cOnline.getWatch());
		}
		login_failed = 0;
		// start video defalut channel is 0
		new playTask().execute();
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		// bsView.uninit();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (realPlay != null) {
			AVNetSDK.AV_StopRealPlay(realPlay); // 停止实时监视
			realPlay = null;
		}
		bsView.uninit(); // 反初始化播放视图
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
			// AVNetSDK.AV_Cleanup(); // 清理网络SDK
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 接受登录返回数据
			loading.setVisibility(View.GONE);

			String result_login = (String) msg.obj;

			switch (msg.what) {
			case 1:
				try {
					history_video hv = XmlToListService.GetPlayVideoHistory(result_login);
					if(hv!=null){
						if(hv.getPlayaddr().length()>4){
							VideoPlayerActivity.start(Activity_Video_Online.this,hv.getPlayaddr(), false);
						}
					}else{
						// parse exception
					}
					
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}

		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgScreenCaptureOfVideo:
			doCapture();
			break;
		case R.id.imgAddSaveOfVideo:
			doSave();
			break;
		case R.id.imgPlayOfVideo:
			play();
			break;
		case R.id.imgStopOfVideo:
			stop();
			break;
		case R.id.imgPlayHistoryOfVideo:
			if (MyApplication.cOnline != null) {
				startActivityForResult(new Intent(Activity_Video_Online.this, DateTimeDialog.class), 100);
			} else {
				MyApplication.getInstance().throwTips("请先登录选择通道！");
			}
			break;
		case R.id.rightButtonOfToperBarYuntai:
			if (MyApplication.cOnline != null) {
				MyApplication.packet.praiseChannel(MyApplication.cOnline.getChannelId(), handler_save);
			} else {
				MyApplication.getInstance().throwTips("请先登录选择通道！");
			}
			break;
		default:
			super.onClick(v);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		// 历史回放
		if (requestCode == 100) {
			String startTime = data.getStringExtra("startTime");
			String endTime = data.getStringExtra("endTime");
			channelOnLine co = MyApplication.cOnline;
			
			//get the rtmp address by tcp socketa
			loading.setVisibility(View.VISIBLE);
			MyApplication.packet.getVideoHistory(co.getDevice_id(), co.getChannelId(), startTime, endTime, handler);
		}

	}

	/**
	 * 暂停
	 */
	private void stop() {
		if (realPlay != null)
			AVNetSDK.AV_StopRealPlay(realPlay); // 停止实时监视
	}

	/*
	 * 开始监控
	 */
	private void play() {
		new playTask().execute();
	}

	private Handler handler_save = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String result = (String) msg.obj;
			System.out.println(result);
			
			switch (msg.what) {
			case 1:	//收藏
				if (result != null && result.contains("errdesc")) {
					String tip = result.substring(result.indexOf("<errdesc>") + 9, result.lastIndexOf("</errdesc>"));
					MyApplication.getInstance().throwTips(tip);
				}
				break;
			case 2: //攒次通道
				try {
					resutl_parise  parise =XmlToListService.GetCountOfZan(result);
					if(null!=parise){
						MyApplication.getInstance().throwTips(parise.getErrdesc());
						//set the count of zan on the top bar
						text_praise.setText(parise.getParise_count());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 增加收藏通道信息到服务器
	 */
	private void doSave() {
		if (MyApplication.cOnline != null) {
			MyApplication.packet.SaveChannel(handler_save, MyApplication.cOnline.getChannelName(), MyApplication.cOnline.getChannelNo(), MyApplication.cOnline.getChannelId());
		} else {
			MyApplication.getInstance().throwTips("请先登录选择通道，再来收藏！");
		}
	}

	class playTask extends AsyncTask<Void, integer, String> {

		@Override
		protected void onPreExecute() {
			loading.setVisibility(View.VISIBLE);

			playINParam = new AV_IN_RealPlay();
			playINParam.nChannelID = MyApplication.selectChannel; // 测试零号通道
			playINParam.nSubType = 1;
			playINParam.playView = bsView;
			playINParam.dataListener = new IAV_DataListener() {

				@Override
				public int onData(AV_HANDLE arg0, byte[] arg1, int arg2, int arg3, AV_MediaInfo arg4, Object arg5) {
					// TODO Auto-generated method stub
					return 0;
				}
			};

			playINParam.netWorkListener = new IAV_NetWorkListener() {

				@Override
				public int onConnectStatus(AV_HANDLE arg0, boolean arg1, AV_HANDLE arg2, Object arg3) {
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
				public void onRecordInfo(Object arg0, AV_Time arg1, AV_Time arg2, List<RecordInfo> arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public int onPlayPos(AV_HANDLE arg0, AV_PlayPosInfo arg1, Object arg2) {
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
			// 构造实时监视输出参数
			playOutParam = new AV_OUT_RealPlay();
		}

		@Override
		protected String doInBackground(Void... params) {
			if (MyApplication.log_handle != null) // 登陆成功才能播放
			{

				realPlay = AVNetSDK.AV_RealPlay(MyApplication.log_handle, playINParam, playOutParam);

			} else {
				return "请先登陆！";
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			loading.setVisibility(View.GONE);
			if (null != result) {
				MyApplication.getInstance().reLogin(new loaded4login() {
					@Override
					public void done() {
						login_failed++;
						if (login_failed < 3)
							new playTask().execute();
					}
				});
			}
		}
	}

	/**
	 * 截屏action
	 */
	private void doCapture() {

		System.out.println("test capture!");
		// if(null==av_capture_handler){
		AV_IN_Capture inParam = new AV_IN_Capture();
		inParam.captureListener = Activity_Video_Online.this;
		inParam.channelId = 0;// MyApplication.cOnline!=null?Integer.parseInt(MyApplication.cOnline.getChannelId()):0;
		inParam.imageSize = 100 * 100;
		AV_OUT_Capture outParam = new AV_OUT_Capture();

		av_capture_handler = AVNetSDK.AV_CreateCapture(MyApplication.log_handle, inParam, outParam);
		// }

		// start
		AVNetSDK.AV_StartCapture(av_capture_handler);
	}

	@Override
	public void captureData(int arg0, int arg1, byte[] arg2) {
		System.out.println(arg2.length + "截屏开始！");
		if (arg2 != null && arg2.length > 0) {
			System.out.println(arg2.length + "截屏返回成功！");
			// 把 byte转化为 图片
			Bitmap bmap = BitmapFactory.decodeByteArray(arg2, 0, arg2.length);

			File lewen = new File("/mnt/sdcard/lewen");

			if (!lewen.exists())
				lewen.mkdir();

			DateUtil dt = new DateUtil();
			String currentTime = dt.getNowTime("yyyy-MM-dd-hh-mm-ss");
			savePic(bmap, "/mnt/sdcard/lewen/" + currentTime + ".png");
			AVNetSDK.AV_StopCapture(av_capture_handler, arg1);

		}
	}

	private static Handler handler_toast = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String strFileName = (String) msg.obj;
			MyApplication.getInstance().throwTipsLong("抓图成功！保存路径：" + strFileName);
		}

	};

	// 保存到sdcard
	private static void savePic(Bitmap b, String strFileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFileName);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();

				handler_toast.sendMessage(handler_toast.obtainMessage(0, strFileName));

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
