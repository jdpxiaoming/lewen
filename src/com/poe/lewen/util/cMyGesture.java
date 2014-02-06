package com.poe.lewen.util;

import com.poe.lewen.MyApplication;
import com.poe.lewen.Activity_Video.VideoLoad;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class cMyGesture extends SimpleOnGestureListener {

	VideoLoad vl;
	
	
	public cMyGesture(VideoLoad vl) {
		super();
		this.vl = vl;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		System.out.println("onDown!");
		return super.onDown(e);
	}

	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

		float x = e2.getY()-e1.getY();
		System.out.println("e2.getY:"+e2.getY());
		System.out.println("e1.getY:"+e1.getY());
		if (Math.abs(x) > 10) {
			if (x > 0) {
				MyApplication.selectChannel = MyApplication.selectChannel + 1;
			} else {
				MyApplication.selectChannel = MyApplication.selectChannel - 1;
			}

			// 数组边界验证
			if (MyApplication.selectChannel < 0) {
				MyApplication.selectChannel = 0;
			}
			if (MyApplication.selectChannel > MyApplication.mChannelList.size()) {
				MyApplication.selectChannel = MyApplication.selectChannel % (MyApplication.mChannelList.size() == 0 ? 1 : MyApplication.mChannelList.size());
			}
			vl.loadVideo();
		}
	    		  
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		
		float x = e2.getY()-e1.getY();
		System.out.println("e2.getY:"+e2.getY());
		System.out.println("e1.getY:"+e1.getY());
		if (Math.abs(x) > 10) {
			if (x > 0) {
				MyApplication.selectChannel = MyApplication.selectChannel + 1;
			} else {
				MyApplication.selectChannel = MyApplication.selectChannel - 1;
			}

			// 数组边界验证
			if (MyApplication.selectChannel < 0) {
				MyApplication.selectChannel = 0;
			}
			if (MyApplication.selectChannel > MyApplication.mChannelList.size()) {
				MyApplication.selectChannel = MyApplication.selectChannel % (MyApplication.mChannelList.size() == 0 ? 1 : MyApplication.mChannelList.size());
			}
			vl.loadVideo();
		}
		
		return false;
	}

}
