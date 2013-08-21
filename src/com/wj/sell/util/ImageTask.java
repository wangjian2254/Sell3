package com.wj.sell.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;


public class ImageTask {

	private Thread th;
	private String uri;
	private String pathName;
	private ImageView imageView;
	
	private Handler mMainHandler;
	private Context context;
	public  void handleResult(String result){
		
	}
	public ImageTask(Context con)
	{
		this.context=con;
		mMainHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				// // 接收子线程的消息
				if(msg.arg1==1){
					String picname=(String)msg.obj;
					Bitmap bm=BitmapFactory.decodeFile(picname);
					try {
						imageView.setImageBitmap(bm);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}else{
					Toast.makeText(context, getUri()+" 下载错误。", 2000).show();
				}
				
			}

		};
		 th = new Thread()
	        {
	        	public void run()
	        	{
	        		String httpUrl = getUri();
	            	//获得的数据
	            	
	            	try
	            	{
	            		if (!Environment.getExternalStorageState().equals(
	            				Environment.MEDIA_MOUNTED)) {
	            			return;
	            		}
	            		URL url = new URL(httpUrl);
	            		//构建一个HttpClient对象
	            		HttpURLConnection  conn =(HttpURLConnection)url.openConnection();
	            		conn.setConnectTimeout(5000);

	            		if (conn.getResponseCode() == 200) {
	            			File dirFile = new File(Convert.picPath);
		            		if (!dirFile.exists()) {
		            			dirFile.mkdir();
		            		}
		            		
	            			File saveFile = new File(dirFile, pathName);
	            			FileOutputStream outStream = new FileOutputStream(saveFile);
	            			
		            		InputStream inStream = conn.getInputStream();
//		            		Bitmap bm=BitmapFactory.decodeStream(inStream);
//		            		inStream.close();
		            		
//		            		byte[] imgData=new byte[conn.getContentLength()];
//		            		inStream.read(imgData);
		            	    byte[] temp=new byte[512];  
		            	    int readLen=0;  
		            	    while((readLen=inStream.read(temp))>0){  
		            	    	if(readLen!=512){
		            	    		byte[] t=new byte[readLen];
		            	    		for (int i=0;i<readLen;i++) {
										t[i]=temp[i];
									}
		            	    		outStream.write(t);
		            	    	}else{
		            	    		outStream.write(temp);
		            	    	}
		            	    }
		            		
		            			outStream.close();
		            			inStream.close();

		            		
	            			Message msg=mMainHandler.obtainMessage();
							msg.obj=Convert.picPath+pathName;
							msg.arg1=1;
							mMainHandler.sendMessage(msg);
	            		}else{
	            			Message msg=mMainHandler.obtainMessage();
							msg.obj=Convert.picPath+pathName;
							msg.arg1=2;
							mMainHandler.sendMessage(msg);
	            		}
	             	}
	            	catch (Exception e) 
	    			{
	            		Message msg=mMainHandler.obtainMessage();
						msg.obj=Convert.picPath+pathName;
						msg.arg1=2;
						mMainHandler.sendMessage(msg);
	            		e.printStackTrace();
	    			}
	        	}
	        };
	}
	public void  start()
	{
		th.start();
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public ImageView getImageView() {
		return imageView;
	}
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	
	public String getPathName() {
		return pathName;
	}
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
	
}
