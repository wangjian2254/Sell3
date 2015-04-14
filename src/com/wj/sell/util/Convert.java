package com.wj.sell.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.os.Environment;
import com.wj.sell.db.models.UserInfo;


public class Convert {

	public static final String hosturl="http://sell4.liulv.net/";
//	public static final String hosturl="http://ec2-54-65-241-68.ap-northeast-1.compute.amazonaws.com/";

//	public static final String hosturl="http://192.168.1.110:8000"; 
	public static final String domain=hosturl.substring(7); 
	
//	public static final String hosturl="http://www.mmggoo.com"; 
	public static final String DATABASE_NAME="oadb"; 
	public static final int DATABASE_VERSON=7; 
	public static final String APP_VERSON="0.1"; 
	public static final String BaiduKey="&key=5c044ddbd374ffe3ffb66036e383f3e7"; 
	
	public static String picPath   = Environment.getExternalStorageDirectory()+"/OA/Picture/";
	public static String infPath   = Environment.getExternalStorageDirectory()+"/OA/";

	public static final String testNum="&verson="+APP_VERSON;
	
	public static String uname=null;
	public static String upwd=null;
	
	
	public static DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	public static boolean hasImage=true;
	public static boolean newtel=false;
	public static String imgreg = "\\[\\*[A-Za-z]*/[-_0-9A-Za-z]*/[_A-Za-z0-9]*\\*\\]";

	public static UserInfo currentUser;
	

}
