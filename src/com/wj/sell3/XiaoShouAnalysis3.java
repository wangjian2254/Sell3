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
import com.wj.sell3.ui.TitleBar;

public class XiaoShouAnalysis3 extends Activity {
    /** Called when the activity is first created. */
	Context con;
	TitleBar titleBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con=this;
        setContentView(R.layout.real_name_registration_result);
        
        this.titleBar = ((TitleBar)findViewById(R.id.titlebar));
	    this.titleBar.setTitle(R.string.result_title);
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
    
    
    
    
    
    public void queryCancel(View view){
    	Intent mainIntent = new Intent(this,Main.class);
    	con.startActivity(mainIntent);
    	finish();
    }
   
    public void queryXiaoShouToday(View view){
    	Convert.newtel=true;
    	Intent mainIntent = new Intent(this,XiaoShouAnalysis.class);
    	con.startActivity(mainIntent);
    	finish();
    }
    

    
    
    public void onResume(){
    	super.onResume();
    }
    public void onPause() {
        super.onPause();
    }
}