package com.poe.lewen;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public abstract class BaseActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
    public abstract void init();
    public abstract void refresh(Object ... param);
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		init();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
//			LayoutInflater li=LayoutInflater.from(this.getParent());
//			View exitV=li.inflate(R.layout.exitdialog, null);
//			AlertDialog.Builder ab=new AlertDialog.Builder(this.getParent());
//			ab.setView(exitV);
//			ab.setPositiveButton(R.string.confirm, new OnClickListener(){
//				public void onClick(DialogInterface arg0, int arg1) {
//					
////					exitApp(this.getParent());
//					
//				}
//			});
//			
//			ab.setNegativeButton(R.string.cancel, null);
//			ab.show();
//			
//			
//			NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);//得到系统服务
//			manager.cancel(11496);//取消通知
//			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

