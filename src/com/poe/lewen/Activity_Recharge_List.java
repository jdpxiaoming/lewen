package com.poe.lewen;

import com.poe.lewen.adapter.adapter4MenueList;
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
import android.widget.TextView;

public class Activity_Recharge_List extends Activity {

	private ListView listview ;
	private TextView textTitle;
	private Button back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_recharge_list);
		
		init();
	}

	public void init() {

		//set title
		textTitle	=	(TextView) findViewById(R.id.textTitleOfToperBarSave);
		textTitle.setText(getString(R.string.str_bank_reockrd));
		
		back			=	(Button) findViewById(R.id.leftButtonOfToperBarSave);
		listview	=	(ListView) findViewById(R.id.listviewOfRechargeList);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(arg2==0){
					startActivity(new Intent(Activity_Recharge_List.this,Activity_Recharge_Record.class));
					overridePendingTransition(R.anim.bg_slide_left_in, R.anim.bg_slide_left_out);
				}
				if(arg2==1){
					startActivity(new Intent(Activity_Recharge_List.this,Activity_Consume_Record.class));
					overridePendingTransition(R.anim.bg_slide_left_in, R.anim.bg_slide_left_out);
				}
			}
		});
		
		setadapter();
	}

	private void setadapter() {
		String[] datasets = getResources().getStringArray(R.array.bank_menue);
		BaseAdapter adapter = new adapter4MenueList(datasets, Activity_Recharge_List.this.getApplicationContext());
		listview.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
