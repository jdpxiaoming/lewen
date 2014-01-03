package com.poe.lewen;

import com.poe.lewen.adapter.adapter4MenueList;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_Home extends BaseActivity  implements OnClickListener{

	private LinearLayout lin_menue, lin_video, lin_yuntai, lin_map;
	private ImageView image_menue, image_video, image_yuntai, image_map;
	private TextView text_menue, text_video, text_yuntai, text_map;
	private ListView listview ;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
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
		lin_menue.setBackgroundResource(R.drawable.btn_bg_press	);
		image_menue.setImageResource(R.drawable.icon_home_press);
		text_menue.setTextColor(Color.WHITE);
		
		
		lin_menue.setOnClickListener(this);
		lin_video.setOnClickListener(this);
		lin_yuntai.setOnClickListener(this);
		lin_map.setOnClickListener(this);
		
		listview	=	(ListView) findViewById(R.id.listviewOfMenue);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				Toast.makeText(Activity_Home.this.getApplicationContext(), "clicked!"+arg2, 300).show();
				if(arg2==0){
					startActivity(new Intent(Activity_Home.this,Activity_Login.class));
				}
				
				if(arg2==1){
					startActivity(new Intent(Activity_Home.this,Activity_WorldPlay.class));
				}
				
				if(arg2==2){
					startActivity(new Intent(Activity_Home.this,Activity_Save.class));
				}
				
				if(arg2 ==3){
					startActivity(new Intent(Activity_Home.this,HelpShowImageActivity.class));
				}
			}
		});
		
		setadapter();
		
		 //如果首次登陆 进入 登陆界面 ，否则 直接结束当前页面
		 if(MyApplication.getPreferenceData("first")==null){
			 MyApplication.pushPreferenceData("first", "yes");
				startActivity(new Intent(Activity_Home.this,HelpShowImageActivity.class));
		 }
	}

	private void setadapter() {
		// TODO Auto-generated method stub
		String[] datasets = getResources().getStringArray(R.array.home_menue);
		BaseAdapter adapter = new adapter4MenueList(datasets, Activity_Home.this.getApplicationContext());
		listview.setAdapter(adapter);
	}

	@Override
	public void refresh(Object... param) {

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lin_menue:
			break;
		case R.id.lin_video:
			startActivity(new Intent(Activity_Home.this, Activity_Video.class));
			break;
		case R.id.lin_yuntai:
			startActivity(new Intent(Activity_Home.this, Activity_Yuntai.class));
			break;
		case R.id.lin_map:
			startActivity(new Intent(Activity_Home.this, Activity_Map.class));
			break;
		default:
			break;
		}
	}


}
