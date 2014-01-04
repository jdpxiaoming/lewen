package com.poe.lewen;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.poe.lewen.adapter.adapter4YanshiList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Activity_WorldPlay extends  Activity {

	private Button back,btn_model;
	private int RANKING_MODE =1;//0:普通排行 1：排行 2：地图模式
	private ListView listview;
	private adapter4YanshiList adapter ;
	/**
	 *  MapView 是地图主控件
	 */
	private MapView mMapView = null;
	/**
	 *  用MapController完成地图控制 
	 */
	private MapController mMapController = null;
	/**
	 *  MKMapViewListener 用于处理地图事件回调
	 */
	MKMapViewListener mMapListener = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication app = (MyApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            /**
             * 如果BMapManager没有初始化则初始化BMapManager
             */
            app.mBMapManager.init(MyApplication.strKey,new MyApplication.MyGeneralListener());
        }
		setContentView(R.layout.layout_world_play);
		
		init();
	}

	public void init() {
		// TODO Auto-generated method stub
		back		=	(Button) findViewById(R.id.leftButtonOfToperBarWorldPlay);
		btn_model	=	(Button) findViewById(R.id.rightButtonOfToperBarWorldPlay);
		
		
		listview		=	(ListView) findViewById(R.id.listviewOfWorldPlay);
		
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
				
				if(RANKING_MODE==2){
					RANKING_MODE = 1;
					btn_model.setBackgroundResource(R.drawable.img_btn_model3);
					//跳转到 listview
					listview.setVisibility(View.VISIBLE);
				}else{
					RANKING_MODE=2;
					btn_model.setBackgroundResource(R.drawable.img_btn_model4);
					//跳转到地图模式
					listview.setVisibility(View.GONE);
				}
			}
		});
		
		
		//set list adapter
		adapter = new  adapter4YanshiList(Activity_WorldPlay.this);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener(	) {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				MyApplication.selectChannel = arg2;
				startActivity(new Intent(Activity_WorldPlay.this, Activity_Video.class));
				finish();
			}
		});
	}
	//初始化 地图
	private void initMap(){
		 mMapView = (MapView)findViewById(R.id.mapViewOfWorldPlay);
	        /**
	         * 获取地图控制器
	         */
	        mMapController = mMapView.getController();
	        /**
	         *  设置地图是否响应点击事件  .
	         */
	        mMapController.enableClick(true);
	        /**
	         * 设置地图缩放级别
	         */
	        mMapController.setZoom(12);
	       
	        /**
	         * 将地图移动至指定点
	         * 使用百度经纬度坐标，可以通过http://api.map.baidu.com/lbsapi/getpoint/index.html查询地理坐标
	         * 如果需要在百度地图上显示使用其他坐标系统的位置，请发邮件至mapapi@baidu.com申请坐标转换接口
	         */
	        GeoPoint p ;
	        double cLat = 39.945 ;
	        double cLon = 116.404 ;
	        Intent  intent = getIntent();
	        if ( intent.hasExtra("x") && intent.hasExtra("y") ){
	        	//当用intent参数时，设置中心点为指定点
	        	Bundle b = intent.getExtras();
	        	p = new GeoPoint(b.getInt("y"), b.getInt("x"));
	        }else{
	        	//设置中心点为天安门
	        	 p = new GeoPoint((int)(cLat * 1E6), (int)(cLon * 1E6));
	        }
	        
	        mMapController.setCenter(p);
	        
	        /**
	    	 *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
	    	 */
	        mMapListener = new MKMapViewListener() {
				@Override
				public void onMapMoveFinish() {
					/**
					 * 在此处理地图移动完成回调
					 * 缩放，平移等操作完成后，此回调被触发
					 */
				}
				
				@Override
				public void onClickMapPoi(MapPoi mapPoiInfo) {
					/**
					 * 在此处理底图poi点击事件
					 * 显示底图poi名称并移动至该点
					 * 设置过： mMapController.enableClick(true); 时，此回调才能被触发
					 * 
					 */
					String title = "";
					if (mapPoiInfo != null){
						title = mapPoiInfo.strText;
						Toast.makeText(Activity_WorldPlay.this,title,Toast.LENGTH_SHORT).show();
						mMapController.animateTo(mapPoiInfo.geoPt);
					}
				}

				@Override
				public void onGetCurrentMap(Bitmap b) {
					/**
					 *  当调用过 mMapView.getCurrentMap()后，此回调会被触发
					 *  可在此保存截图至存储设备
					 */
				}

				@Override
				public void onMapAnimationFinish() {
					/**
					 *  地图完成带动画的操作（如: animationTo()）后，此回调被触发
					 */
				}
	            /**
	             * 在此处理地图载完成事件 
	             */
				@Override
				public void onMapLoadFinish() {
					Toast.makeText(Activity_WorldPlay.this, 
							       "地图加载完成", 
							       Toast.LENGTH_SHORT).show();
					
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
	    	 *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
	    	 */
	        mMapView.onResume();
	        super.onResume();
	    }
	 
	 @Override
	    protected void onDestroy() {
	    	/**
	    	 *  MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
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
	    
}
