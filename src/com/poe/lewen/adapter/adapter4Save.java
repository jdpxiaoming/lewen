package com.poe.lewen.adapter;

import com.poe.lewen.Activity_Save;
import com.poe.lewen.R;
import com.poe.lewen.bean.channel;
import com.poe.lewen.bean.channelOnLine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class adapter4Save extends BaseAdapter {

	private LayoutInflater lin;
	
	public adapter4Save(Context c) {
		super();
		lin = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return Activity_Save.list_channel.size();
	}

	@Override
	public Object getItem(int position) {
		return Activity_Save.list_channel.get(position);
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
		channelOnLine c =Activity_Save.list_channel.get(position);
		
		tv.setText(c.getChannelName());
		
		return convertView;
	}
	
}
