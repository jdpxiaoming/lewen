package com.poe.lewen;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.mm.android.avnetsdk.AVNetSDK;
import com.poe.lewen.adapter.TreeAdapter;
import com.poe.lewen.adapter.adapter4MenueList;
import com.poe.lewen.adapter.adapter4Save;
import com.poe.lewen.bean.Node;
import com.poe.lewen.bean.channel;
import com.poe.lewen.bean.channelOnLine;
import com.poe.lewen.service.XmlToListService;
import com.poe.lewen.vlc.VideoPlayerActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

public class Activity_Home extends BaseActivity implements OnItemClickListener {

	//more
	private Button btn_more;
	private PopupWindow pw;
	
	//tip
	private TextView text_tip;
	
	//组织架构
	private List<channel> list_channel = null;// 全球演示组织架构
	private LinearLayout progress;
	private ListView listview ,listviewDemo;
	private Handler handler;
	private TreeAdapter adapter;
	private Node root, selected_node;
	private HashMap<String, List<channelOnLine>> hash_online = new HashMap<String, List<channelOnLine>>();
	private channelOnLine conline = null;
	
	//演示demo
	public  List<channelOnLine> list_channelOnLine = null;// demo List
	private adapter4Save adapter_demo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_main);
	}

	@Override
	public void init() {
		
		progress = (LinearLayout) findViewById(R.id.progressbarOfMain);
		progress.setVisibility(View.GONE);

		
		btn_more	=	(Button) findViewById(R.id.rightButtonOfToperBarHome);
		btn_more.setOnClickListener(this);
		
		text_tip	=	(TextView) findViewById(R.id.textTipOfMain);
		// footer init
		lin_menue.setBackgroundResource(R.drawable.btn_bg_press	);
		image_menue.setImageResource(R.drawable.icon_home_press);
		text_menue.setTextColor(Color.WHITE);
		
		
		listview	=	(ListView) findViewById(R.id.listviewOfMain);
		listview.setOnItemClickListener(this);
		
		listviewDemo	=	(ListView) findViewById(R.id.listview2OfMain);
		listviewDemo.setOnItemClickListener(this);
		// // set list adapter
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
								text_tip.setVisibility(View.GONE);
								for (channel c : list_channel) {
									System.out.println(c.getName());
								}
								// set adapter
								setadapter();
							} else {
								MyApplication.getInstance().throwTips("获取数据失败！");
								text_tip.setVisibility(View.VISIBLE);
							}
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case 2:// 获取直播地址 的通道
						try {

							List<channelOnLine> conlines = XmlToListService.GetVideoAddress(result_login);

							hash_online.put(selected_node.getText(), conlines);

							for (channelOnLine conline : conlines) {
								Node lead = MakeNode(conline, selected_node);
								selected_node.add(lead);
								// adapter.notifyDataSetChanged();
								adapter.update();
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case 3:
						try {
							list_channelOnLine = XmlToListService.GetVideoAddress(result_login);
							if (list_channelOnLine != null) {
								text_tip.setVisibility(View.GONE);
								for (channelOnLine c : list_channelOnLine) {
									System.out.println(c.getChannelName());
								}
								// set adapter
								setAdapter();
							} else {
								text_tip.setVisibility(View.VISIBLE);
								MyApplication.getInstance().throwTips("获取数据失败！");
							}
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
				}
			};

			// 获取数据命令
			doSendTcpRequest();
	}

	private void setAdapter() {
		adapter_demo = new adapter4Save(Activity_Home.this,list_channelOnLine);
		listviewDemo.setAdapter(adapter_demo);
	}
	
	private void showPopupWindow(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//popupwindow 需要一个单独的布局 xml来显示 这里是 一个包含一个listview的 xml布局
		final View listview = inflater.inflate(R.layout.layout_main_pop, null);

		//获取对应布局里面的listview控件。
		final ListView list = (ListView) listview.findViewById(R.id.listviewOfMainPop);

		pw = new PopupWindow(listview,300,LayoutParams.WRAP_CONTENT,true);
		//out side touch dismiss
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.setOutsideTouchable(true);
		String[] datasets = getResources().getStringArray(R.array.home_menue);
		BaseAdapter adapter = new adapter4MenueList(datasets, Activity_Home.this.getApplicationContext());
		list.setAdapter(adapter);
		OnItemClickListener listen3 =new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
//					MyApplication.getInstance().throwTips("click>>"+position);
					pw.dismiss();
					if(position==0){
						startActivity(new Intent(Activity_Home.this,Activity_Login.class));
						finish();
					}
					
//					if(position==1){
//						startActivity(new Intent(Activity_Home.this,Activity_WorldPlay2.class));
//					}
					
					if(position==1){
						startActivity(new Intent(Activity_Home.this,Activity_Save.class));
					}
					
					if(position ==2){
						startActivity(new Intent(Activity_Home.this,HelpShowImageActivity.class));
					}
					
					if(position == 3){//log out
						MyApplication.rsp_login=null;
						if (MyApplication.log_handle != null) {
							AVNetSDK.AV_Logout(MyApplication.log_handle);
							MyApplication.log_handle = null;
						}
						MyApplication.getInstance().throwTips("登出账号！");
						MyApplication.packet.disconnect();
						finish();
					}
			}
		};
		list.setOnItemClickListener(listen3);
		pw.showAsDropDown(btn_more);
	}
	
	private void setadapter() {
		adapter = new TreeAdapter(MyApplication.getInstance(), makeDataNew());
		// 设置展开和折叠时图标
		adapter.setExpandedCollapsedIcon(R.drawable.icon_minus, R.drawable.icon_pus, R.drawable.icon_save_video);
		// 设置默认展开级别
		adapter.setExpandLevel(1);
		listview.setAdapter(adapter);
	}

	// ********
		// 根据实时数据早root
		private Node makeDataNew() {
			// 创建根节点
			Node root = new Node("我的设备", "000000");

			// get the first level data collection
			List<channel> leve1s = getFirstLevel(list_channel);

			if (leve1s.size() > 0) {
				int i = 0;
				for (channel c : leve1s) {
					Node n1 = MakeNode(c, i++, root);
					root.add(n1);
				}
			}

			return root;
		}

		/**
		 * 制造Node
		 * 
		 * @param c
		 * @return
		 */
		private Node MakeNode(channel c, int i, Node parent) {
			Node node = new Node(c.getName(), c.getType());
			node.setId(c.getId());
			node.setParentId(c.getParent_id());
			node.setParent(parent);

			List<channel> childs = getChild(c.getId());
			if (childs.size() > 0) {
				for (channel c1 : childs) {
					node.add(MakeNode(c1, i + 1, node));
				}
			}

			return node;
		}

		private Node MakeNode(channelOnLine c, Node parent) {

			Node node = new Node(c.getChannelName(), "" + Integer.parseInt(parent.getValue()) + 1);
			node.setId(c.getChannelId());
			node.setParentId(parent.getId());
			node.setParent(parent);

			return node;
		}

		/**
		 * 获取child
		 * 
		 * @param id
		 * @return
		 */
		private List<channel> getChild(String id) {
			List<channel> childs = new ArrayList<channel>();

			for (int i = 0; i < list_channel.size(); i++) {
				if (list_channel.get(i).getParent_id().equals(id)) {
					childs.add(list_channel.get(i));
				}
			}

			return childs;
		}

		// 获取第一级组织
		private List<channel> getFirstLevel(List<channel> list_channel2) {
			List<channel> levels = new ArrayList<channel>();

			if (list_channel2 != null && list_channel2.size() > 0) {

				for (int i = 0; i < list_channel2.size(); i++) {

					channel c1 = list_channel2.get(i);
					boolean isFirst = true;
					for (int j = 0; j < list_channel2.size(); j++) {
						if (c1.getParent_id().equals(list_channel2.get(j).getId())) {
							isFirst = false;
							break;
						}
					}

					if (isFirst) {
						levels.add(c1);
					}
				}
			}

			return levels;
		}

		/**
		 * 获取直播组织架构
		 */
		private void doSendTcpRequest() {
			
			
			if (MyApplication.rsp_login != null) {
				
				if(list_channel==null||list_channel.size()==0){
					
					progress.setVisibility(View.VISIBLE);
					listview.setVisibility(View.VISIBLE);
					listviewDemo.setVisibility(View.GONE);
					MyApplication.packet.getPlayingList(handler);
				}
			} else {//未登录获取demo地址
//				text_tip.setVisibility(View.VISIBLE);
				if(list_channelOnLine==null||list_channelOnLine.size()==0){
					
					progress.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
					listviewDemo.setVisibility(View.VISIBLE);
					MyApplication.packet.getDemoList(handler);
				}
			}
			
		}
		
	@Override
	public void refresh(Object... param) {

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rightButtonOfToperBarHome:
			showPopupWindow(MyApplication.getInstance());
			break;
		default:
			super.onClick(v);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.rsp_login=null;
		MyApplication.getInstance().defaultSDKParam();
		if(null!=MyApplication.cOnline){
			MyApplication.packet.WatchChannel(MyApplication.cOnline.getChannelId(), "0");
			MyApplication.cOnline =null;
		}
		MyApplication.packet.disconnect();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
		
		System.out.println("item clicked!");
		if(listview.getVisibility()==View.VISIBLE){
			// 1.判断是否为 叶子节点
			selected_node = (Node) ((TreeAdapter) parent.getAdapter()).getItem(position);
			if (selected_node.isLeaf()) {

				if (selected_node.getValue().equals("1")) {// 门店 次终点
					progress.setVisibility(View.VISIBLE);
					// 发送请求：获取 第一个直播地址
					MyApplication.packet.getVideoAddress(selected_node.getId(), handler);
				} else {

					if (null!=selected_node.getParent()&&null != hash_online.get(selected_node.getParent().getText()) && hash_online.get(selected_node.getParent().getText()).size() > 0) {
						progress.setVisibility(View.VISIBLE);
//						doPlay();
						for (channelOnLine c : hash_online.get(selected_node.getParent().getText())) {
							if (c.getChannelName().equals(selected_node.getText())) {
								conline = c;
								doConnectChannel(c);
								break;
							}
						}
					}
				}
			}
			// 这句话写在最后面
			((TreeAdapter) parent.getAdapter()).ExpandOrCollapse(position);
		}else{//listviewDemo的天下
			
			if(list_channelOnLine!=null&&list_channelOnLine.size()>0){
				doConnectChannel(list_channelOnLine.get(position));
			}
		}
		
	}
	
	private void doConnectChannel(channelOnLine conline) {
		try {
//			progress.setVisibility(View.VISIBLE);
			if (conline != null && conline.getPlayer_Addr() != null) {
				System.out.println("直播地址：" + conline.getPlayer_Addr()+"ip:"+conline.getDevice_ipAddr()
						+"port:"+conline.getDevice_portNo());
				MyApplication.ip_dahua = conline.getDevice_ipAddr();
				MyApplication.prot_dahua = Integer.parseInt(conline.getDevice_portNo().equals("1")?"37777":conline.getDevice_portNo());
				MyApplication.username = conline.getUserName();
				MyApplication.password = conline.getUserPsw();
				MyApplication.selectChannel = 0;
				if(MyApplication.cOnline!=null&&
						!MyApplication.cOnline.getChannelId().equals(conline.getChannelId())){
					//send the user action to service 
					MyApplication.packet.WatchChannel(MyApplication.cOnline.getChannelId(), "0");
				}
				MyApplication.cOnline = conline;
				
				//直接播放rtmp
				VideoPlayerActivity.start(Activity_Home.this, conline.getPlayer_Addr(), false);
			}
		} catch (Exception e) {
			progress.setVisibility(View.GONE);
			e.printStackTrace();
		}
	}

}
