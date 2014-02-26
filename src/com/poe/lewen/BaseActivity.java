package com.poe.lewen;

import com.mm.android.avplaysdk.IViewListener;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseActivity extends Activity implements OnClickListener, IViewListener{
	
	 LinearLayout lin_menue, lin_video, lin_yuntai, lin_map;
	 ImageView image_menue, image_video, image_yuntai, image_map;
	 TextView text_menue, text_video, text_yuntai, text_map;
	 Context mContext;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
	}
	
    public abstract void init();
    public abstract void refresh(Object ... param);
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		lin_menue = (LinearLayout) findViewById(R.id.lin_menue);
		image_menue = (ImageView) findViewById(R.id.image_menue);
		text_menue = (TextView) findViewById(R.id.text_menue);

		lin_video = (LinearLayout) findViewById(R.id.lin_video);
		image_video = (ImageView) findViewById(R.id.image_video);
		text_video = (TextView) findViewById(R.id.text_video);

		lin_yuntai = (LinearLayout) findViewById(R.id.lin_yuntai);
		image_yuntai = (ImageView) findViewById(R.id.image_yuntai);
		text_yuntai = (TextView) findViewById(R.id.text_yuntai);

		lin_map = (LinearLayout) findViewById(R.id.lin_map);
		image_map = (ImageView) findViewById(R.id.image_map);
		text_map = (TextView) findViewById(R.id.text_map);
		
		lin_menue.setOnClickListener(this);
		lin_video.setOnClickListener(this);
		lin_yuntai.setOnClickListener(this);
		lin_map.setOnClickListener(this);
		
		init();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lin_menue:
			startActivity(new Intent(mContext, Activity_Home.class));
			break;
		case R.id.lin_video:
//			startActivity(new Intent(mContext, Activity_Video.class));
			Intent intent =new Intent(mContext, Activity_Video.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(intent);
			break;
		case R.id.lin_yuntai:
			startActivity(new Intent(mContext, Activity_Yuntai.class));
			break;
		case R.id.lin_map:
			startActivity(new Intent(mContext, Activity_Map.class));
			break;
		default:
//			onClick(v);
			break;
		}
		
	}
	
	@Override
	public void onViewMessage(int arg0, SurfaceView arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		System.out.println("baseactivity: motionEvent:"+ev.getAction());
		return super.dispatchTouchEvent(ev);
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPause(this);
	}
}

