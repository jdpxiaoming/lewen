package com.poe.lewen;

import java.util.ArrayList;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.mm.android.avnetsdk.AVNetSDK;
import com.mm.android.avnetsdk.param.AV_HANDLE;
import com.mm.android.avnetsdk.param.AV_IN_Login;
import com.mm.android.avnetsdk.param.AV_OUT_Login;
import com.mm.android.avnetsdk.param.ConnectStatusListener;
import com.poe.lewen.bean.Constant;
import com.poe.lewen.bean.ServiceInfo;
import com.poe.lewen.bean.channelOnLine;
import com.poe.lewen.bean.rsp_login;
import com.poe.lewen.util.TcpUtil;
import android.R.integer;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class MyApplication extends Application {
	
	private static MyApplication mInstance = null;
	public static int loginCount=0;
	public static SharedPreferences mPref=null;
	
	public boolean m_bKeyRight = true;
    BMapManager mBMapManager = null;
	public static final String strKey = "7klwK4swNouixDI6ukEcFLRQ";
//	-----、、
	private  boolean netSDKIsInit = false; // NetSDK是否初始化成功标志
	public static AV_HANDLE log_handle = null; // 登陆句柄
	private AV_IN_Login refInParam = null; // 登陆输入参数
	private AV_OUT_Login refOutParam = null; // 登陆输出参数
//	private IAV_CaptureDataListener
	private int mChannelCount=0;	//连接设备的通道数
	public static ArrayList<String> mChannelList = new ArrayList<String>();
	public static String ip_dahua ="60.18.152.38";
	public static int prot_dahua=37779;
	public static String username="admin";
	public static String password	="admin";
	
	public static channelOnLine cOnline ;
	
	public static rsp_login rsp_login;
	/**
	 * 用户在列表模式中选择的摄像头 index default:0
	 */
	public static int selectChannel = 0;
	
	//packet tool
	public static TcpUtil packet = new TcpUtil();
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstance = this;
		initEngineManager(this);
		loginCount++;
		mPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		netSDKIsInit = AVNetSDK.AV_Startup(mInstance.getPackageName());	
		
		defaultSDKParam();
		new loginTask().execute();
	}
	
	//***********
	//重复获取大华SDK通道信息
	public void reLogin(loaded4login ll){
//		this.username = username;
//		this.password = password;
		new loginTask().execute(ll);
	}
	
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
	/**
	 * 向配置文件里面存储参数
	 * @param tag 标记
	 * @param data 存储u的参数
	 */
	public static void pushPreferenceData(String tag,String data){
		Editor editor = mPref.edit();
		editor.putString(tag, data);
		editor.commit();
	}
	
	public static MyApplication getInstance() {
		return mInstance;
	}
	
	/**
	 * 取出参数
	 * @param tag 标记
	 * @return
	 */
	public static String getPreferenceData(String tag){
		return mPref.getString(tag, null);
	}
	
	public void setHost(ServiceInfo service){
		
		MyApplication.pushPreferenceData("hostname",  service.getServiceName()) ;
		MyApplication.pushPreferenceData("host",  service.getServiceUrl()) ;
		MyApplication.pushPreferenceData("port",  service.getPort()) ;
	}
	public ServiceInfo getServiceInfo(){
		ServiceInfo service = new ServiceInfo();
		service.setServiceName(MyApplication.getPreferenceData("hostname"));
		service.setServiceUrl(MyApplication.getPreferenceData("host"));
		service.setPort(MyApplication.getPreferenceData("port"));
		
		return service;
		
	}
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
        	//非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
                        "请在 MyApplication.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();
                MyApplication.getInstance().m_bKeyRight = false;
            }
            else{
            	MyApplication.getInstance().m_bKeyRight = true;
//            	Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
//                        "key认证成功", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    /*
     * 回复默认演示地址
     */
    public void defaultSDKParam(){
		MyApplication.ip_dahua = "60.18.152.38";
		MyApplication.prot_dahua	= 37779;
		MyApplication.username	=	"admin";
		MyApplication.password	=	"admin";
		MyApplication.selectChannel  = 0;
		//初始化的时候 给一个默认的ip和端口号
		if(MyApplication.getPreferenceData("host")==null){
			MyApplication.pushPreferenceData("host", Constant.str_login_ip);
			MyApplication.pushPreferenceData("port", Constant.login_port+"");
		}
		
    }
	// 没有网络的提示
	public  void NoNetWorkTips() {
		Toast.makeText(mInstance, "您的网络不可用,请设置！", Toast.LENGTH_LONG).show();
	}
	
	// 剪贴板复制提示
		public  void throwTips(String str) {
			
			Toast.makeText(mInstance, str, 2200).show();
			
		}
		
		// 剪贴板复制提示
				public  void throwTipsLong(String str) {
					
					Toast.makeText(mInstance, str, 5000).show();
					
				}
				
		class loginTask extends AsyncTask<loaded4login, integer, String>{

			private loaded4login login_interface = null;
			
			@Override
			protected void onPreExecute() {
				
				//构造登陆输入参数
				refInParam = new AV_IN_Login();
				refInParam.strDevIP = ip_dahua;
				refInParam.nDevPort = prot_dahua;
				refInParam.strUsername = username;
				refInParam.strPassword = password;
				refInParam.bReconnect = false;
				refInParam.connStatusListener = new ConnectStatusListener() {

					@Override
					public int onConnectStatus(AV_HANDLE arg0, boolean arg1,
							String arg2, int arg3, Object arg4) {
						// TODO Auto-generated method stub
						return 0;
					}
				};
				
//				refInParam.
				//构造登陆输出参数
				refOutParam = new AV_OUT_Login();
			}

			@Override
			protected String doInBackground(loaded4login... params) {
				
				if(params!=null&&params.length>0)
					login_interface = params[0];
				
				if (netSDKIsInit) { // 如果NetSDK初始化成功才登陆
					log_handle = AVNetSDK.AV_Login(refInParam, refOutParam);// 登录失败返回null，调用AV_GetLastError来获取具体的错误信息
					Log.d("jhe", refOutParam.strDeviceType+":"+refOutParam.nAnalogChnNum+":"+refOutParam.nChannelCount+":"+refOutParam.nDigitalChnNum+":"+refOutParam.nProtocolVersion);
					mChannelCount=refOutParam.nChannelCount;
					for(int i =0;i<mChannelCount;i++)
					{
						String channel = "通道 " +String.format("%02d", i+1);
						mChannelList.add(i,channel);
					}
					if (log_handle == null){
//						startVideo();
						return null;
					}
					return "success";
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if(null!=result){
					throwTips("通道连接成功~@！");
					
				}else{
					throwTips("通道连接失败~@！");
				}
				
				if(null!=login_interface)
					login_interface.done();
			}
		}
		
	 interface loaded4login{
		 void done();
	 }
	 
	   
	     /**
	      * 在状态栏显示通知
	      */
	     private void showNotification(){
	         // 创建一个NotificationManager的引用  
	         NotificationManager notificationManager = (NotificationManager)   
	             this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);  
	           
	         // 定义Notification的各种属性  
	         Notification notification =new Notification(R.drawable.audio_circle,  
	                 "您有新的語音!", System.currentTimeMillis());
	         //FLAG_AUTO_CANCEL   该通知能被状态栏的清除按钮给清除掉
	         //FLAG_NO_CLEAR      该通知不能被状态栏的清除按钮给清除掉
	         //FLAG_ONGOING_EVENT 通知放置在正在运行
	         //FLAG_INSISTENT     是否一直进行，比如音乐一直播放，知道用户响应
	         notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中  
	         notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用  
	         notification.flags |= Notification.FLAG_SHOW_LIGHTS;  
	         //DEFAULT_ALL     使用所有默认值，比如声音，震动，闪屏等等
	         //DEFAULT_LIGHTS  使用默认闪光提示
	         //DEFAULT_SOUNDS  使用默认提示声音
	         //DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission android:name="android.permission.VIBRATE" />权限
	         notification.defaults = Notification.DEFAULT_LIGHTS;
	         //叠加效果常量
	         //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
	         notification.ledARGB = Color.BLUE;  
	         notification.ledOnMS =5000; //闪光时间，毫秒
	           
	         // 设置通知的事件消息  
	         CharSequence contentTitle ="督导系统标题"; // 通知栏标题  
	         CharSequence contentText ="督导系统内容"; // 通知栏内容  
	         Intent notificationIntent =new Intent(mInstance, HelloBubblesActivity.class); // 点击该通知后要跳转的Activity  
	         PendingIntent contentItent = PendingIntent.getActivity(this, 0, notificationIntent, 0);  
	         notification.setLatestEventInfo(this, contentTitle, contentText, contentItent);  
	         // 把Notification传递给NotificationManager  
	         notificationManager.notify(0, notification);  
	     }
	     
	     //删除通知   
	     private void clearNotification(){
	         // 启动后删除之前我们定义的通知  
	         NotificationManager notificationManager = (NotificationManager) this
	                 .getSystemService(NOTIFICATION_SERVICE);  
	         notificationManager.cancel(0); 
	   
	  
	     }

}
