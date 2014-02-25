package com.poe.lewen.adapter;

import java.util.List;
import com.poe.lewen.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class adapter4UserList extends BaseAdapter {

	private List<String> datasets;
	private LayoutInflater lin;
	public adapter4UserList(List<String> datasets,Context c) {
		super();
		this.datasets = datasets;
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
		if(convertView ==null||convertView.findViewById(R.id.imageOfMenueItem)==null){
			convertView = lin.inflate(R.layout.item_list_menue, null);
		}
		
		TextView tv 	  = (TextView) convertView.findViewById(R.id.textOfMenueItem);
		tv.setTextSize(16);
		tv.setText(datasets.get(position));
		
		return convertView;
	}

}
