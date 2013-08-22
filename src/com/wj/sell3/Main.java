package com.wj.sell3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.wj.sell.adapter.AppItemAdapter;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.PluginMod;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;

public class Main extends Activity {
    /** Called when the activity is first created. */
	Context con;
	UserInfo user=null;
	GridView gridApp;
	List<PluginMod> pluginList=new ArrayList<PluginMod>();
//	public static final int SEARCHPLUGIN = Menu.FIRST + 1;
	public static final int RELOGIN = Menu.FIRST + 1;
	private Handler tmpMainHandler4;
	private TextView notice;
	
	String[] appArr={"kaoshi,实名认证"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.onError(this);
        
        setContentView(R.layout.app_list);
        con=this;
    
        setContentView(R.layout.app_list);
        user=UserInfoUtil.getCurrentUserInfo(this);
        if(user==null){
        	Intent mainIntent = new Intent(con,Login.class);
        	startActivity(mainIntent); 
        	finish(); 
        	return ;
        }
        gridApp=(GridView)findViewById(R.id.gridAppView);
        gridApp.setAdapter(new AppItemAdapter(this, pluginList));// 调用ImageAdapter.java
        gridApp.setOnItemClickListener(new OnItemClickListener() {// 监听事件
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					gotoPlugin(pluginList.get(position));
//				Toast.makeText(con, "短按事件", 1000).show();
			}
		});
        
        initAppList();
    }
    
    public void initAppList(){
    	getPluginList();
		((AppItemAdapter) gridApp.getAdapter()).notifyDataSetChanged();
    }
    
    
    public void showKaoShi(int num){
    	if(num==0){
    		notice.setVisibility(View.GONE);
    	}else{
    		notice.setVisibility(View.VISIBLE);
    		notice.setText(""+num);
    	}
    }
    
    
    
    private void getPluginList(){
    	pluginList.clear();
    	for(String appcode:appArr){
    		PluginMod p=new PluginMod();
    		p.setAppcode(appcode.split(",")[0]);
    		p.setName(appcode.split(",")[1]);
    		pluginList.add(p);
    	}
    }
    public void gotoPlugin(PluginMod p){
    	if("kaoshi".equals(p.getAppcode())){
    		Intent mainIntent = new Intent(con,XiaoShouAnalysis.class);
    		startActivity(mainIntent); 
    	}
    	else{
    		Toast.makeText(con, p.getName()+"尚未开发", 1000).show();
    	}
		
    }
    public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		populateMenu(menu);
	}

	public void populateMenu(Menu menu) {
//		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "查找插件");
		menu.add(Menu.NONE, RELOGIN, Menu.NONE, "返回登录界面");
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
		
		case RELOGIN:
			Intent mainIntent = new Intent(Main.this,Login.class);
	    	Bundle extras=new Bundle();
	    	extras.putSerializable("user", user);
	    	mainIntent.putExtras(extras);
	    	startActivity(mainIntent); 
	    	finish();
			return true;
		}
		return false;
	}
    
	public void onResume(){
    	super.onResume();
    	UmengUpdateAgent.update(this);
    	MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
