package com.wj.sell3.eve.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Handler;

//import com.asiainfo.encrypt.Encrypt;


public  class UrlTask {

	private Thread th;
	
	DefaultHttpClient client;
	HttpContext httpContext;
	HttpResponse httpResponse;
	HttpGet getMet;
	HttpPost postMet;
	private Handler mMainHandler;
	private Context context;
	long[] pattern = {100, 50, 400, 30}; // OFF/ON/OFF/ON... 
	
	public void doResult(){
		try {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void doFailureResult(){
		try {
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
	        		
	        			postMet = new HttpPost("http://channel.bj.chinamobile.com/channelApp/sys/login");
	        			List<NameValuePair> param=new ArrayList<NameValuePair>();
//	        	    	param.add(new BasicNameValuePair("u",Encrypt.classicVarLenEncrypt("bj040_01", 30)));
//	        	    	param.add(new BasicNameValuePair("p",Encrypt.classicVarLenEncrypt("time9818", 30)));
	        	    	
	        			postMet.setEntity(new UrlEncodedFormEntity(param,HTTP.UTF_8));
	        	
	        		client = new DefaultHttpClient();
	        		httpContext=new BasicHttpContext(); 
	        		client.getParams().setParameter(
	        				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
	        		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
	        		client.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
	        		
	        				httpResponse = client.execute(postMet,httpContext);
	        			
	        			int statusCode=httpResponse.getStatusLine().getStatusCode();

	        			if (statusCode == 200) {
	        				// 取出回应字串
	        				

	        				strResult = EntityUtils.toString(httpResponse.getEntity());
	        				doResult();
	        			}else{
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
	            		doFailureResult();
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
	
	
}
