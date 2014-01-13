package com.poe.lewen;

import java.io.UnsupportedEncodingException;
import java.util.List;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.poe.lewen.adapter.TreeAdapter;
import com.poe.lewen.adapter.adapter4YanshiList;
import com.poe.lewen.bean.Constant;
import com.poe.lewen.bean.Node;
import com.poe.lewen.bean.channel;
import com.poe.lewen.service.XmlToListService;
import com.poe.lewen.util.Packet;
import com.poe.lewen.util.XMLUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
	private adapter4YanshiList adapter;
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
		// adapter = new adapter4YanshiList(Activity_WorldPlay.this);
		// listview.setAdapter(adapter);

		// listview.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // TODO Auto-generated method stub
		// MyApplication.selectChannel = arg2;
		// startActivity(new Intent(Activity_WorldPlay.this,
		// Activity_Video.class));
		// finish();
		// }
		// });

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 接受登录返回数据
				progress.setVisibility(View.GONE);

				String result_login = (String) msg.obj;
				try {
					list_channel = XmlToListService.GetChannelList(result_login);
					if (list_channel != null) {
						for (channel c : list_channel) {
							System.out.println(c.getName());
						}
						// set adapter
						setAdapter();
						
						//获取最后一个直播地址：
						//发送请求：获取 第一个直播地址
						String tmp =  XMLUtil.MakeXML4PlayAddress(list_channel.get(list_channel.size()-1).getId());
						Packet.getVideoAddress(list_channel.get(list_channel.size()-1).getId(), null);
					
					}
					MyApplication.getInstance().throwTips("获取数据失败！");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		// 获取数据命令
		 doSendTcpRequest();

//		setAdapter();
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
	// ****************
	// 配置无限级列表
	private void setAdapter() {
		TreeAdapter ta = new TreeAdapter(Activity_WorldPlay.this, makeData());
		// 设置展开和折叠时图标
		ta.setExpandedCollapsedIcon(R.drawable.icon_minus, R.drawable.icon_pus, R.drawable.icon_save_video);
		// 设置默认展开级别
		ta.setExpandLevel(1);
		listview.setAdapter(ta);
	}

	private Node makeData() {
		// 创建根节点
				Node root = new Node("全球演示点", "000000");
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
	
	//********
	//根据实时数据早root
	private Node makeDataNew() {
		// 创建根节点
				Node root = new Node("全球演示点", "000000");
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
	 * 获取直播组织架构
	 */
	private void doSendTcpRequest() {
		progress.setVisibility(View.VISIBLE);
		Packet.getPlayingList(handler);
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
		// 这句话写在最后面
		((TreeAdapter) parent.getAdapter()).ExpandOrCollapse(position);
	}
}
