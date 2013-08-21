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
	        		String strResult=null;
	        		try{
	        		getMet = null;
	        		postMet = null;
	        		if (urlSync.isGet()) {
	        			getMet = new HttpGet(urlSync.getAllUri());
	        		} else {
	        			postMet = new HttpPost(urlSync.getAllUri());
	        			if(urlSync.getPrarm()!=null){
	        				postMet.setEntity(new UrlEncodedFormEntity(urlSync.getPrarm(),HTTP.UTF_8));
	        			}
	        		}
	        		Log.e("request", urlSync.getAllUri());
	        		client = new DefaultHttpClient();
	        		httpContext=new BasicHttpContext(); 
//	        		CookieStore cookieStore=new BasicCookieStore();
//	        		CookieStore cookieStore=client.getCookieStore();
	        		if(urlSync.isGet()){
    					getMet.setHeader("Cookie", urlSync.getUser().getCookies());
    					getMet.setHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
    				}else{
    					postMet.setHeader("Cookie", urlSync.getUser().getCookies());
    					postMet.setHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
    				}
	        		
	        		
//	        		client.setCookieStore(cookieStore);
	        		client.getParams().setParameter(
	        				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
	        		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
	        		client.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
	        		
	        			if (urlSync.isGet()) {
	        				httpResponse = client.execute(getMet,httpContext);

	        			} else {
	        				httpResponse = client.execute(postMet,httpContext);
	        			}
	        			
	        			if(!"outwebsite".equals(urlSync.getResultType())&&!((HttpHost)httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST)).toURI().equals(Convert.hosturl)){
	        				doFailureResult("网络不通，请检查网络。");
	        				return;
	        			}
	        			int statusCode=httpResponse.getStatusLine().getStatusCode();

	        			if (statusCode == 200) {
	        				// 取出回应字串
	        				

	        				strResult = EntityUtils.toString(httpResponse.getEntity());
	        				urlSync.setResult(strResult);
	        				doResult();
	        			}else{
	        				strResult = EntityUtils.toString(httpResponse.getEntity());
	        				doFailureResult();
	        			}
//	        		String httpUrl = setUrl();
//	        		String strResult = "";
//	            	//获得的数据
//	            	HttpGet httpRequest = new HttpGet(httpUrl);
//	            	try
//	            	{
//	            		//构建一个HttpClient对象
//	            		HttpClient httpclient = new DefaultHttpClient();
//	            		//请求HttpClient，取得HttpResponse
//	            		HttpResponse httpResponse = httpclient.execute(httpRequest);
//	            		//请求成功
//	            		if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
//	            		{
//	            			//取得返回的字符串
//	            			strResult = EntityUtils.toString(httpResponse.getEntity());
//	            		}
//	             	}
	        		}
	            	catch (Exception e) 
	    			{
	            		doFailureResult("网络不通，请检查网络。");
	    			}
	            	
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
