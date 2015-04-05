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
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.CheckSync;
import com.wj.sell.util.CheckSync2;
import com.wj.sell.util.Convert;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;
import com.wj.sell3.ui.AlertDialogCustom;
import com.wj.sell3.ui.TitleBar;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogOKListener;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogCancelListener;

public class XiaoShouAnalysis extends Activity {
    /** Called when the activity is first created. */
	Context con;
	UserInfo user=null;
	EditText tel;
	EditText name;
	EditText number;
	EditText address;
	AlertDialogCustom localAlertDialogCustom;
	
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;
	public static final int APPLIST = Menu.FIRST + 2;
	TitleBar titleBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con=this;
        Bundle bunde = this.getIntent().getExtras();
        user=SellApplication.getUserInfoIdByUid(SellApplication.getUidCurrent());
        setContentView(R.layout.real_name_registration_form);
        

        name = (EditText) findViewById(R.id.name);  
        tel = (EditText) findViewById(R.id.tel);  
        number = (EditText) findViewById(R.id.number);  
        address = (EditText) findViewById(R.id.address);
        
        
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
    					successResult(m);
    					return;
    					
    				}
    				
    				
    				if(msg.arg1==404){
    					String m=(String)msg.obj;
    					OAUtil.gotoReLogin(con,m);
    					return;
    				}
    				
    			}
    			
    		};
    		this.titleBar = ((TitleBar)findViewById(R.id.titlebar));
    	    this.titleBar.setTitle(R.string.shiming_luru);
    	    this.titleBar.setBackListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				finish();
    			}
    		});
    	    this.titleBar.setUp();
    }
    
    public void showOffice(){
    	
    }

	/**
	 * 拍照 识别函数
	 * @param view
	 */
	public void paiZhao(View view){

	}

	/**
	 * 蓝牙 硬件
	 * @param view
	 */
	public void blueTooth(View view){

	}
    
    public void failResult(String msg){
    	localAlertDialogCustom= new AlertDialogCustom(this);
		localAlertDialogCustom.show();
		localAlertDialogCustom.setMessage(msg);
		localAlertDialogCustom.setOnOKListener("返回主界面",new AlertDialogOKListener() {
			
			@Override
			public void onOKClick() {
				// TODO Auto-generated method stub
				localAlertDialogCustom.dismiss();
				finish();
    			
			}
		});
		localAlertDialogCustom.setOnCancelListener("修改信息",new AlertDialogCancelListener() {
			
			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
				localAlertDialogCustom.dismiss();
			}
		});
		
    	
    }
    
    public void successResult(String photo){
    	Intent mainIntent = new Intent(XiaoShouAnalysis.this,XiaoShouAnalysisConfirm.class);
    	Bundle extras=new Bundle();
    	extras.putString("tel", tel.getText().toString().trim());
    	extras.putString("name", name.getText().toString().trim());
    	extras.putString("number", number.getText().toString().trim());
    	extras.putString("address", address.getText().toString().trim());
    	extras.putString("photo", photo);
    	mainIntent.putExtras(extras);
    	con.startActivity(mainIntent); 
//    	
//    	localAlertDialogCustom= new AlertDialogCustom(this);
//		localAlertDialogCustom.show();
//		localAlertDialogCustom.setMessage("信息正确。是否通过？");
//		localAlertDialogCustom.setOnOKListener("通过",new AlertDialogOKListener() {
//			
//			@Override
//			public void onOKClick() {
//				// TODO Auto-generated method stub
//				localAlertDialogCustom.dismiss();
//    			Intent mainIntent = new Intent(XiaoShouAnalysis.this,XiaoShouAnalysis2.class);
//    	    	Bundle extras=new Bundle();
//    	    	extras.putString("tel", tel.getText().toString().trim());
//    	    	extras.putString("name", name.getText().toString().trim());
//    	    	extras.putString("number", number.getText().toString().trim());
//    	    	extras.putString("address", address.getText().toString().trim());
//    	    	mainIntent.putExtras(extras);
//    	    	con.startActivity(mainIntent); 
//			}
//		});
//		localAlertDialogCustom.setOnCancelListener("不通过",new AlertDialogCancelListener() {
//			
//			@Override
//			public void onCancelClick() {
//				// TODO Auto-generated method stub
//				localAlertDialogCustom.dismiss();
//			}
//		});
		
    }
    
    
    public void queryTongji(View view){
    	UrlSync urlSync=new CheckSync2();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(true);
			
			urlSync.setToastContentSu("验证信息成功。");
			urlSync.setToastContentFa("验证信息失败。");
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/androidCheck/");
		List<NameValuePair> param=new ArrayList<NameValuePair>();
    	param.add(new BasicNameValuePair("tel",tel.getText().toString().trim()));
    	param.add(new BasicNameValuePair("name",name.getText().toString().trim()));
    	param.add(new BasicNameValuePair("number",number.getText().toString().trim()));
    	param.add(new BasicNameValuePair("address",address.getText().toString().trim()));
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
		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "返回主界面");
		menu.add(Menu.NONE, APPLIST, Menu.NONE, "清空");
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
			finish();
			return true;
		case APPLIST:
			finish();
			return true;
		}
		return false;
	}
    
    
    public void onResume(){
    	super.onResume();
    	if(Convert.newtel){
    		Convert.newtel=false;
    		tel.setText("");
    		name.setText("");
    		number.setText("");
    		address.setText("");
    	}
    }
    public void onPause() {
        super.onPause();
    }
}