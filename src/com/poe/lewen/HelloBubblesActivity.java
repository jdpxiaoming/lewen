package com.poe.lewen;

import java.io.File;
import java.io.IOException;
import com.poe.lewen.adapter.DiscussArrayAdapter;
import com.poe.lewen.util.Common;
import com.poe.lewen.util.TcpUtil;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;

public class HelloBubblesActivity extends Activity {
	private static final String LOG_TAG = "HelloBubblesActivity";
	private  DiscussArrayAdapter adapter;
	private ListView lv;
	private Button btn_speak;
	
	//录音
	private static String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/audiorecordmp3.mp3";
	//收到的录音
	private static String mFileName2 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/audio.mp3";
	// 录音按钮
	private MediaRecorder mRecorder = null;
	// 回放按钮
	private MediaPlayer mPlayer = null;

	private String toUserId ;
	//语音数据
	private byte[] temp = null;
	
	private long startTime,endTime;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if(null!=msg.obj){
				temp = (byte[]) msg.obj;
			}
			if(temp!=null){
				System.out.println("temp size:"+temp.length);
				Common.saveFile(mFileName2, temp);
				startPlaying();
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						TestDiff();
					}
				}, 5000);
			}
		}
	};
	
	private void startRecording() {
		startTime  = System.currentTimeMillis();
		mRecorder = new MediaRecorder();
		// 设置音源为Micphone
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置封装格式
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mRecorder.setOutputFile(mFileName);
		// 设置编码格式
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mRecorder.start();
	}

	private void stopRecording() {
		endTime = System.currentTimeMillis();
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		//转化 文件为 byte 数组

		if(endTime-startTime<1.1*1000){
			MyApplication.getInstance().throwTips("时间太短！");
		}else{
			//1.删除已经存在的缓存
			temp	=	Common.readFileDataBytes(mFileName);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					File f = new File(mFileName2);
					System.out.println(f.delete());
					
					System.out.println("制作语音byte[]:"+temp.length+">>"+TcpUtil.getStringFromBuff(temp));
					TcpUtil.reqSpeak(MyApplication.rsp_login.getUserId(), toUserId, temp, handler);
				}
			}).start();
		}
	}
	
	private void startPlaying() {
		mPlayer = new MediaPlayer();
		try {
			// 设置要播放的文件
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			// 播放之
			mPlayer.start();
			
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
		
	}  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discuss);

		toUserId	=getIntent().getStringExtra("toUserId");
		lv = (ListView) findViewById(R.id.listView1);
		btn_speak		=	(Button) findViewById(R.id.btnSpeakOfDiscuss);
		
		adapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);
		lv.setAdapter(adapter);

		btn_speak.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					//开始录音
					btn_speak.setText("松开发送！");
					startRecording();
				}
				
				if(event.getAction()==MotionEvent.ACTION_UP){
					//结束录音并发送
					btn_speak.setText("按住录音！");
					stopRecording();
				}
				
				return true;
			}
		});
//		editText1.setOnKeyListener(new OnKeyListener() {
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				// If the event is a key-down event on the "enter" button
//				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//					// Perform action on key press
//					adapter.add(new OneComment(false, editText1.getText().toString()));
//					editText1.setText("");
//					return true;
//				}
//				return false;
//			}
//		});
		
		
		TestDiff();
	}

	private void TestDiff() {
		// TODO Auto-generated method stub
		byte[] b1 = Common.readFileDataBytes(mFileName);
		byte[] b2 = Common.readFileDataBytes(mFileName2);
		if(b2!=null){
		for(int i=0;i<b1.length;i++){
			if(i<b2.length){
				
				if(b1[i]!=b2[i]){
					System.out.println("postion:"+i+" b1:"+b1[i] +"b2: "+b2[i]);
				}
			}
		}
		}
	}
}