package com.poe.lewen;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MyApplication extends Application {
	
	private static MyApplication mInstance = null;
	public static int loginCount=0;
	public static SharedPreferences mPref=null;
	
	public boolean m_bKeyRight = true;
    BMapManager mBMapManager = null;
	public static final String strKey = "7klwK4swNouixDI6ukEcFLRQ";
	 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstance = this;
		initEngineManager(this);
		loginCount++;
		mPref = PreferenceManager.getDefaultSharedPreferences(this);
		
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
            	Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
                        "key认证成功", Toast.LENGTH_LONG).show();
            }
        }
    }
}
