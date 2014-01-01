package com.poe.lewen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_Video extends BaseActivity implements OnClickListener{

	private LinearLayout lin_menue, lin_video, lin_yuntai, lin_map;
	private ImageView image_menue, image_video, image_yuntai, image_map;
	private TextView text_menue, text_video, text_yuntai, text_map;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_video);

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		// footer init
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
		
		//footer select index default
		lin_video.setBackgroundResource(R.drawable.btn_bg_press	);
		image_video.setImageResource(R.drawable.icon_video_press);
		text_video.setTextColor(Color.WHITE);
		
		lin_menue.setOnClickListener(this);
		lin_video.setOnClickListener(this);
		lin_yuntai.setOnClickListener(this);
		lin_map.setOnClickListener(this);
		
		
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lin_menue:
			startActivity(new Intent(Activity_Video.this, Activity_Home.class));
			break;
		case R.id.lin_video:
			break;
		case R.id.lin_yuntai:
			startActivity(new Intent(Activity_Video.this, Activity_Yuntai.class));
			break;
		case R.id.lin_map:
			startActivity(new Intent(Activity_Video.this, Activity_Map.class));
			break;
		default:
			break;
		}
	}

}
