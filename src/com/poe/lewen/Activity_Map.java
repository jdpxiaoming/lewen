package com.poe.lewen;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Map extends BaseActivity implements OnClickListener{

	private LinearLayout lin_menue, lin_video, lin_yuntai, lin_map;
	private ImageView image_menue, image_video, image_yuntai, image_map;
	private TextView text_menue, text_video, text_yuntai, text_map;
	
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
        
		setContentView(R.layout.layout_map);

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		// footer init
		lin_menue = (LinearLayout) findViewById(R.id.lin_menue);
		image_menue = (ImageView) findViewById(R.id.image_menue);
		text_menue = (TextView) findViewById(R.id.text_menue);

		lin_video = (LinearLayout) findViewById(R.id.lin_video);
		image_video = (ImageView) findViewById(R.id.image_video);
		text_video = (TextView) findViewById(R.id.text_video);

		lin_yuntai = (LinearLayout) findViewById(R.id.lin_yuntai);
		image_yuntai = (ImageView) findViewById(R.id.image_yuntai);
		text_yuntai = (TextView) findViewById(R.id.text_yuntai);

		lin_map = (LinearLayout) findViewById(R.id.lin_map);
		image_map = (ImageView) findViewById(R.id.image_map);
		text_map = (TextView) findViewById(R.id.text_map);
		
		//footer select index default
		lin_map.setBackgroundResource(R.drawable.btn_bg_press	);
		image_map.setImageResource(R.drawable.icon_map_press);
		text_map.setTextColor(Color.WHITE);
		
		lin_menue.setOnClickListener(this);
		lin_video.setOnClickListener(this);
		lin_yuntai.setOnClickListener(this);
		lin_map.setOnClickListener(this);
		
		
		 mMapView = (MapView)findViewById(R.id.bmapView);
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
						Toast.makeText(Activity_Map.this,title,Toast.LENGTH_SHORT).show();
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
					Toast.makeText(Activity_Map.this, 
							       "地图加载完成", 
							       Toast.LENGTH_SHORT).show();
					
				}
			};
			mMapView.regMapViewListener(MyApplication.getInstance().mBMapManager, mMapListener);
	   
	}


	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}

	
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
	    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lin_menue:
			startActivity(new Intent(Activity_Map.this, Activity_Home.class));
			break;
		case R.id.lin_video:
			startActivity(new Intent(Activity_Map.this, Activity_Video.class));
			break;
		case R.id.lin_yuntai:
			startActivity(new Intent(Activity_Map.this, Activity_Yuntai.class));
			break;
		case R.id.lin_map:
			break;
		default:
			break;
		}
	}

}
