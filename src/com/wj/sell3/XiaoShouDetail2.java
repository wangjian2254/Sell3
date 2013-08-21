package com.wj.sell3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;

public class XiaoShouDetail2 extends Activity  {

	TextView name;
	TextView officenameText;
	TextView productnameText;
	TextView imieText;
	TextView producttypeText;
	EditText telEdit;
	Button btnSubmit;
	Button btnReSubmit;
	LinearLayout giftlayout;
	ImageView imgview;
	
	XiaoShouDetail2 con;
	
	
	
	
	private Button   mStartBtn;
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;

	private UserInfo user=null;
	private int officeid=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.onCreate(savedInstanceState);
        con=this;
        setContentView(R.layout.xiaoshou_detail2);
		Bundle bunde = this.getIntent().getExtras();
//		officeid=bunde.getInt("officeid");
		user=UserInfoUtil.getCurrentUserInfo(this);
		name = (TextView) findViewById(R.id.notice_title);
		mStartBtn =(Button)findViewById(R.id.setoffice);
		btnSubmit=(Button)findViewById(R.id.btnSubmit);
		btnReSubmit=(Button)findViewById(R.id.btnReSubmit);
		mStartBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				return;
				
			}
		});
		name.setText("销售记录");
		
		
		 officenameText=(TextView)findViewById(R.id.officename);
		 productnameText=(TextView)findViewById(R.id.product);
		 imieText=(TextView)findViewById(R.id.imie);
		 producttypeText=(TextView)findViewById(R.id.producttype);
		 telEdit=(EditText)findViewById(R.id.tel);
		 giftlayout=(LinearLayout)findViewById(R.id.giftlayout);
		
//		 randomProductOrder();
		 
		 setProductOrder();
		
			tmpMainHandler4 = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				try{
					
				
				// // 接收子线程的消息
				String m=(String)msg.obj;
				if(msg.arg1==1){
					if(myDialog!=null){
						myDialog.dismiss();
					}
					myDialog = ProgressDialog.show  
					    	(
					    			con,
					    		"提示",
					    		m
					    	);
					return;
				}
				if(msg.arg1<10){
					myDialog.setMessage(m);
					return;
					
				}
				if(msg.arg1==10){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.setMessage(m);
					}
					btnReSubmit.setVisibility(View.VISIBLE);
					
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
				if(msg.arg1==12){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.setMessage(m);
					}
					btnSubmit.setEnabled(false);
					
					new Thread() {
						public void run() {
							try {
								sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message msg=tmpMainHandler4.obtainMessage();
							msg.arg1=13;
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
					return;
					
				}
				if(msg.arg1==13){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.dismiss();
					}
					myDialog=null;
					finish();
					return;
					
				}
				
				
				if(msg.arg1==404){
					OAUtil.gotoReLogin(con,m);
					finish();
					return;
				}
				
			}catch(Exception e){
				Log.e("xiaohsou2", "销售error");
			}
				
			}
			
		};
		
	}   
	
	
	
	public void savePhone(View view){
		finish();
	}
	
	public void setProductOrder(){
		
	}
	
	
	
	

	
	
	
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenu.ContextMenuInfo menuInfo) {
//		populateMenu(menu);
//	}
//
//	public void populateMenu(Menu menu) {
////		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "查找插件");
//		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "刷新厅台信息");
////		menu.add(Menu.NONE, APPLIST, Menu.NONE, "应用列表");
////		menu.add(Menu.NONE, SYSTEM, Menu.NONE, "系统消息");
////		menu.add(Menu.NONE, REFASH, Menu.NONE, "刷新");
////		menu.add(Menu.NONE, ALLREFASH, Menu.NONE, "反馈意见");
//	}
//
//	public boolean onCreateOptionsMenu(Menu menu) {
//		populateMenu(menu);
//		return (super.onCreateOptionsMenu(menu));
//	}
//
//	public boolean onOptionsItemSelected(MenuItem item) {
//		return (applyMenuChoice(item) || super.onOptionsItemSelected(item));
//	}
//
//	public boolean onContextItemSelected(MenuItem item) {
//		return (applyMenuChoice(item) || super.onContextItemSelected(item));
//	}
//
//
//	public boolean applyMenuChoice(MenuItem item) {
//		switch (item.getItemId()) {
//		
//		case SEARCHPLUGIN:
//			
//			return true;
//		}
//		return false;
//	}

	public void onResume(){
    	super.onResume();
    	
    	MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}