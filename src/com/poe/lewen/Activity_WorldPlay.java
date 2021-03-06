package com.poe.lewen;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.poe.lewen.MyApplication.loaded4login;
import com.poe.lewen.adapter.TreeAdapter;
import com.poe.lewen.bean.Node;
import com.poe.lewen.bean.channel;
import com.poe.lewen.bean.channelOnLine;
import com.poe.lewen.service.XmlToListService;
import com.poe.lewen.vlc.VideoPlayerActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

public class Activity_WorldPlay extends Activity implements OnItemClickListener {

	private Button back, btn_model;
	private int RANKING_MODE = 1;// 0:普通排行 1：排行 2：地图模式
	private List<channel> list_channel = null;// 全球演示组织架构
	private LinearLayout progress;
	private Handler handler;
	private ListView listview;
	private TreeAdapter adapter;
	private Node root, selected_node;
	private HashMap<String, List<channelOnLine>> hash_online = new HashMap<String, List<channelOnLine>>();
	private channelOnLine conline = null;

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView = null;
	/**
	 * 用MapController完成地图控制
	 */
	private MapController mMapController = null;
	/**
	 * MKMapViewListener 用于处理地图事件回调
	 */
	MKMapViewListener mMapListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		MyApplication app = (MyApplication) this.getApplication();

		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			/**
			 * 如果BMapManager没有初始化则初始化BMapManager
			 */
			app.mBMapManager.init(MyApplication.strKey, new MyApplication.MyGeneralListener());
		}

		setContentView(R.layout.layout_world_play);

		init();
		// test rtsp
		// VideoPlayerActivity.start(Activity_WorldPlay.this, hubei_movie,
		// false);
	}

	public void init() {
		progress = (LinearLayout) findViewById(R.id.progressbarOfWorldPlay);
		progress.setVisibility(View.GONE);

		back = (Button) findViewById(R.id.leftButtonOfToperBarWorldPlay);
		btn_model = (Button) findViewById(R.id.rightButtonOfToperBarWorldPlay);

		listview = (ListView) findViewById(R.id.listviewOfWorldPlay);
		listview.setOnItemClickListener(this);

		initMap();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_model.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (RANKING_MODE == 2) {
					RANKING_MODE = 1;
					btn_model.setBackgroundResource(R.drawable.img_btn_model3);
					// 跳转到 listview
					listview.setVisibility(View.VISIBLE);
				} else {
					RANKING_MODE = 2;
					btn_model.setBackgroundResource(R.drawable.img_btn_model4);
					// 跳转到地图模式
					listview.setVisibility(View.GONE);
				}
			}
		});

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
							for (channel c : list_channel) {
								System.out.println(c.getName());
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
				}
			}
		};

		// 获取数据命令
		if (MyApplication.rsp_login != null) {
			doSendTcpRequest();
		} else {
			MyApplication.getInstance().throwTips("请登陆后查看本通道信息！");
		}

		// setAdapter();
	}

	// ****************
	// 配置无限级列表
	private void setAdapter() {
		adapter = new TreeAdapter(Activity_WorldPlay.this, makeDataNew());
		// 设置展开和折叠时图标
		adapter.setExpandedCollapsedIcon(R.drawable.icon_minus, R.drawable.icon_pus, R.drawable.icon_save_video);
		// 设置默认展开级别
		adapter.setExpandLevel(1);
		listview.setAdapter(adapter);
	}

	// ********
	// 制作根节点
	private Node makeData() {
		// 创建根节点
		root = new Node("全球演示点", "000000");
		// 创建1级子节点
		Node n1 = new Node("治安警察大队", "1");
		n1.setParent(root);// 设置父节点

		Node n11 = new Node("李伟", "13966664567");
		n11.setParent(n1);
		Node n12 = new Node("张同刚", "13966664567");
		n12.setParent(n1);

		n1.add(n11);
		n1.add(n12);

		// 创建1级子节点
		Node n2 = new Node("刑事警察大队", "2");
		n2.setParent(root);
		Node n21 = new Node("曹梦华", "13966664567");
		n21.setParent(n2);
		Node n22 = new Node("文燕", "13966664567");
		n22.setParent(n2);
		Node n23 = new Node("赵文涛", "13766604867");
		n23.setParent(n2);
		n2.add(n21);
		n2.add(n22);
		n2.add(n23);

		// 创建1级子节点
		Node n3 = new Node("巡警防暴大队", "3");
		n3.setParent(root);
		Node n31 = new Node("崔逊田", "15305510131");
		n31.setParent(n3);
		Node n32 = new Node("测试用户", "13855196982");
		n32.setParent(n3);

		// 创建2级子节点
		Node n33 = new Node("巡警第一中队", "31");
		n33.setParent(n3);

		Node n331 = new Node("张楠", "15890875672");
		n331.setParent(n33);
		// n331.setIcon(R.drawable.icon_police);
		Node n332 = new Node("阮明东", "15890875672");
		n332.setParent(n33);
		Node n333 = new Node("司徒正雄", "15890875672");
		n333.setParent(n33);
		// n333.setIcon(R.drawable.icon_police);
		n33.add(n331);
		n33.add(n332);
		n33.add(n333);

		n3.add(n31);
		n3.add(n32);
		n3.add(n33);

		root.add(n3);
		root.add(n1);
		root.add(n2);

		return root;
	}

	/**
	 * <node><id>1</id><name>成都双流机场</name><parent>0</parent></node>
	 * <node><id>2</id><name>T1航站楼</name><parent>1</parent></node>
	 * <node><id>3</id><name>1号通道</name><parent>2</parent></node>
	 * <node><id>4</id><name>2号通道</name><parent>2</parent></node>
	 * <node><id>5</id><name>重庆江北机场</name><parent>0</parent></node>
	 * <node><id>6</id><name>T2航站楼</name><parent>5</parent></node>
	 * <node><id>7</id><name>1号通道</name><parent>6</parent></node>
	 * <node><id>8</id><name>2号通道</name><parent>6</parent></node>
	 */
	// ********
	// 根据实时数据早root
	private Node makeDataNew() {
		// 创建根节点
		Node root = new Node("全球演示点", "000000");

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
		progress.setVisibility(View.VISIBLE);
		MyApplication.packet.getPlayingList(handler);
	}

	// 初始化 地图
	private void initMap() {
		mMapView = (MapView) findViewById(R.id.mapViewOfWorldPlay);
		/**
		 * 获取地图控制器
		 */
		mMapController = mMapView.getController();
		/**
		 * 设置地图是否响应点击事件 .
		 */
		mMapController.enableClick(true);
		/**
		 * 设置地图缩放级别
		 */
		mMapController.setZoom(12);

		/**
		 * 将地图移动至指定点
		 * 使用百度经纬度坐标，可以通过http://api.map.baidu.com/lbsapi/getpoint/index
		 * .html查询地理坐标 如果需要在百度地图上显示使用其他坐标系统的位置，请发邮件至mapapi@baidu.com申请坐标转换接口
		 */
		GeoPoint p;
		double cLat = 39.945;
		double cLon = 116.404;
		Intent intent = getIntent();
		if (intent.hasExtra("x") && intent.hasExtra("y")) {
			// 当用intent参数时，设置中心点为指定点
			Bundle b = intent.getExtras();
			p = new GeoPoint(b.getInt("y"), b.getInt("x"));
		} else {
			// 设置中心点为天安门
			p = new GeoPoint((int) (cLat * 1E6), (int) (cLon * 1E6));
		}

		mMapController.setCenter(p);

		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * 在此处理地图移动完成回调 缩放，平移等操作完成后，此回调被触发
				 */
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件 显示底图poi名称并移动至该点 设置过：
				 * mMapController.enableClick(true); 时，此回调才能被触发
				 * 
				 */
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(Activity_WorldPlay.this, title, Toast.LENGTH_SHORT).show();
					mMapController.animateTo(mapPoiInfo.geoPt);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 * 当调用过 mMapView.getCurrentMap()后，此回调会被触发 可在此保存截图至存储设备
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 * 地图完成带动画的操作（如: animationTo()）后，此回调被触发
				 */
			}

			/**
			 * 在此处理地图载完成事件
			 */
			@Override
			public void onMapLoadFinish() {
				Toast.makeText(Activity_WorldPlay.this, "地图加载完成", Toast.LENGTH_SHORT).show();

			}
		};
		mMapView.regMapViewListener(MyApplication.getInstance().mBMapManager, mMapListener);

	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		/**
		 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		 */
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
		System.out.println("item clicked!");
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
					doPlay();
				}
			}
		}
		// 这句话写在最后面
		((TreeAdapter) parent.getAdapter()).ExpandOrCollapse(position);
	}

	private void doPlay() {
		for (channelOnLine c : hash_online.get(selected_node.getParent().getText())) {
			if (c.getChannelName().equals(selected_node.getText())) {
				conline = c;
				break;
			}
		}
		
		// 最终播放 摄像头
		if (conline != null && conline.getPlayer_Addr() != null) {
			System.out.println("直播地址：" + conline.getPlayer_Addr());
			MyApplication.ip_dahua = conline.getDevice_ipAddr();
			MyApplication.prot_dahua = Integer.parseInt(conline.getDevice_portNo());
			MyApplication.username = conline.getUserName();
			MyApplication.password = conline.getUserPsw();
			MyApplication.selectChannel = 0;
//			if(MyApplication.cOnline!=null&&
//					!MyApplication.cOnline.getChannelId().equals(conline.getChannelId())){
//				//send the user action to service 
//				MyApplication.packet.WatchChannel(MyApplication.cOnline.getChannelId(), "0");
//			}
			MyApplication.cOnline = conline;
			//直接播放rtmp
//			VideoPlayerActivity.start(Activity_WorldPlay.this, conline.getPlayer_Addr(), false);
			
			// 可选择 1.直播 2.通道选择
			MyApplication.getInstance().reLogin(new loaded4login() {

				@Override
				public void done() {
					progress.setVisibility(View.GONE);
					AlertDialog.Builder ab = new AlertDialog.Builder(Activity_WorldPlay.this);
					ab.setTitle("是否进入直播？");
					ab.setPositiveButton("低码流", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							VideoPlayerActivity.start(Activity_WorldPlay.this, conline.getPlayer_Addr(), false);
						}

					});

					ab.setNegativeButton("高码流", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
//							startActivity(new Intent(Activity_WorldPlay.this, Activity_Video.class));
							Intent intent =new Intent(Activity_WorldPlay.this, Activity_Video.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
							startActivity(intent);
							overridePendingTransition(R.anim.bg_slide_left_in, R.anim.bg_slide_left_out);
							//send the user action to service 
							MyApplication.packet.WatchChannel(conline.getChannelId(), "1");
						}
					});
					ab.show();
				}
			});
		}
	}
}
