package com.poe.lewen.adapter;

import java.util.ArrayList;

import com.poe.lewen.MyApplication;
import com.poe.lewen.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adapter4Save extends BaseAdapter {

	private ArrayList<String> datasets;
	private LayoutInflater lin;
	
	public adapter4Save(Context c) {
		super();
		this.datasets = MyApplication.mChannelList;
		lin = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return datasets.size();
	}

	@Override
	public Object getItem(int position) {
		return datasets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView ==null||convertView.findViewById(R.id.textOfSaveItem)==null){
			convertView = lin.inflate(R.layout.item_list_save, null);
		}
		
		TextView tv 	  = (TextView) convertView.findViewById(R.id.textOfSaveItem);
		
		tv.setText(datasets.get(position));
		
		return convertView;
	}

}
