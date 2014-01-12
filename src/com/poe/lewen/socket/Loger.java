package com.poe.lewen.socket;

import android.util.Log;

public class Loger {

	/**
	 * Log.i("poe", message);
	 * @param message
	 */
	public static void i(String message){
		Log.i("poe", message);
	}
	
	public static void e(String message ){
		Log.e("poe", message);
	}
	
	public static void e(String message,Exception e){
		e.printStackTrace();
		Log.e("poe", message);
	}
	
	
	public static void w(String message ){
		Log.w("poe", message);
	}
}
