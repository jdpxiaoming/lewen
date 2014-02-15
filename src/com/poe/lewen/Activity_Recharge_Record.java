package com.poe.lewen;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.poe.lewen.adapter.adapter4Record;
import com.poe.lewen.bean.rsp_recharge_record;
import com.poe.lewen.service.XmlToListService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_Recharge_Record extends Activity {

	private ListView listview ;
	private TextView textTitle;
	private List<rsp_recharge_record> listDatas = null;
	private LinearLayout progressar ;
	private Button back;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			progressar.setVisibility(View.GONE);
			
			String result_login =(String) msg.obj;
			try {
				listDatas = XmlToListService.GetRechargeList(result_login);
				if(listDatas!=null){
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

		setContentView(R.layout.layout_recharge_record);
		
		init();
	}

	public void init() {

		//set title
		textTitle	=	(TextView) findViewById(R.id.textTitleOfToperBarSave);
		textTitle.setText("充值记录");
		
		listview	=	(ListView) findViewById(R.id.listviewOfRechargeRecord);
		progressar	=	(LinearLayout) findViewById(R.id.progressbarOfRechargeRecor);
		back			=	(Button) findViewById(R.id.leftButtonOfToperBarSave);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		if(MyApplication.rsp_login!=null){
			progressar.setVisibility(View.VISIBLE);
			MyApplication.packet.getRechargeRecord(MyApplication.rsp_login.getUserId(), handler);
		}else{
			MyApplication.getInstance().throwTips("您当前没有充值记录，请先登录！");
			startActivity(new Intent(Activity_Recharge_Record.this,Activity_Login.class));
			finish();
		}
	}

	private void setadapter() {
		adapter4Record adapter = new adapter4Record(listDatas,MyApplication.getInstance());
		listview.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
