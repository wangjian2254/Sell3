package com.wj.sell.util;

import org.json.JSONObject;

import android.os.Message;

public class BaiduMapSync extends UrlSync {

	public void doResult() throws Exception {
		// TODO Auto-generated method stub
		JSONObject map=new JSONObject(getResult());
		if(!"ok".equalsIgnoreCase(map.getString("status"))){
			doFailureResult();
			return;
		}
		if(getHandler()!=null){
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=map;
			hmsg.arg1=1;
			getHandler().sendMessage(hmsg);
			setHandler(null);
		}
	}
	
	public void doFailureResult()throws Exception{
		if(getHandler()!=null){
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=getToastContentFa();
			hmsg.arg1=2;
			getHandler().sendMessage(hmsg);
			setHandler(null);
		}
	}
}
