package com.poe.lewen.adapter;

import java.util.List;
import com.poe.lewen.MyApplication;
import com.poe.lewen.R;
import com.poe.lewen.bean.ServiceInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SystemUrlSetAdapter extends BaseAdapter {

	
	private List<ServiceInfo> searchList ;//满足搜索条件的集合
	
	private LayoutInflater lin;
	
	
	public SystemUrlSetAdapter(Context c, List<ServiceInfo> serviceList) {
		super();
		this.searchList= serviceList;
		lin =LayoutInflater.from(c);
	}


	public void add(ServiceInfo service){
		searchList.add(service);
		this.notifyDataSetChanged();
	}
	/**
	 * 
	 * 根据点击的位置获取对应的 数据对象
	 * 
	 * @param arg0
	 * @return
	 */
	public ServiceInfo getSelectedObject(int arg0){
		ServiceInfo result =null;
		
		if(null!=searchList&&searchList.size()>0){
			
			result = searchList.get(arg0);
			
		}
		
		return result;
		
	}
	
	/**
	 * 点击 listview时候的服务器设置被触发、并刷新界面
	 * @param arg0
	 */
	public void setSelectdObject(int arg0){
		
//		MyApplication.pushPreferenceData("hostname",  searchList.get(arg0).getServiceName()) ;
//		MyApplication.pushPreferenceData("host",  searchList.get(arg0).getServiceUrl()) ;
//		MyApplication.pushPreferenceData("port",  searchList.get(arg0).getPort()) ;
		MyApplication.getInstance().setHost(searchList.get(arg0));
		
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return searchList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return searchList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return searchList.indexOf(getItem(arg0));
	}

	@Override
	public View getView(int arg0, View contentView, ViewGroup arg2) {
		
		if(null==contentView||contentView.findViewById(R.id.txt1OfSystemUrlSetItem)==null){
			
			contentView =lin.inflate(R.layout.item_system_urlset_list, null);
		}
		
		TextView txt1 = (TextView) contentView.findViewById(R.id.txt1OfSystemUrlSetItem);
		
		TextView txt2 = (TextView) contentView.findViewById(R.id.txt2OfSystemUrlSetItem);
		
		ServiceInfo si =searchList.get(arg0);
		
		if(si!=null){
			
			txt1.setText(si.getServiceName());
			
		}
		
		if(si.getServiceUrl().equals(MyApplication.getPreferenceData("host"))){
			txt2.setVisibility(View.VISIBLE);
		}else{
			txt2.setVisibility(View.GONE);
		}
		
		return contentView;
	}

}
