package com.wj.sell.db;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserInfoUtil {
	
	private static SQLiteDatabase readdb;
	private static SQLiteDatabase wdb;

	
	
	public static UserInfo getCurrentUserInfo(Context context){
		UserInfo user=null;
		synchronized (String.class) {
			 try{
				 Cursor cur=getReaddb(DBhelper.getDBHelper(context)).rawQuery("select * from userinfo order by  isactive desc", null);
				 cur.moveToFirst();
				 while(!cur.isAfterLast()){
					 user=new UserInfo();
					 user.setId(cur.getInt(0));
					 user.setUsername(cur.getString(1));
					 user.setPassword(cur.getString(2));
					 user.setIsactive(cur.getString(3));
					 user.setCookies(cur.getString(7));
					 Convert.uname=user.getUsername();
					 Convert.upwd=user.getPassword();
					 break;
				 }
				 cur.close();
			 }catch(Exception e){
				 return null;
			 }
		 }
		
		return user;
	}
	
	public static boolean updateUserInfo(UserInfo user,Context context){
		boolean flag=false;
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		ContentValues userapp = new ContentValues();
		userapp.put("password", user.getPassword());
		userapp.put("isactive", sdf.format(now));
		userapp.put("lastUpdate", sdf.format(now));
		userapp.put("sessionid",user.getCookies());
		user.setIsactive(sdf.format(now));
		String[] w={user.getUsername()};
		synchronized (String.class) {
			try{
				Cursor cur=getReaddb(DBhelper.getDBHelper(context)).rawQuery("select * from userinfo where username=?", w);
				int num=cur.getCount();
				cur.close();
				if(num>0){
					getWdb(DBhelper.getDBHelper(context)).update("userinfo",  userapp,"username=?",w);
				}else{
					userapp.put("writepwd", "1");
					userapp.put("username", user.getUsername());
					getWdb(DBhelper.getDBHelper(context)).insert("userinfo", null, userapp);
				}
				delOtherUser(user.getUsername(),context);
				flag=true;
			}catch(Exception e){
				return false;
			}
		}
		return flag;
	}
	
	public static void delOtherUser(String username,Context context){
			try{
				String[] w={username};
				getReaddb(DBhelper.getDBHelper(context)).delete("userinfo", "username <>?", w);
				
			}catch(Exception e){
			}
	}
	
	public static boolean addUserInfo(UserInfo user,Context context){
		boolean flag=false;
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		ContentValues userapp = new ContentValues();
		userapp.put("username", user.getUsername());
		userapp.put("password", user.getPassword());
		userapp.put("writepwd", user.getWritepwd());
		userapp.put("isactive", sdf.format(now));
		userapp.put("lastUpdate", sdf.format(now));
		userapp.put("sessionid",user.getCookies());
		 synchronized (String.class) {
			 try{
				
				getWdb(DBhelper.getDBHelper(context)).insert("userinfo", null, userapp);
				flag=true;
			 }catch(Exception e){
				 flag=false;
			 }
        }
		
		return flag;
	}
	
	private static SQLiteDatabase getReaddb(ActiveUserHelper auh) {
		if (readdb == null || !readdb.isOpen()) {
			readdb = auh.getReadableDatabase();
		}
		return readdb;
	}
	private static SQLiteDatabase getWdb(ActiveUserHelper auh) {
		if (wdb == null || !wdb.isOpen()) {
			wdb = auh.getWritableDatabase();
		}
		return wdb;
	}
}
