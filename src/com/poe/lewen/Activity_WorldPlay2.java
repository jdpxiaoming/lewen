package com.poe.lewen;

import java.io.UnsupportedEncodingException;
import java.util.List;
import com.poe.lewen.adapter.adapter4Save;
import com.poe.lewen.bean.channelOnLine;
import com.poe.lewen.service.XmlToListService;
import com.poe.lewen.vlc.VideoPlayerActivity;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Activity_WorldPlay2 extends Activity  {
	
	private Button back, btn_model;
	private ListView listview;
	private adapter4Save adapter;
	private LinearLayout progress;
	private Handler handler;
	public  List<channelOnLine> list_channel = null;// demo List

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_world_play);
		init();
	}

	public void init() {
		progress = (LinearLayout) findViewById(R.id.progressbarOfWorldPlay);
		back = (Button) findViewById(R.id.leftButtonOfToperBarWorldPlay);
		btn_model = (Button) findViewById(R.id.rightButtonOfToperBarWorldPlay);
		listview = (ListView) findViewById(R.id.listviewOfWorldPlay);

		btn_model.setVisibility(View.GONE);
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
				// TODO Auto-generated method stub
				// MyApplication.selectChannel = arg2;
				// startActivity(new Intent(Activity_Save.this,
				// Activity_Video.class));
				// finish();
				// 发送请求：获取 第一个直播地址
				// Packet.getVideoAddress(list_channel.get(arg2).getId(),
				// handler);
				doConnectChannel(list_channel.get(arg2));
			}
		});

		// 如果登陆了 ，去 执行socket请求获取 数据 否则 、提示 先登录

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 接受登录返回数据
				progress.setVisibility(View.GONE);
				String result_login = (String) msg.obj;
				switch (msg.what) {
				case 1:
					try {
						list_channel = XmlToListService.GetVideoAddress(result_login);
						if (list_channel != null) {
							for (channelOnLine c : list_channel) {
								System.out.println(c.getChannelName());
							}
							// set adapter
							setAdapter();
						} else {
							MyApplication.getInstance().throwTips("获取数据失败！");
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 2:
					break;
				}
			}

		};

		// 获取数据命令
			doSendTcpRequest();
	}

	private void doConnectChannel(final channelOnLine conline) {
		try {
			progress.setVisibility(View.VISIBLE);
			if (conline != null && conline.getPlayer_Addr() != null) {
				System.out.println("直播地址：" + conline.getPlayer_Addr());
				MyApplication.ip_dahua = conline.getDevice_ipAddr();
				MyApplication.prot_dahua = Integer.parseInt(conline.getDevice_portNo());
				MyApplication.username = conline.getUserName();
				MyApplication.password = conline.getUserPsw();
				MyApplication.selectChannel = 0;
				if(MyApplication.cOnline!=null&&
						!MyApplication.cOnline.getChannelId().equals(conline.getChannelId())){
					//send the user action to service 
					MyApplication.packet.WatchChannel(MyApplication.cOnline.getChannelId(), "0");
				}
				MyApplication.cOnline = conline;
			}
		} catch (Exception e) {
			progress.setVisibility(View.GONE);
			e.printStackTrace();
		}
	}

	private void setAdapter() {
		adapter = new adapter4Save(Activity_WorldPlay2.this,list_channel);
		listview.setAdapter(adapter);
	}

	/**
	 * 获取直播组织架构
	 */
	private void doSendTcpRequest() {
		progress.setVisibility(View.VISIBLE);
		MyApplication.packet.getDemoList(handler);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		progress.setVisibility(View.GONE);
	}
}
