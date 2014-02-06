package com.poe.lewen.util;

import com.poe.lewen.Activity_Video.VideoLoad;
import com.poe.lewen.MyApplication;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

/**
 * 多点触碰 图片的缩放和移动
 * @author caixm
 * 
 */
public class MulitPointTouchListener implements OnGestureListener {

	VideoLoad vl;

//	public MulitPointTouchListener(VideoLoad videoLoad) {
//		super();
//		this.vl = videoLoad;
//	}

//	public MulitPointTouchListener(VideoLoad videoLoad) {
//		// TODO Auto-generated constructor stub
//		this.vl = videoLoad;
//	}

	public MulitPointTouchListener(VideoLoad videoLoad) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
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
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
