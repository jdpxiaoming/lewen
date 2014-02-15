package com.poe.lewen;

import java.io.UnsupportedEncodingException;
import java.util.List;
import com.poe.lewen.adapter.adapter4UserList;
import com.poe.lewen.service.XmlToListService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_User_List extends Activity {

	private ListView listview ;
	private TextView textTitle;
	private Button back;
	private List<String> userList = null;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
//			progressar.setVisibility(View.GONE);
			
			String result_login =(String) msg.obj;
			try {
				userList = XmlToListService.GetUserNameList(result_login);
				if(userList!=null){
					//set adapter
					setadapter();
				}else{
					MyApplication.getInstance().throwTips("获取数据失败！");
//					Packet.close();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_recharge_list);
		
		init();
	}

	public void init() {

		//set title
		textTitle	=	(TextView) findViewById(R.id.textTitleOfToperBarSave);
		textTitle.setText(getString(R.string.str_user_list));
		
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
					startActivity(new Intent(Activity_User_List.this,Activity_Recharge_Record.class));
				}
			}
		});
		
//		setadapter();
		MyApplication.packet.getUserList(handler);
	}

	private void setadapter() {
		BaseAdapter adapter = new adapter4UserList(userList, Activity_User_List.this.getApplicationContext());
		listview.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
