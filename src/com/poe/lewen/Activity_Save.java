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
import android.widget.Button;
import android.widget.ListView;

//我的收藏
public class Activity_Save extends BaseActivity {

	private Button back;
	private ListView listview ;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_save);

	}

	@Override
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
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			}
		});
		
		setadapter();
	}

	private void setadapter() {
		// TODO Auto-generated method stub
		String[] datasets = getResources().getStringArray(R.array.home_menue);
		BaseAdapter adapter = new adapter4MenueList(datasets, Activity_Save.this.getApplicationContext());
		listview.setAdapter(adapter);
	}

	@Override
	public void refresh(Object... param) {

	}
	
}
