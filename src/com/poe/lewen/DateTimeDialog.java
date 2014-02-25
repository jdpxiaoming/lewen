package com.poe.lewen;

import java.util.Calendar;
import java.util.Locale;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DateTimeDialog extends Activity {

	private TextView textDate;
	private WheelView month, year, day, hours, mins,seconds;
	private Button submit,btn_ok,btn_cancel;
	private Calendar calendar = Calendar.getInstance();
	private int curYear =-1 ;
	
	
	//日期选择框
		private EditText edit_star ,edit_end;
		private LinearLayout linearDate1,linearDate2;
		private boolean isStart =true;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.date_layout);
		init();
	}

	private void init() {
		edit_star	=	(EditText) findViewById(R.id.edit_start);
		edit_end	=	(EditText) findViewById(R.id.edit_end);
		btn_ok		=	(Button) findViewById(R.id.btn_okOfDateLayout);
		btn_cancel	=	(Button) findViewById(R.id.btn_cancelOfDateLayout);
		
		linearDate1	=	(LinearLayout) findViewById(R.id.linearDate1OfDateLayout);
		linearDate2	=	(LinearLayout) findViewById(R.id.linearDate2OfDateLayout);
		
		linearDate1.setVisibility(View.VISIBLE);
		linearDate2.setVisibility(View.GONE);
		
		edit_star.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				System.out.println("clicked!");
				isStart = true;
				linearDate1.setVisibility(View.GONE);
				linearDate2.setVisibility(View.VISIBLE);
				return true;
			}
		});
		
		edit_end.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				isStart = false;
				linearDate1.setVisibility(View.GONE);
				linearDate2.setVisibility(View.VISIBLE);
				return true;
			}
		});
		
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				
				if(edit_star.getText().toString().isEmpty()){
					Toast.makeText(DateTimeDialog.this, " 请选择开始时间", 300).show();
				}else if(edit_end.getText().toString().isEmpty()){
					Toast.makeText(DateTimeDialog.this, " 请选择结束时间", 300).show();
				}else{
					//filter empty input
//					Toast.makeText(DateTimeDialog.this, " 日期选择结束，回调回放接口！", 300).show();
					Intent intent = new Intent();
					intent.putExtra("startTime", edit_star.getText().toString());
					intent.putExtra("endTime", edit_end.getText().toString());
					setResult(2, intent);
					finish();
				}
				
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});	
		
		textDate = (TextView) findViewById(R.id.textOfDateLayout);
		submit = (Button) findViewById(R.id.btn_submitOfDateLayout);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("clicked!!");
				if(isStart){
					edit_star.setText(textDate.getText());
				}else{
					edit_end.setText(textDate.getText());
				}
				
				linearDate1.setVisibility(View.VISIBLE);
				linearDate2.setVisibility(View.GONE);
			}
		});
		month = (WheelView) findViewById(R.id.month);
		year = (WheelView) findViewById(R.id.year);
		day = (WheelView) findViewById(R.id.day);

		   OnWheelChangedListener listener = new OnWheelChangedListener() {
	            public void onChanged(WheelView wheel, int oldValue, int newValue) {
	                updateText();
	            }
	        };
		hours = (WheelView) findViewById(R.id.hour);
        NumericWheelAdapter hourAdapter = new NumericWheelAdapter(this, 0, 23, "%02d");
        hourAdapter.setItemResource(R.layout.wheel_text_item);
        hourAdapter.setItemTextResource(R.id.text);
        hours.setViewAdapter(hourAdapter);
        hours.addChangingListener(listener);
		
		mins = (WheelView) findViewById(R.id.mins);
		NumericWheelAdapter minAdapter = new NumericWheelAdapter(this, 0, 59, "%02d");
        minAdapter.setItemResource(R.layout.wheel_text_item);
        minAdapter.setItemTextResource(R.id.text); 
        mins.setViewAdapter(minAdapter);
        mins.addChangingListener(listener);
        mins.setCyclic(true);
        
        seconds = (WheelView) findViewById(R.id.seconds);
		NumericWheelAdapter secondsAdapter = new NumericWheelAdapter(this, 0, 59, "%02d");
		secondsAdapter.setItemResource(R.layout.wheel_text_item);
		secondsAdapter.setItemTextResource(R.id.text); 
		seconds.setViewAdapter(secondsAdapter);
		seconds.addChangingListener(listener);
		seconds.setCyclic(true);
		
        
        Calendar calendar = Calendar.getInstance(Locale.US);
        hours.setCurrentItem(calendar.get(Calendar.HOUR));
        mins.setCurrentItem(calendar.get(Calendar.MINUTE));
        
        setDateAdapter();
	}
	
	private void updateText(){
		
		String lastest = "";
		if(curYear<0)
			curYear = calendar.get(Calendar.YEAR);
		 System.out.println("curYear: "+curYear +"currentItem:"+year.getCurrentItem());
		String _year = curYear-30+year.getCurrentItem()+"";
		
		String _month=month.getCurrentItem()<9?"0"+(month.getCurrentItem()+1):month.getCurrentItem()+1+"";
		
		String _day =(day.getCurrentItem()<9?"0"+(day.getCurrentItem()+1):day.getCurrentItem()+1)+"";
		
		String _hour=hours.getCurrentItem()<9?"0"+hours.getCurrentItem():hours.getCurrentItem()+"";
		
		String _mins=mins.getCurrentItem()<9?"0"+mins.getCurrentItem():mins.getCurrentItem()+"";
		
		String _seconds = seconds.getCurrentItem()<9?"0"+seconds.getCurrentItem():seconds.getCurrentItem()+"";
		
		lastest=_year+"-"+_month+"-"+_day+" "+_hour+":"+_mins+":"+_seconds;
		textDate.setText(lastest);
	}

	private void setDateAdapter() {
		
		Calendar calendar = Calendar.getInstance();
		
		 OnWheelChangedListener listener = new OnWheelChangedListener() {
	            public void onChanged(WheelView wheel, int oldValue, int newValue) {
	                updateDays(year, month, day);
	                updateText();
	            }
	        };

	        // month
	        int curMonth = calendar.get(Calendar.MONTH);
//	        month.setViewAdapter(new DateNumericAdapter(this, 1, 12,curMonth));
//	        month.setCurrentItem(curMonth);
//	        month.addChangingListener(listener);
	        NumericWheelAdapter monthAdapter = new NumericWheelAdapter(this, 1, 12, "%02d");
	        monthAdapter.setItemResource(R.layout.wheel_text_item);
	        monthAdapter.setItemTextResource(R.id.text);
	        month.setViewAdapter(monthAdapter);
	        month.setCurrentItem(curMonth);
	        month.addChangingListener(listener);
	    
	        // year
	        int curYear = calendar.get(Calendar.YEAR);
//	        year.setViewAdapter(new DateNumericAdapter(this, curYear-30, curYear + 30, curYear));
//	        year.setCurrentItem(curYear);
//	        year.addChangingListener(listener);
	        NumericWheelAdapter yearAdapter = new NumericWheelAdapter(this, curYear-30, curYear+30, "%04d");
	        yearAdapter.setItemResource(R.layout.wheel_text_item);
	        yearAdapter.setItemTextResource(R.id.text);
	        year.setViewAdapter(yearAdapter);
	        year.setCurrentItem(30);
	        year.addChangingListener(listener);
	        
	        
	        //day
	        updateDays(year, month, day);
	        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
	}
	
	/**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
//        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
//        calendar.set(Calendar.MONTH, month.getCurrentItem());
        
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateText();
            }
        };
        
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        NumericWheelAdapter dayAdapter = new NumericWheelAdapter(this, 1, maxDays, "%02d");
        dayAdapter.setItemResource(R.layout.wheel_text_item);
        dayAdapter.setItemTextResource(R.id.text);
        day.setViewAdapter(dayAdapter);
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1-1);
        day.addChangingListener(listener);
        
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }
    
    
    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
    
    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
}
