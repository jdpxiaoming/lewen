package com.poe.lewen;

//import com.poe.view_vlc.VideoView4VLC;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Activity_Video_Play extends Activity {

	private Button back;
//	private VideoView4VLC videoview;
	private String hubei_movie = "http://live9.hbtv.com.cn/channels/zbk/hbys/flv:sd/live";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_video_play);

		init();
	}

	public void init() {

//		videoview = (VideoView4VLC) findViewById(R.id.videoOfVideoPlay);
		back = (Button) findViewById(R.id.leftButtonOfToperBarVideoPlay);
		// get the rtmp address
		// String rtsp =getIntent().getStringExtra("rtsp");

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		//test rtsp
//		videoview.start(hubei_movie);
	}

	@Override
	protected void onDestroy() {
		
//		videoview.stopPlayback();
		super.onDestroy();
	}
}
