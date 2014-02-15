package com.poe.lewen.adapter;

import java.util.List;

import com.poe.lewen.R;
import com.poe.lewen.bean.rsp_recharge_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adapter4Record extends BaseAdapter {

	private List<rsp_recharge_record> datasets;
	private LayoutInflater lin;
	
	public adapter4Record(List<rsp_recharge_record> datasets,Context c) {
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
			convertView = lin.inflate(R.layout.item_recharge_record, null);
		}
		
		TextView tv1 	  = (TextView) convertView.findViewById(R.id.textNoOfRechargeRecordItem);
		TextView tv2 	  = (TextView) convertView.findViewById(R.id.textDateOfRechargeRecordItem);
		TextView tv3 	  = (TextView) convertView.findViewById(R.id.textMoneyOfRechargeRecordItem);
		TextView tv4 	  = (TextView) convertView.findViewById(R.id.textBalanceOfRechargeRecordItem);
		
		rsp_recharge_record rsp=datasets.get(position);
		

		tv1.setText(rsp.getBuyId());
		tv2.setText(rsp.getBeginTime());
//		tv1.setText(rsp.get)
		
		return convertView;
	}
	
}
