package com.wj.sell.util;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;


public  class UrlTask {

	private Thread th;
	private UrlSync urlSync=null;
	
	DefaultHttpClient client;
	HttpContext httpContext;
	HttpResponse httpResponse;
	HttpGet getMet;
	HttpPost postMet;
	private Context context;
	long[] pattern = {100, 50, 400, 30}; // OFF/ON/OFF/ON... 
	
	public void doResult(){
		try {
			this.getUrlSync().doResult();
		} catch (Exception e) {
			doFailureResult();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void doFailureResult(){
		try {
			this.getUrlSync().doFailureResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void doFailureResult(String msg){
		try {
			this.getUrlSync().setToastContentFa(msg);
			this.getUrlSync().doFailureResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public UrlTask(Context con)
	{
		this.context=con;
		 th = new Thread()
	        {
	        	public void run()
	        	{

	            	
	        	}
	        };
	}
	public void  start()
	{
		th.start();
	}
	
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public UrlSync getUrlSync() {
		return urlSync;
	}
	public void setUrlSync(UrlSync urlSync) {
		this.urlSync = urlSync;
	}
	
}
