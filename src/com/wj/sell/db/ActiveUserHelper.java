package com.wj.sell.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wj.sell.util.Convert;

public class ActiveUserHelper extends SQLiteOpenHelper {
	private static final String DB_NAME="main";
	public static final String username="username";
	public static final String password="password";
	public static final String key="id";
	Context context;
	public ActiveUserHelper(Context context) {
		super(context, DB_NAME, null,Convert.DATABASE_VERSON);
		this.context=context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 创建必要表
		
		try{
			//用户信息数据库
			db.execSQL("CREATE TABLE userinfo (id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,password TEXT,writepwd TEXT,isactive TEXT,updateTime TEXT,lastUpdate TEXT,sessionid TEXT);");
			
			//用户签到记录
			onUpdate(db);
			
			db.execSQL("CREATE TABLE noticeupdate (id INTEGER PRIMARY KEY,lastUpdateTime TEXT);");
			db.execSQL("CREATE TABLE sysnotice (id INTEGER PRIMARY KEY,appcode TEXT,pluginimg TEXT,type TEXT,title TEXT,lastUpdateTime TEXT,startdate TEXT,enddate Text,isread Text);");
			db.execSQL("CREATE TABLE noticecontent (id INTEGER PRIMARY KEY,noticeid INTEGER,type TEXT,content TEXT,imgid TEXT,url TEXT,path TEXT,indexnum INTEGER,download TEXT);");
//			getApplistJson(db);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	private void onUpdate(SQLiteDatabase db){
		try{
			//签到服务列表
			db.execSQL("CREATE TABLE qiandao (id INTEGER PRIMARY KEY ,name TEXT,needTime TEXT,needGPS TEXT,needAddress TEXT,isdel TEXT, lastEventDate TEXT, eventid INTEGER, lastOfficeName TEXT,Officeid INTEGER);");
			db.execSQL("CREATE TABLE userqiandao (id INTEGER PRIMARY KEY AUTOINCREMENT,qiandaoid INTEGER,qiandaoname TEXT,officeid INTEGER,officename TEXT,time TEXT,gps TEXT,address TEXT);");
			//签到服务列表
			db.execSQL("CREATE TABLE office (id INTEGER PRIMARY KEY ,name TEXT,flag TEXT,gps TEXT,address TEXT,isdel TEXT);");
			db.execSQL("CREATE TABLE xiaoshouoffice (id INTEGER PRIMARY KEY ,daydate TEXT,officeid INTEGER,isdel TEXT);");
			db.execSQL("CREATE TABLE xiaoshouoorder (id INTEGER PRIMARY KEY AUTOINCREMENT,clientdate TEXT,clienttime TEXT,productflag TEXT,productname TEXT,typeflag TEXT,typename TEXT,giftsflag TEXT,giftsname TEXT,imie TEXT,tel TEXT,officeid INTEGER,serverid INTEGER,issubmit TEXT);");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db , int oldVersion , int newVersion){
		db.execSQL("DROP TABLE IF EXISTS qiandao");
		db.execSQL("DROP TABLE IF EXISTS userqiandao");
		db.execSQL("DROP TABLE IF EXISTS office");
		db.execSQL("DROP TABLE IF EXISTS xiaoshouoffice");
		db.execSQL("DROP TABLE IF EXISTS xiaoshouoorder");
		onUpdate(db);
	}
	
	

}
