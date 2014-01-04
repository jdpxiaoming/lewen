package com.poe.lewen.util;

import android.content.Context;
import android.widget.Toast;

public class Tool {
	public static void showMsg(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

}
