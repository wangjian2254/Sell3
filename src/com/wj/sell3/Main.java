package com.wj.sell3;

import com.wj.sell3.eve.util.UrlTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.GridView;
import android.widget.TextView;


public class Main extends Activity {
    /** Called when the activity is first created. */
	Context con;
	GridView gridApp;
//	public static final int SEARCHPLUGIN = Menu.FIRST + 1;
	public static final int RELOGIN = Menu.FIRST + 1;
	private Handler tmpMainHandler4;
	private TextView notice;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        con=this;
        UrlTask ut=new UrlTask(con);
        ut.start();
    
    }
    
    
}