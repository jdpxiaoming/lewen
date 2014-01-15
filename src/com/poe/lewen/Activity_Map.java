package com.poe.lewen;

import java.util.List;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mm.android.avnetsdk.AVNetSDK;
import com.mm.android.avnetsdk.param.AV_HANDLE;
import com.mm.android.avnetsdk.param.AV_IN_RealPlay;
import com.mm.android.avnetsdk.param.AV_MediaInfo;
import com.mm.android.avnetsdk.param.AV_OUT_RealPlay;
import com.mm.android.avnetsdk.param.AV_PlayPosInfo;
import com.mm.android.avnetsdk.param.AV_Time;
import com.mm.android.avnetsdk.param.IAV_DataListener;
import com.mm.android.avnetsdk.param.IAV_NetWorkListener;
import com.mm.android.avnetsdk.param.IAV_PlayerEventListener;
import com.mm.android.avnetsdk.param.RecordInfo;
import com.mm.android.avplaysdk.render.BasicGLSurfaceView;
import com.poe.lewen.Activity_Video.playTask;
import com.poe.lewen.MyApplication.loaded4login;
import com.poe.lewen.util.Tool;

import android.R.integer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class Activity_Map extends BaseActivity {

	//---------------------------通道一
		private AV_IN_RealPlay playINParam = null; // 实时监视输入参数
		private AV_OUT_RealPlay playOutParam = null; // 实时监视输出参数
		private BasicGLSurfaceView bsView = null; // 播放的视图
		private AV_HANDLE realPlay = null; // 实时监测句柄
		
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
		bsView =	 (BasicGLSurfaceView) findViewById(R.id.screenOfMap);
		//footer select index default
		lin_map.setBackgroundResource(R.drawable.btn_bg_press	);
		image_map.setImageResource(R.drawable.icon_map_press);
		text_map.setTextColor(Color.WHITE);
		
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
//		   bsView.uninit();
	       super.onPause();		
	}
	
	 @Override
	    protected void onResume() {
	    	/**
	    	 *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
	    	 */
	        mMapView.onResume();
	        
	        if(bsView.getRenderer()==null){
	    		bsView.init(Activity_Map.this);
	    	}
	      //start video defalut channel is 0
	    	if(MyApplication.log_handle!=null){
	    		new playTask().execute();
	    	}else{
	    		MyApplication.getInstance().reLogin(new loaded4login() {
	    			
	    			@Override
	    			public void done() {
	    				// TODO Auto-generated method stub
	    				new playTask().execute();
	    			}
	    		});
	    	}
	    		
	        super.onResume();
	    }
	 
	 @Override
	    protected void onDestroy() {
	    	/**
	    	 *  MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
	    	 */
	        mMapView.destroy();
	        
	    	if (realPlay != null) {
	    		AVNetSDK.AV_StopRealPlay(realPlay); // 停止实时监视
	    		realPlay = null;
	    	}
	    	bsView.uninit();	//反
	    	
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
	    class playTask extends AsyncTask<Void, integer, String>{
			
			@Override
			protected void onPreExecute() {
				playINParam = new AV_IN_RealPlay();
				playINParam.nChannelID = 7; // 测试零号通道
				playINParam.nSubType = 1;
				playINParam.playView = bsView;
				playINParam.dataListener = new IAV_DataListener() {

					@Override
					public int onData(AV_HANDLE arg0, byte[] arg1, int arg2, int arg3,
							AV_MediaInfo arg4, Object arg5) {
						// TODO Auto-generated method stub
						return 0;
					}
				};
				
				playINParam.netWorkListener = new IAV_NetWorkListener() {

					@Override
					public int onConnectStatus(AV_HANDLE arg0, boolean arg1,
							AV_HANDLE arg2, Object arg3) {
						// TODO Auto-generated method stub
						return 0;
					}
				};
				playINParam.playerEventListener = new IAV_PlayerEventListener() {

					@Override
					public void onResolutionChange(AV_HANDLE arg0, int arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onRecordInfo(Object arg0, AV_Time arg1, AV_Time arg2,
							List<RecordInfo> arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public int onPlayPos(AV_HANDLE arg0, AV_PlayPosInfo arg1,
							Object arg2) {
						// TODO Auto-generated method stub
						return 0;
					}

					@Override
					public void onNotSupportedEncode(AV_HANDLE arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFrameRateChange(AV_HANDLE arg0, int arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFrameLost(AV_HANDLE arg0) {
						// TODO Auto-generated method stub

					}
				};
				//构造实时监视输出参数
				playOutParam = new AV_OUT_RealPlay();
			}

			@Override
			protected String doInBackground(Void... params) {
				if (MyApplication.log_handle != null) // 登陆成功才能播放
					realPlay = AVNetSDK.AV_RealPlay(MyApplication.log_handle, playINParam,	playOutParam);
				else {
					return "请先登陆！";
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if(null!=result){
					Tool.showMsg(MyApplication.getInstance().getApplicationContext(), result);
				}
			}
		}
}
