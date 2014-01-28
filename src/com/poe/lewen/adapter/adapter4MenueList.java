package com.poe.lewen.adapter;

import com.poe.lewen.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adapter4MenueList extends BaseAdapter {

	private String [] datasets;
	private LayoutInflater lin;
	private int[] icons={R.drawable.icon_menue_login,R.drawable.icon_menue_yanshi,
//			R.drawable.icon_menue_boke,
			R.drawable.icon_menue_save,R.drawable.icon_menue_help,R.drawable.icon_menue_login};
	
	public adapter4MenueList(String[] datasets,Context c) {
		super();
		this.datasets = datasets;
		lin = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return datasets.length;
	}

	@Override
	public Object getItem(int position) {
		return datasets[position];
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
		
		ImageView img =(ImageView) convertView.findViewById(R.id.imageOfMenueItem);
		TextView tv 	  = (TextView) convertView.findViewById(R.id.textOfMenueItem);
		
		img.setImageResource(icons[position]);
		tv.setText(datasets[position]);
		
		return convertView;
	}

}
