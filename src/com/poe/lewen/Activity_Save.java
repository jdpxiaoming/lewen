package com.poe.lewen;

import com.poe.lewen.adapter.adapter4MenueList;
import com.poe.lewen.adapter.adapter4YanshiList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

//我的收藏
public class Activity_Save extends  Activity {

	private Button back;
	private ListView listview ;
	private adapter4YanshiList adapter ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_save);
		init();
	}

	public void init() {
		back		=	(Button) findViewById(R.id.leftButtonOfToperBarSave);
		
		listview	=	(ListView) findViewById(R.id.listviewOfSave);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		//set list adapter
				adapter = new  adapter4YanshiList(Activity_Save.this);
				listview.setAdapter(adapter);
				
				listview.setOnItemClickListener(new OnItemClickListener(	) {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						MyApplication.selectChannel = arg2;
						startActivity(new Intent(Activity_Save.this, Activity_Video.class));
						finish();
					}
				});
	}
}
