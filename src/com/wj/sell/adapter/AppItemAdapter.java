package com.wj.sell.adapter;

import java.util.List;

import com.wj.sell.controls.AppItem;
import com.wj.sell.db.models.PluginMod;
import com.wj.sell3.Main;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AppItemAdapter extends BaseAdapter {
	private Main mContext;
	List<PluginMod> imgarrlist;
//	List<String> imgarrlistcode;
	
//	Pattern pattern = Pattern.compile(Convert.imgreg);
//	Matcher matcher = null;

	public AppItemAdapter(Main c,List<PluginMod> itemContent) {
		mContext = c;
		imgarrlist=itemContent;
//		imgarrlistcode=itemCode;
//		contentmap=contentmap;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgarrlist.size();
	}
	
	public PluginMod getImgArr(int position){
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

		AppItem appItem;
//		matcher = pattern.matcher(imgarrlist.get(position));
//		String imgstr;
////		String imglibstr;
//		String imgcodestr=null;
//		String imgappidstr=null;
//		int imgnum=0;
//		int imgnum2=0;
//		while (matcher.find()) {
//			imgstr = matcher.group();
//			imgnum = imgstr.indexOf("/");
//			imgnum2 = imgstr.indexOf("/", imgnum + 1);
////			imglibstr = imgstr.substring(2, imgnum);
//			imgcodestr = imgstr.substring(imgnum + 1,
//					imgnum2);
//			imgappidstr = imgstr.substring(imgnum2 + 1,
//					imgstr.length() - 2);
//		}
//		Bitmap bitmap=null;
//		if(imgcodestr!=null){
//			bitmap= Imgtools.getImage(imgcodestr+imgappidstr);
//		}else{
//			bitmap=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon);
//		}
		 
		if (convertView == null) {
			appItem = new AppItem(mContext);
			
		} else {
			appItem = (AppItem) convertView;
		}
//		appItem.setImg(bitmap);
		appItem.setTitle(imgarrlist.get(position).getName());
		//appItem.setImg(imgarrlist.get(position).getAppcode());
		appItem.setImg("function_1_icon");
		
		
//		imageview.setImageResource();
//		setViewImage(imageview,stra[0]+stra[1]);
		return appItem;
	}
	

}
