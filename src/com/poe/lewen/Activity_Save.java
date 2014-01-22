package com.poe.lewen;

import java.io.UnsupportedEncodingException;
import java.util.List;
import com.poe.lewen.MyApplication.loaded4login;
import com.poe.lewen.adapter.adapter4Save;
import com.poe.lewen.bean.channel;
import com.poe.lewen.bean.channelOnLine;
import com.poe.lewen.service.XmlToListService;
import com.poe.lewen.util.Packet;
import com.poe.lewen.vlc.VideoPlayerActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

//我的收藏
public class Activity_Save extends Activity {

	private Button back;
	private ListView listview;
	private adapter4Save adapter;
	private LinearLayout progress;
	private Handler handler;
	public static List<channel> list_channel = null;// 收藏的组织架构

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_save);
		init();
	}

	public void init() {
		progress = (LinearLayout) findViewById(R.id.progressbarOfSave);
		back = (Button) findViewById(R.id.leftButtonOfToperBarSave);

		listview = (ListView) findViewById(R.id.listviewOfSave);

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
//				MyApplication.selectChannel = arg2;
//				startActivity(new Intent(Activity_Save.this, Activity_Video.class));
//				finish();
				progress.setVisibility(View.VISIBLE);
				// 发送请求：获取 第一个直播地址
				Packet.getVideoAddress(list_channel.get(arg2).getId(), handler);
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
						list_channel = XmlToListService.GetChannelList(result_login);
						if (list_channel != null) {
							for (channel c : list_channel) {
								System.out.println(c.getName());
							}
							// set adapter
							setAdapter();
						}else{
							MyApplication.getInstance().throwTips("获取数据失败！");
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 2:
					try {
						final channelOnLine conline = XmlToListService.GetVideoAddress(result_login).get(0);
						if(conline!=null&&conline.getPlayer_Addr()!=null){
							System.out.println("直播地址："+conline.getPlayer_Addr());
							MyApplication.ip_dahua = conline.getDevice_ipAddr();
							MyApplication.prot_dahua	= Integer.parseInt(conline.getDevice_portNo());
							MyApplication.username	=	conline.getUserName();
							MyApplication.password	=	conline.getUserPsw();
							MyApplication.selectChannel  = 0;
							
							//可选择 1.直播  2.通道选择
							MyApplication.getInstance().reLogin(new loaded4login() {
								
								@Override
								public void done() {
									AlertDialog.Builder ab=new AlertDialog.Builder(Activity_Save.this);
									ab.setTitle("是否进入直播？选择否进入普通模式");
									ab.setPositiveButton("高码流", new DialogInterface.OnClickListener(){
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											
											VideoPlayerActivity.start(Activity_Save.this, conline.getPlayer_Addr(), false);
										}
										
									});
									
									ab.setNegativeButton("低码流", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											
											startActivity(new Intent(Activity_Save.this,Activity_Video.class));
										}
									});
									
									ab.show();
								}
							});
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		};

		// 获取数据命令
		if (MyApplication.rsp_login != null) {
			doSendTcpRequest();
		} else {
			MyApplication.getInstance().throwTips("请登陆后查看本通道信息！");
		}
	}
	
	private void setAdapter() {
		 adapter = new adapter4Save(Activity_Save.this);
		 listview.setAdapter(adapter);
	}
	
	
	/**
	 * 获取直播组织架构
	 */
	private void doSendTcpRequest() {
		progress.setVisibility(View.VISIBLE);
		Packet.getPlayingList(handler);
	}
}
