package com.poe.lewen;

import java.util.ArrayList;
import java.util.List;
import com.poe.lewen.adapter.SystemUrlSetAdapter;
import com.poe.lewen.bean.ServiceInfo;
import com.poe.lewen.util.HttpUtil;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SystemUrlSet extends Activity {
	private Button btn_back,btn_ok;
	private List<ServiceInfo> serviceInfoList = new ArrayList<ServiceInfo>();
	private LinearLayout progressbar;
//	public static int selected_item = -1;
	private SystemUrlSetAdapter adapter =null;
	private ListView listView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去title
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.webservice_set);
		
		init();
	}

	public void init() {

		btn_back		=	(Button) findViewById(R.id.btn_backOfSystemToperBar);
		
		btn_ok			=	(Button) findViewById(R.id.btn_okOfSystemToperBar);
		
		progressbar 	= (LinearLayout) findViewById(R.id.progressbarOfWebserviceSet);
		
		listView		=	(ListView) findViewById(R.id.listViewOfWebServiceSet);
		
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
					
					adapter.setSelectdObject(arg2);
					
			}
		});
		
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SystemUrlSet.this.finish();
			}
		});
		
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//跳转到登陆页面
				Intent intent = new Intent(SystemUrlSet.this,Activity_Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				
				SystemUrlSet.this.finish();
				
			}
		});

		// 开启任务
		if (HttpUtil.checkNet(SystemUrlSet.this)) {
			
			myTask ts = new myTask();
			
			ts.execute(" ");
			
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
				
				Intent intent = new Intent(SystemUrlSet.this,Activity_Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				SystemUrlSet.this.finish();
		}
		
		return true;
	}



	/**
	 * 获取 服务器的最新列表地址
	 * @author poe.Cai
	 *
	 */
	final class myTask extends AsyncTask<String, integer, List<ServiceInfo>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// 开启progressbar
			progressbar.setVisibility(View.VISIBLE);
		}

		@Override
		protected List<ServiceInfo> doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<ServiceInfo> serviceList = null;
			
			return serviceList;
		}

		@Override
		protected void onPostExecute(List<ServiceInfo> result) {
			
			// TODO Auto-generated method stub
			super.onPostExecute(result);
				// 采用 备用的 本地服务器地址 列表 默认是 2个
				ServiceInfo s1 = new ServiceInfo();
				s1.setServiceName("联通网址一");
				s1.setServiceUrl(SystemUrlSet.this.getApplication().getString(R.string.host1));
				s1.setPort(SystemUrlSet.this.getApplication().getString(R.string.port1));
				serviceInfoList.add(s1);
				
				ServiceInfo s2 = new ServiceInfo();
				s2.setServiceName("电信网址二");
				s2.setServiceUrl(SystemUrlSet.this.getApplication().getString(R.string.host2));
				s2.setPort(SystemUrlSet.this.getApplication().getString(R.string.port2));
				serviceInfoList.add(s2);
				
				ServiceInfo s3 = new ServiceInfo();
				s3.setServiceName("本地测试byPoe");
				s3.setServiceUrl(SystemUrlSet.this.getApplication().getString(R.string.host3));
				s3.setPort(SystemUrlSet.this.getApplication().getString(R.string.port3));
				serviceInfoList.add(s3);

			if (serviceInfoList.size() > 0) {
				//设置服务器选择列表
				adapter = new SystemUrlSetAdapter(SystemUrlSet.this, serviceInfoList);
				listView.setAdapter(adapter);
			}
			progressbar.setVisibility(View.GONE);
			
		}
	}
}
