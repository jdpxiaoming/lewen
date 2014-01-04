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

import android.R.integer;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	private boolean netSDKIsInit = false; // NetSDK是否初始化成功标志
	public static AV_HANDLE log_handle = null; // 登陆句柄
	private AV_IN_Login refInParam = null; // 登陆输入参数
	private AV_OUT_Login refOutParam = null; // 登陆输出参数
	private int mChannelCount=0;	//连接设备的通道数
	public static ArrayList<String> mChannelList = new ArrayList<String>();
	/**
	 * 用户在列表模式中选择的摄像头 index default:0
	 */
	public static int selectChannel = 0;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstance = this;
		initEngineManager(this);
		loginCount++;
		mPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		netSDKIsInit = AVNetSDK.AV_Startup(mInstance.getPackageName());	
		
		new loginTask().execute();
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
    
    
	// 没有网络的提示
	public  void NoNetWorkTips() {
		Toast.makeText(mInstance, "您的网络不可用,请设置！", Toast.LENGTH_LONG).show();
	}
	
	// 剪贴板复制提示
		public  void throwTips(String str) {
			
			Toast.makeText(mInstance, str, 2200).show();
			
		}
		
		class loginTask extends AsyncTask<Void, integer, String>{


			
			@Override
			protected void onPreExecute() {
				
				//构造登陆输入参数
				refInParam = new AV_IN_Login();
				refInParam.strDevIP = Constant.str_login_ip;
				refInParam.nDevPort = Constant.login_port;
				refInParam.strUsername = "admin";
				refInParam.strPassword = "123456";
				refInParam.bReconnect = false;
				refInParam.connStatusListener = new ConnectStatusListener() {

					@Override
					public int onConnectStatus(AV_HANDLE arg0, boolean arg1,
							String arg2, int arg3, Object arg4) {
						// TODO Auto-generated method stub
						return 0;
					}
				};
				//构造登陆输出参数
				refOutParam = new AV_OUT_Login();
			}

			@Override
			protected String doInBackground(Void... params) {
				if (netSDKIsInit) { // 如果NetSDK初始化成功才登陆
					log_handle = AVNetSDK.AV_Login(refInParam, refOutParam);// 登录失败返回null，调用AV_GetLastError来获取具体的错误信息
					Log.d("jhe", refOutParam.strDeviceType+":"+refOutParam.nAnalogChnNum+":"+refOutParam.nChannelCount+":"+refOutParam.nDigitalChnNum+":"+refOutParam.nProtocolVersion);
					mChannelCount=refOutParam.nChannelCount;
					for(int i =0;i<mChannelCount;i++)
					{
						String channel = "通道 " +String.format("%02d", i+1);
						mChannelList.add(i,channel);
					}
					if (log_handle != null){
//						startVideo();
					}
					
					return "success";
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if(null!=result){
					throwTips("登录成功~@！");
				}
			}	
		}
}
