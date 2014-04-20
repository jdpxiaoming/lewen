package com.poe.lewen;


import com.poe.lewen.adapter.adapter4RecordVideoList;
import com.poe.lewen.bean.history_video;
import com.poe.lewen.vlc.VideoPlayerActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 录像回放
 * @author Administrator
 *
 */
public class Activity_Record_List extends Activity {

	private ListView listview;
	private TextView textTitle;
	private Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_recharge_list);

		init();
	}

	public void init() {

		// set title
		textTitle = (TextView) findViewById(R.id.textTitleOfToperBarSave);
		textTitle.setText(getString(R.string.str_record_list));

		back = (Button) findViewById(R.id.leftButtonOfToperBarSave);
		listview = (ListView) findViewById(R.id.listviewOfRechargeList);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String rtmp = arg0.getAdapter().getItem(arg2).toString();
				if (rtmp.length() > 4) {
					VideoPlayerActivity.start(Activity_Record_List.this, rtmp,
							false);
				}
			}
		});

		setadapter();
	}

	private void setadapter() {
		String[] datasets = new String[Activity_Video.HList.size()];

		int i = 0;
		for (history_video h : Activity_Video.HList) {
			datasets[i] = h.getPlayaddr();
			i++;
		}

		BaseAdapter adapter = new adapter4RecordVideoList(datasets,
				Activity_Record_List.this.getApplicationContext());
		listview.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
