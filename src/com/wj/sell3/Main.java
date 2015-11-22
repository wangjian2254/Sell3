package com.wj.sell3;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.wj.sell.adapter.AppItemAdapter;
import com.wj.sell.db.models.PluginMod;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.HttpCallResultBack;
import com.wj.sell.util.HttpCallResultBackCurrentUser;
import com.wj.sell.util.HttpResult;
import com.wj.sell3.ui.ChannelApplication;
import com.wj.sell3.ui.TitleBar;

public class Main extends Activity {
	/** Called when the activity is first created. */
	Context con;
	GridView gridApp;
	TitleBar titleBar;
	List<PluginMod> pluginList = new ArrayList<PluginMod>();
	// public static final int SEARCHPLUGIN = Menu.FIRST + 1;
	public static final int RELOGIN = Menu.FIRST + 1;
	public static final int CHAT = Menu.FIRST + 2;
	private Handler tmpMainHandler4;
	private TextView notice;

	String[] appArr = { "function_1_icon,蓝牙读卡登记", "function_2_icon,实名记录", "function_3_icon,实名统计",
			"function_4_icon,快递信息比对", "function_5_icon,USB读卡登记","function_6_icon,NFC登记" ,"function_7_icon,客服" };

	// String[] appArr={"function_1_icon,实名认证", };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ChannelApplication.mContext = this;
		MobclickAgent.updateOnlineConfig(this);
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				switch (updateStatus) {
				case 0: // has update
					UmengUpdateAgent.showUpdateDialog(con, updateInfo);
					break;
				case 2: // has update
					Log.e("nowifi~~~~~~", "no wifi");
					break;
				}
			}
		});
		setContentView(R.layout.app_list2);
		con = this;

		gridApp = (GridView) findViewById(R.id.gridAppView);
		gridApp.setAdapter(new AppItemAdapter(this, pluginList));// 调用ImageAdapter.java
		gridApp.setOnItemClickListener(new OnItemClickListener() {// 监听事件
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gotoPlugin(pluginList.get(position));
				// Toast.makeText(con, "短按事件", 1000).show();
			}
		});
		this.titleBar = ((TitleBar) findViewById(R.id.titlebar));
		this.titleBar.setTitle(R.string.function_title);
		this.titleBar.setBackListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent mainIntent = new Intent(Main.this, Login.class);
				Bundle extras = new Bundle();
				mainIntent.putExtras(extras);
				startActivity(mainIntent);
				finish();
			}
		});
		this.titleBar.setUp();
		initAppList();

		// Log.e("mimimiimii", Encrypt.classicVarLenEncrypt("time9818", 30));
		RequestParams params = new RequestParams();
		HttpCallResultBackCurrentUser httpCallResultBackCurrentUser = new HttpCallResultBackCurrentUser(
				new HttpCallResultBack() {
					@Override
					public void doresult(HttpResult result) {
						if (!result.isSuccess()) {
							Intent mainIntent = new Intent(Main.this, Login.class);
							Bundle extras = new Bundle();
							mainIntent.putExtras(extras);
							startActivity(mainIntent);
							finish();
						}else{
//							SellApplication.getUidCurrent()

							PushManager.startWork(getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY, "3q1MuN9rcCazEGGaurhAZrgb");

						}
					}

					@Override
					public void dofailure() {

					}
				});
		httpCallResultBackCurrentUser.setParams(params);
		SellApplication.post(httpCallResultBackCurrentUser);

	}

	public void initAppList() {
		getPluginList();
		((AppItemAdapter) gridApp.getAdapter()).notifyDataSetChanged();
	}

	public void showKaoShi(int num) {
		if (num == 0) {
			notice.setVisibility(View.GONE);
		} else {
			notice.setVisibility(View.VISIBLE);
			notice.setText("" + num);
		}
	}

	private void getPluginList() {
		pluginList.clear();
		String isshowyidongapp = MobclickAgent.getConfigParams(this, "isshowyidongapp");
		int num = 0;
		for (String appcode : appArr) {
			num++;
			if ("1".equals(isshowyidongapp) && num > 3) {
				continue;
			}
			if ("0".equals(isshowyidongapp) && num > 1 && num < 4) {
				continue;
			}
			PluginMod p = new PluginMod();
			p.setAppcode(appcode.split(",")[0]);
			p.setName(appcode.split(",")[1]);
			pluginList.add(p);

		}
	}

	public void gotoPlugin(PluginMod p) {
		Log.e("xx", p.getAppcode() + " " + p.getName());
		if ("function_1_icon".equals(p.getAppcode())) {
			Intent mainIntent = new Intent(con, XiaoShouAnalysis.class);
			startActivity(mainIntent);
		} else if ("function_2_icon".equals(p.getAppcode())) {

			Intent mainIntent = new Intent(con, XiaoShouAnalysis4.class);
			startActivity(mainIntent);
		} else if ("function_3_icon".equals(p.getAppcode())) {
			Intent mainIntent = new Intent(con, XiaoShouAnalysis5.class);
			startActivity(mainIntent);
		} else if ("function_5_icon".equals(p.getAppcode())) {
			Intent mainIntent = new Intent(con, UsbRegisterActivity.class);
			startActivity(mainIntent);
		}else if ("function_6_icon".equals(p.getAppcode())) {
			Intent mainIntent = new Intent(con, NfcRegisterActivity.class);
			startActivity(mainIntent);
		}else if ("function_7_icon".equals(p.getAppcode())) {
			Intent mainIntent = new Intent(con, ChatActivity.class);
			startActivity(mainIntent);
		}
		else {
			Toast.makeText(con, p.getName() + "尚未开发", Toast.LENGTH_SHORT).show();
		}

	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		populateMenu(menu);
	}

	public void populateMenu(Menu menu) {
		// menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "查找插件");
		menu.add(Menu.NONE, RELOGIN, Menu.NONE, "返回登录界面");
		menu.add(Menu.NONE, CHAT, Menu.NONE, "客服");
		// menu.add(Menu.NONE, SYSTEM, Menu.NONE, "系统消息");
		// menu.add(Menu.NONE, REFASH, Menu.NONE, "刷新");
		// menu.add(Menu.NONE, ALLREFASH, Menu.NONE, "反馈意见");
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
				Intent mainIntent = new Intent(Main.this, Login.class);
				Bundle extras = new Bundle();
				mainIntent.putExtras(extras);
				startActivity(mainIntent);
				finish();
				return true;

			case CHAT:
				Intent mainIntent2 = new Intent(Main.this, ChatActivity.class);
				Bundle extras2 = new Bundle();
				mainIntent2.putExtras(extras2);
				startActivity(mainIntent2);
				finish();
				return true;
		}
		return false;
	}

	public void onResume() {
		super.onResume();

		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
