package com.wj.sell3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;
import com.wj.sell.util.XiaShuSync;

public class XiaoShouAnalysis extends Activity {
    /** Called when the activity is first created. */
	Context con;
	UserInfo user=null;
	EditText datepicker;
	EditText datepicker2;
	LinearLayout user_list;
	LinearLayout office_list;
	
	List<UserInfo> userlist=new ArrayList<UserInfo>();
	String date1=null;
	String date2=null;
	DatePickerDialog.OnDateSetListener dateListener;
	DatePickerDialog.OnDateSetListener dateListener2;
	DatePickerDialog dialog;
	
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con=this;
        Bundle bunde = this.getIntent().getExtras();
        user=UserInfoUtil.getCurrentUserInfo(this);
        setContentView(R.layout.xiaoshou_searchwindow);
        

        datepicker = (EditText) findViewById(R.id.datepicker);  
        datepicker2 = (EditText) findViewById(R.id.datepicker2);
        user_list=(LinearLayout)findViewById(R.id.user_list);
        office_list=(LinearLayout)findViewById(R.id.office_list);
        
        datepicker.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
            @Override  
            public void onFocusChange(View v, boolean hasFocus) {  

                if(hasFocus) {
		        // 此处为得到焦点时的处理内容
                	setDate1(null);
		        } else {
		
		        // 此处为失去焦点时的处理内容
		
		        }
            }
        });
        datepicker2.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
        	@Override  
        	public void onFocusChange(View v, boolean hasFocus) {  
        		
        		if(hasFocus) {
        			// 此处为得到焦点时的处理内容
        			setDate2(null);
        		} else {
        			
        			// 此处为失去焦点时的处理内容
        			
        		}
        	}
        });
        dateListener =  
            new DatePickerDialog.OnDateSetListener() { 
                @Override 
                public void onDateSet(DatePicker datePicker,  
                        int year, int month, int dayOfMonth) { 
                	datepicker.setText(getDate(datePicker));
                	date1=getDate(datePicker);
                } 
            }; 
        dateListener2 =  
            	new DatePickerDialog.OnDateSetListener() { 
            	@Override 
            	public void onDateSet(DatePicker datePicker,  
            			int year, int month, int dayOfMonth) { 
            		datepicker2.setText(getDate(datePicker));
            		date2=getDate(datePicker);
            	} 
            }; 
            tmpMainHandler4 = new Handler() {
    			
    			@Override
    			public void handleMessage(Message msg) {
    				
    				if(msg.arg1==1){
    					if(myDialog!=null){
    						myDialog.dismiss();
    					}
    					String m=(String)msg.obj;
    					myDialog = ProgressDialog.show  
    					    	(
    					    			con,
    					    		"提示",
    					    		m
    					    	);
    					return;
    				}
    				if(msg.arg1<10){
    					String m=(String)msg.obj;
    					if(myDialog!=null&&myDialog.isShowing()){
    						myDialog.setMessage(m);
    					}
    					return;
    					
    				}
    				if(msg.arg1==10){
    					String m=(String)msg.obj;
    					if(myDialog!=null&&myDialog.isShowing()){
    						myDialog.setMessage(m);
    					}
    					
    					
    					new Thread() {
    	            		public void run() {
    	            			try {
    								sleep(2000);
    							} catch (InterruptedException e) {
    								// TODO Auto-generated catch block
    								e.printStackTrace();
    							}
    	            			Message msg=tmpMainHandler4.obtainMessage();
    	            			msg.arg1=11;
    	            			tmpMainHandler4.sendMessage(msg);
    	            		}
    	            	}.start();
    					return;
    					
    				}
    				
    				if(msg.arg1==11){
    					if(myDialog!=null&&myDialog.isShowing()){
    						myDialog.dismiss();
    					}
    					myDialog=null;
    					userlist.clear();
    					List<UserInfo> l=(ArrayList<UserInfo>)msg.obj;
    					userlist.addAll(l);
    					showUserInfo();
    					return;
    					
    				}
    				
    				
    				if(msg.arg1==404){
    					String m=(String)msg.obj;
    					OAUtil.gotoReLogin(con,m);
    					return;
    				}
    				
    			}
    			
    		};
        initDatepick();
        showOffice();
        initUserList(false);
        
    }
    
    public void showOffice(){
    }
    
    public void initUserList(boolean toast){
    	UrlSync urlSync=new XiaShuSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(toast);
		if(toast){
			
			urlSync.setToastContentSu("刷新下属列表成功。");
			urlSync.setToastContentFa("刷新下属列表失败。");
		}
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/userListClient/");
		
		urlSync.setHandler(tmpMainHandler4);
		UrlTask utk=new UrlTask(con);
		utk.setUrlSync(urlSync);
		utk.start();
		if(toast){
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=1;
			msg.obj="正在刷新下属列表……";
			tmpMainHandler4.sendMessage(msg);
		}
    }
    
    public void showUserInfo(){
    	user_list.removeAllViews();
    	CheckBox cb=null;
    	for(UserInfo o:userlist){
    		cb=new CheckBox(con);
    		cb.setChecked(false);
    		cb.setText(o.getUsername());
    		cb.setTextAppearance(con, R.style.xiaoshou2);
    		user_list.addView(cb);
    	}
    }
    
    
    public void setDate1(View view){
    	if(dialog!=null&&dialog.isShowing()){
    		dialog.dismiss();
    		dialog=null;
    	}
    	dialog = new DatePickerDialog(this, 
                dateListener, 
                Integer.parseInt(date1.split("-")[0]) , 
                Integer.parseInt(date1.split("-")[1])-1, 
                Integer.parseInt(date1.split("-")[2]));
    	dialog.show();
    }
    public void setDate2(View view){
    	if(dialog!=null&&dialog.isShowing()){
    		dialog.dismiss();
    		dialog=null;
    	}
    	dialog = new DatePickerDialog(this, 
    			dateListener2, 
    			Integer.parseInt(date2.split("-")[0]) , 
                Integer.parseInt(date2.split("-")[1])-1, 
                Integer.parseInt(date2.split("-")[2]));
    	dialog.show();
    }
    
    public void initDatepick(){
    	datepicker.setText(Convert.format1.format(new Date()).substring(0, 10));
		date1=Convert.format1.format(new Date()).substring(0, 10);
		datepicker2.setText(Convert.format1.format(new Date()).substring(0, 10));
		date2=Convert.format1.format(new Date()).substring(0, 10);
    }
   
    public void queryTongji(View view){
    }
    
    public String getDate(DatePicker dp){
    	String  year=String.valueOf(dp.getYear());
    	String  month=String.valueOf(dp.getMonth()+1);
    	String  day=String.valueOf(dp.getDayOfMonth());
    	if(month.length()==1){
    		month="0"+month;
    	}
    	if(day.length()==1){
    		day="0"+day;
    	}
    	return year+"-"+month+"-"+day;
    }
    
    public void queryXiaoShouToday(View view){
    }
    

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		populateMenu(menu);
	}

	public void populateMenu(Menu menu) {
//		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "查找插件");
		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "刷新下属列表");
//		menu.add(Menu.NONE, APPLIST, Menu.NONE, "应用列表");
//		menu.add(Menu.NONE, SYSTEM, Menu.NONE, "系统消息");
//		menu.add(Menu.NONE, REFASH, Menu.NONE, "刷新");
//		menu.add(Menu.NONE, ALLREFASH, Menu.NONE, "反馈意见");
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		populateMenu(menu);
		return (super.onCreateOptionsMenu(menu));
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return (applyMenuChoice(item) || super.onOptionsItemSelected(item));
	}

	public boolean onContextItemSelected(MenuItem item) {
		return (applyMenuChoice(item) || super.onContextItemSelected(item));
	}


	public boolean applyMenuChoice(MenuItem item) {
		switch (item.getItemId()) {
		
		case SEARCHPLUGIN:
			initUserList(true);
			
			return true;
		}
		return false;
	}
    
    
    public void onResume(){
    	super.onResume();
    }
    public void onPause() {
        super.onPause();
    }
}