package com.poe.lewen;

import java.util.ArrayList;
import java.util.List;
import com.poe.lewen.view.NumberDotImageView;
import com.poe.lewen.view.SlowFlipGallery;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

public class HelpShowImageActivity extends Activity {
	public static final String HELP_EXHIBITION_VIEW = "exhibition";
	public static final String HELP_VIEW_KEY = "help_view_key";
	private int flag;
	private GestureDetector gestureDetector;
	private int[] images;
	private List<Bitmap> mBmpList;
	private NumberDotImageView mDotImageView;
	private SlowFlipGallery mGallery;
	private ImageAdapter mImageAdapter;
	private int postion = 0;
	private ImageButton btn;
	public static boolean onDestroy = true;// 标示次activity是否处于结束状态、false正在结束...

	public HelpShowImageActivity() {
		int[] arrayOfInt = new int[5];
		arrayOfInt[0] = R.drawable.android_help1;
		arrayOfInt[1] = R.drawable.android_help2;
		arrayOfInt[2] = R.drawable.android_help3;
		arrayOfInt[3] = R.drawable.android_help4;
		arrayOfInt[4] = R.drawable.android_help5;
		this.images = arrayOfInt;
		this.flag = 6;
	}

	// 给 gallery 放数据。
	private void fillGallery(List<Bitmap> paramList) {
		this.mImageAdapter = new ImageAdapter(this, paramList);
		this.mGallery.setAdapter(this.mImageAdapter);
		final int i = paramList.size();

		mGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
				HelpShowImageActivity.this.mDotImageView.refresh(i, paramInt);

				// if (paramInt < HelpShowImageActivity.this.arrays.length)
				// HelpShowImageActivity.this.mTitle.setText(HelpShowImageActivity.this.arrays[paramInt]);

				if (paramInt == -1 + 5) {
					// 最后一张页面 系那是跳转的button 并给坚挺事件
					btn.setBackgroundResource(R.drawable.android_help_button2);

				} else {
					btn.setBackgroundResource(R.drawable.android_help_button1);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		this.mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
				if (paramInt == -1 + HelpShowImageActivity.this.images.length) {

					// HelpShowImageActivity.this.jumpActivity();
					// Toast.makeText(HelpShowImageActivity.this, "over",
					// Toast.LENGTH_LONG).show();
					jumpActivity();
				}

			}
		});

		this.mGallery.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
				return HelpShowImageActivity.this.gestureDetector.onTouchEvent(paramMotionEvent);
			}
		});
	}

	private void jumpActivity() {
		startActivity(new Intent(this, Activity_Home.class));
		finish();
	}

	/**
	 * 获取屏幕分辨率
	 * 
	 * @return
	 */
	public int getScreenScale() {
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);

		int i;

		switch (localDisplayMetrics.widthPixels) {
		default:
			i = 2;
		case 240:
			i = 1;
		case 320:
			i = 1;
		case 480:
			i = 2;
		}
		return i;
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.help);

		onDestroy = false;
		this.gestureDetector = new GestureDetector(new DefaultGestureDetector());
		this.mDotImageView = ((NumberDotImageView) findViewById(R.id.dot_imageview));
		this.mGallery = ((SlowFlipGallery) findViewById(R.id.gallery));

		btn = (ImageButton) findViewById(R.id.btnOfHelpShowImageItem);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jumpActivity();
			}
		});

		// 填充数据
		this.mBmpList = new ArrayList();

		for (int i = 0; i < this.images.length; i++) {
			Drawable localDrawable = getResources().getDrawable(this.images[i]);

			this.mBmpList.add(((BitmapDrawable) localDrawable).getBitmap());

			localDrawable = null;
		}

		fillGallery(this.mBmpList);

		if (getScreenScale() == 3) {
			// this.mTitle.setPadding(0, 80, 0, 0);
			// this.mdotlayout.setPadding(0, 20, 0, 20);
		}
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if ((paramInt == 4) && (paramKeyEvent.getRepeatCount() == 0))
			// Toast.makeText(HelpShowImageActivity.this, "over",
			// Toast.LENGTH_LONG).show();
			jumpActivity();
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

	class DefaultGestureDetector extends GestureDetector.SimpleOnGestureListener {
		int mFlag;

		DefaultGestureDetector() {
		}

		public boolean onDown(MotionEvent paramMotionEvent) {
			this.mFlag = HelpShowImageActivity.this.flag;
			return false;
		}

		public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
			Log.e("mGallery.getSelectedItemId()", HelpShowImageActivity.this.mGallery.getSelectedItemId() + "");

			Log.e("mGallery.getSelected-----position", postion + "");

			if (postion == 4) {
				if ((paramMotionEvent1.getX() - paramMotionEvent2.getX() > 20.0F) && (Math.abs(paramFloat1) > 20.0F)) {

					Log.e("mGallery.getSelected----end", "over now goto next activity");
				}
			}
			postion = (int) HelpShowImageActivity.this.mGallery.getSelectedItemId();
			return false;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		onDestroy = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		onDestroy = false;
	}

	class ImageAdapter extends BaseAdapter {
		private final Context mContext;
		private final List<Bitmap> mImagesByteList;

		public ImageAdapter(Context mContext, List<Bitmap> arg2) {
			this.mContext = mContext;
			this.mImagesByteList = arg2;
		}

		public int getCount() {
			return this.mImagesByteList.size();
		}

		public Object getItem(int paramInt) {
			return this.mImagesByteList.get(paramInt);
		}

		public long getItemId(int paramInt) {
			return paramInt;
		}

		public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {

			if (null == paramView || paramView.findViewById(R.id.imageOfHelpShowImageItem) == null) {

				paramView = LayoutInflater.from(mContext).inflate(R.layout.item_help_show_image, null);
			}

			ImageView iv = (ImageView) paramView.findViewById(R.id.imageOfHelpShowImageItem);
			iv.setImageBitmap(this.mImagesByteList.get(paramInt));
			return paramView;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		onDestroy = true;
		super.onDestroy();
		for (int i = 0; i < mBmpList.size(); i++) {
			if (null != mBmpList.get(i)) {
				mBmpList.get(i).recycle();
			}
		}
	}
}
