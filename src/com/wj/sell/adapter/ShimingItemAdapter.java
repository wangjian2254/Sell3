package com.wj.sell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wj.sell.controls.AppItem;
import com.wj.sell.db.models.PluginMod;
import com.wj.sell.db.models.Shiming;
import com.wj.sell3.Main;
import com.wj.sell3.R;

import java.util.List;

public class ShimingItemAdapter extends BaseAdapter {
	private Context mContext;
	List<Shiming> imgarrlist;
//	List<String> imgarrlistcode;
	private LayoutInflater inflater;

//	Pattern pattern = Pattern.compile(Convert.imgreg);
//	Matcher matcher = null;

	public ShimingItemAdapter(Context c, List<Shiming> itemContent) {
		mContext = c;
		imgarrlist=itemContent;
		inflater = LayoutInflater.from(c);
//		imgarrlistcode=itemCode;
//		contentmap=contentmap;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgarrlist.size();
	}
	
	public Shiming getImgArr(int position){
		return imgarrlist.get(position);
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Shiming appItem = imgarrlist.get(position);
		if(convertView==null){
			convertView = inflater.inflate(R.layout.shiming_item,null);
		}
		TextView phone_number = (TextView) convertView.findViewById(R.id.phone_number);
		TextView user_name = (TextView) convertView.findViewById(R.id.user_name);
		TextView real_status = (TextView) convertView.findViewById(R.id.real_status);
		phone_number.setText(appItem.getPhone_number());
		user_name.setText(appItem.getName());
		switch (appItem.getSuccess()){
			case 0:
				real_status.setText("等待实名审核");
				break;
			case 1:
				real_status.setText("实名失败");
				break;
			case 2:
				real_status.setText("实名成功");
				break;
		}


		return convertView;
	}
	

}
