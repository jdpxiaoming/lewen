package com.poe.lewen;

//import com.poe.view_vlc.VideoView4VLC;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Activity_Video_Play extends Activity {

	private Button back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_video_play);

		init();
	}

	public void init() {

		back = (Button) findViewById(R.id.leftButtonOfToperBarVideoPlay);

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
