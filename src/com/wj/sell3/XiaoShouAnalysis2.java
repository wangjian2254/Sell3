package com.wj.sell3;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.CheckSync;
import com.wj.sell.util.Convert;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;

public class XiaoShouAnalysis2 extends Activity {
    /** Called when the activity is first created. */
	Context con;
	UserInfo user=null;
	private WebView  webView;  
	
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;
	
	String tel;
	String name;
	String number;
	String address;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con=this;
        Bundle bunde = this.getIntent().getExtras();
        tel=bunde.getString("tel");
        name=bunde.getString("name");
        number=bunde.getString("number");
        address=bunde.getString("address");
        user=UserInfoUtil.getCurrentUserInfo(this);
        setContentView(R.layout.xiaoshou_web);
        
        webView  = (WebView)  findViewById(R.id.webView);  
        webView.loadUrl("http://www.baidu.com");
        
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
    						myDialog.dismiss();
    					}
    					failResult(m);
    					return;
    					
    				}
    				
    				if(msg.arg1==11){
    					String m=(String)msg.obj;
    					if(myDialog!=null&&myDialog.isShowing()){
    						myDialog.dismiss();
    					}
    					successResult();
    					return;
    					
    				}
    				
    				
    				if(msg.arg1==404){
    					String m=(String)msg.obj;
    					OAUtil.gotoReLogin(con,m);
    					return;
    				}
    				
    			}
    			
    		};
        
    }
    
    public void showOffice(){
    	
    }
    
    public void failResult(String msg){
    	TextView eDeleteW = new TextView(con);
    	LinearLayout.LayoutParams eLayoutParam = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
    			,LinearLayout.LayoutParams.WRAP_CONTENT);
    	eDeleteW.setPadding(10, 10, 10, 10);
    	eDeleteW.setText(msg);
    	eDeleteW.setTextColor(Color.WHITE);
    	eDeleteW.setTextSize(14);
    	eDeleteW.setLayoutParams(eLayoutParam);
    	
    	
    	AlertDialog.Builder builder;
    	builder = new  AlertDialog.Builder(con);
    	AlertDialog myDialog  = builder.create();  
    	myDialog.setMessage(eDeleteW.getText());
    	myDialog.setTitle("提示");
    	myDialog.setCancelable(false);
    	
    	myDialog.setButton2("返回主界面", new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			finish();
    		}
    	});
    	myDialog.setButton("修改信息",//继续签到
    			new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    		}
    	});
    	myDialog.show();
    }
    
    public void successResult(){
    	TextView eDeleteW = new TextView(con);
    	LinearLayout.LayoutParams eLayoutParam = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
    			,LinearLayout.LayoutParams.WRAP_CONTENT);
    	eDeleteW.setPadding(10, 10, 10, 10);
    	eDeleteW.setText("手机号实名认证成功。是否继续？");
    	eDeleteW.setTextColor(Color.WHITE);
    	eDeleteW.setTextSize(14);
    	eDeleteW.setLayoutParams(eLayoutParam);
    	
    	
    	AlertDialog.Builder builder;
    	builder = new  AlertDialog.Builder(con);
    	AlertDialog myDialog  = builder.create();  
    	myDialog.setMessage(eDeleteW.getText());
    	myDialog.setTitle("提示");
    	myDialog.setCancelable(false);
    	
    	myDialog.setButton2("返回主界面", new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			Intent mainIntent = new Intent(XiaoShouAnalysis2.this,Main.class);
    	    	Bundle extras=new Bundle();
    	    	mainIntent.putExtras(extras);
    	    	startActivity(mainIntent);
    			finish();
    		}
    	});
    	myDialog.setButton("提交",//继续签到
    			new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			
    			finish();
    		}
    	});
    	myDialog.show();
    }
    
    public void queryCancel(View view){
    	finish();
    }
    public void queryTongji(View view){
    	UrlSync urlSync=new CheckSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(true);
			
			urlSync.setToastContentSu("验证信息成功。");
			urlSync.setToastContentFa("验证信息失败。");
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/androidSave/");
		List<NameValuePair> param=new ArrayList<NameValuePair>();
    	param.add(new BasicNameValuePair("tel",tel));
    	param.add(new BasicNameValuePair("name",name));
    	param.add(new BasicNameValuePair("number",number));
    	param.add(new BasicNameValuePair("address",address));
    	urlSync.setPrarm(param);
		urlSync.setHandler(tmpMainHandler4);
		UrlTask utk=new UrlTask(con);
		utk.setUrlSync(urlSync);
		utk.start();
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=1;
			msg.obj="正在验证信息……";
			tmpMainHandler4.sendMessage(msg);
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