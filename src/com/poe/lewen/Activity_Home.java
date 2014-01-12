package com.poe.lewen;

import com.poe.lewen.adapter.adapter4MenueList;
import com.poe.lewen.util.Packet;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class Activity_Home extends BaseActivity {

	private ListView listview ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_main);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		// footer init
		lin_menue.setBackgroundResource(R.drawable.btn_bg_press	);
		image_menue.setImageResource(R.drawable.icon_home_press);
		text_menue.setTextColor(Color.WHITE);
		
		
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyApplication.rsp_login=null;
//		MyApplication.getInstance().closeSocket();
		Packet.close();
	}
}
