package com.wj.sell3;

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
	
	String[] appArr={"qiandao,签到","wendang,文档平台","kaoshi,考试","xiaoshou,销售产品","qiandaotongji,签到统计","kaoshitongji,考试统计","xiaoshoutongji,销售统计"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        con=this;
    
    }
    
}