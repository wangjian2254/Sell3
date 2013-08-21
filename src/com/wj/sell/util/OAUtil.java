package com.wj.sell.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wj.sell3.Login;

public class OAUtil {
	static Context gpscon;
	
	public static void gotoReLogin(Context con,String value){
		Intent mainIntent = new Intent(con,Login.class);
		Bundle extras=new Bundle();
    	extras.putString("message", value);
		mainIntent.putExtras(extras);
    	con.startActivity(mainIntent); 
	}
	
	public static void needCloseGPS(Context con,String privder){
//		if(Secure.isLocationProviderEnabled(con.getContentResolver(), privder)){
//			Secure.setLocationProviderEnabled(con.getContentResolver(), privder,false);
//		}
	}
	
	public static boolean isGPSProviderAvaliable(Context con){
		LocationManager alm = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return true;
		}
		return false;
	}
	public static boolean isWIFIProviderAvaliable(Context con){
		LocationManager alm = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
			return true;
		}
		return false;
	}
	public static boolean needOpenGPS(Context con){
		LocationManager alm = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
//		if(!Secure.isLocationProviderEnabled(con.getContentResolver(), privder)){
//			
//			Secure.setLocationProviderEnabled(con.getContentResolver(), privder,true);
//		}
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)||alm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
			return true;
		}
		gpscon=con;
		TextView eDeleteW = new TextView(con);
    	LinearLayout.LayoutParams eLayoutParam = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
    			,LinearLayout.LayoutParams.WRAP_CONTENT);
    	eDeleteW.setPadding(10, 10, 10, 10);
    	eDeleteW.setText("GPS功能尚未开启，请开启GPS功能。");
    	eDeleteW.setTextColor(Color.WHITE);
    	eDeleteW.setTextSize(18);
    	eDeleteW.setLayoutParams(eLayoutParam);
    	
    	
    	AlertDialog.Builder builder;
    	builder = new  AlertDialog.Builder(con);
    	AlertDialog myDialog  = builder.create();  
    	myDialog.setMessage(eDeleteW.getText());
    	myDialog.setTitle("提示");
    	myDialog.setCancelable(false);
    	
    	myDialog.setButton2("取消", new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			
    		}
    	});
    	myDialog.setButton("去设置",
    			new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			 
    			Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS );  
    			gpscon.startActivity(intent);  
    			gpscon=null;
    			
    		}
    	});
    	myDialog.show();
    	return false;
	}
	
	
}
