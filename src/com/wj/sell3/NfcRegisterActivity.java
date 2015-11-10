package com.wj.sell3;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cmcc.nativepackage.UsbIDCard;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.otg.idcard.OTGReadCardAPI;
import com.wj.sell.db.models.Shiming;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.HttpCallResultBack;
import com.wj.sell.util.HttpCallResultBackShiming;
import com.wj.sell.util.HttpResult;
import com.wj.sell3.ui.AlertDialogCustom;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogCancelListener;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogOKListener;
import com.wj.sell3.ui.TitleBar;
import com.wj.sell3.ui.ToastCustom;

public class NfcRegisterActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	public static Context con;
	public UserInfo user = null;
	public EditText tel;
	public EditText name;
	public EditText number;
	public RadioButton gender_male;
	public RadioButton gender_famale;
	public EditText ethnic;
	public EditText date;
	public EditText qixian;
	public EditText address;
	public EditText danwei;
	public Button photo;
	public Button btn;

	AlertDialogCustom localAlertDialogCustom;

	public ProgressDialog myDialog = null;
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;
	public static final int APPLIST = Menu.FIRST + 2;
	TitleBar titleBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		con = this;
		Bundle bunde = this.getIntent().getExtras();
		user = SellApplication.getUserInfoIdByUid(SellApplication.getUidCurrent());
		setContentView(R.layout.real_name_registration_form);

		photo = (Button) findViewById(R.id.photo);
		name = (EditText) findViewById(R.id.name);
		tel = (EditText) findViewById(R.id.tel);
		number = (EditText) findViewById(R.id.number);
		gender_male = (RadioButton) findViewById(R.id.gender_male);
		gender_famale = (RadioButton) findViewById(R.id.gender_famale);
		ethnic = (EditText) findViewById(R.id.ethnic);
		date = (EditText) findViewById(R.id.date);
		qixian = (EditText) findViewById(R.id.qixian);
		address = (EditText) findViewById(R.id.address);
		danwei = (EditText) findViewById(R.id.danwei);

		Button btnBt = (Button) findViewById(R.id.bluetooth);
		btnBt.setVisibility(View.GONE);

		btn = (Button) findViewById(R.id.nfc);
		btn.setVisibility(View.VISIBLE);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn.setEnabled(false);

				if (mAdapter == null) {
					Toast.makeText(NfcRegisterActivity.this, "设备不支持NFC", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!mAdapter.isEnabled()) {
					Toast.makeText(NfcRegisterActivity.this, "请在系统设置中先启用NFC功能", Toast.LENGTH_SHORT).show();
					return;
				}

				btn.setEnabled(false);
				Toast.makeText(NfcRegisterActivity.this, "正在读取...", Toast.LENGTH_SHORT).show();

				readflag = 0;
				startNFC_Listener();
			}
		});

		this.titleBar = ((TitleBar) findViewById(R.id.titlebar));
		this.titleBar.setTitle(R.string.shiming_luru);
		this.titleBar.setBackListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		this.titleBar.setUp();

		mAdapter = NfcAdapter.getDefaultAdapter(this);
		init_NFC();
		Log.e("nfc", ">>>>>>>>>>nfcAdapter:" + mAdapter);

		IPArray = new ArrayList<String>();
		IPArray.add("103.21.119.78");
		IPArray.add(remoteIPB);
		IPArray.add(remoteIPC);

		ReadCardAPI = new OTGReadCardAPI(getApplicationContext() /* tcontext */, IPArray);
		
	}

	public void failResult(String msg) {
		localAlertDialogCustom = new AlertDialogCustom(this);
		localAlertDialogCustom.show();
		localAlertDialogCustom.setMessage(msg);
		localAlertDialogCustom.setOnOKListener("返回主界面", new AlertDialogOKListener() {

			@Override
			public void onOKClick() {
				// TODO Auto-generated method stub
				localAlertDialogCustom.dismiss();
				finish();

			}
		});
		localAlertDialogCustom.setOnCancelListener("修改信息", new AlertDialogCancelListener() {

			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
				localAlertDialogCustom.dismiss();
			}
		});

	}

	public void successResult(String photo) {
		Intent mainIntent = new Intent(NfcRegisterActivity.this, XiaoShouAnalysisConfirm.class);
		Bundle extras = new Bundle();
		extras.putString("tel", tel.getText().toString().trim());
		extras.putString("name", name.getText().toString().trim());
		extras.putString("number", number.getText().toString().trim());
		extras.putString("address", address.getText().toString().trim());
		extras.putString("photo", photo);
		mainIntent.putExtras(extras);
		con.startActivity(mainIntent);
	}

	public void queryTongji(View view) {
		if ("".equals(tel.getText().toString())) {
			// Toast.makeText(con, "请填写手机号", Toast.LENGTH_LONG).show();
			ToastCustom.showMessage(con, "请填写手机号");
			return;
		}

		if ("".equals(number.getText().toString())) {
			ToastCustom.showMessage(con, "请扫描身份证");
			return;
		}

		if ("".equals(name.getText().toString())) {
			ToastCustom.showMessage(con, "请扫描身份证");
			return;
		}
		if ("".equals(address.getText().toString())) {
			ToastCustom.showMessage(con, "请扫描身份证");
			return;
		}
		if ("".equals(danwei.getText().toString())) {
			ToastCustom.showMessage(con, "请扫描身份证");
			return;
		}
		// if("".equals(qixian.getText().toString())){
		// ToastCustom.showMessage(con, "请扫描身份证");
		// return;
		// }
		RequestParams params = new RequestParams();
		params.addBodyParameter("phone_number", tel.getText().toString());
		params.addBodyParameter("cardno", number.getText().toString());
		params.addBodyParameter("name", name.getText().toString());
		params.addBodyParameter("address", address.getText().toString());
		params.addBodyParameter("qfjg", danwei.getText().toString());
		params.addBodyParameter("yxqx", qixian.getText().toString());
		final Shiming shiming = new Shiming();
		shiming.setAddress(address.getText().toString());
		shiming.setCardno(number.getText().toString());
		shiming.setName(name.getText().toString());
		shiming.setPhone_number(tel.getText().toString());
		shiming.setQfjg(danwei.getText().toString());
		shiming.setYxqx(qixian.getText().toString());
		Date date1 = new Date();
		shiming.setCreate_time("" + (date1.getYear() + 1900) + "." + date1.getMonth() + "." + date1.getDate() + " "
				+ date1.getHours() + ":" + date1.getMinutes() + ":" + date1.getSeconds());

		try {
			SellApplication.db.saveOrUpdate(shiming);
		} catch (DbException e) {
			e.printStackTrace();
		}

		SellApplication.showDialog("正在实名", "", con);
		HttpCallResultBackShiming httpCallResultBackShiming = new HttpCallResultBackShiming(new HttpCallResultBack() {
			@Override
			public void doresult(HttpResult result) {
				if (result.getResult() != null) {
					Shiming shiming1 = new Shiming(result.getResult());
					if (result.isSuccess()) {

						shiming1.setSuccess(2);
						ToastCustom.showMessage(con, "实名成功。");
						return;
					} else {
						shiming1.setSuccess(0);
						shiming1.setMessage(result.getMessage());
						SellApplication.failureResult(result);
					}
					try {
						SellApplication.db.saveOrUpdate(shiming1);
					} catch (DbException e) {
						e.printStackTrace();
					}

				} else {
					if (!result.isSuccess()) {
						shiming.setMessage(result.getMessage());
						try {
							SellApplication.db.saveOrUpdate(shiming);
						} catch (DbException e) {
							e.printStackTrace();
						}
						SellApplication.failureResult(result);
					}
				}
			}

			@Override
			public void dofailure() {

			}
		});
		httpCallResultBackShiming.setParams(params);
		SellApplication.post(httpCallResultBackShiming);

	}

	public String getDate(DatePicker dp) {
		String year = String.valueOf(dp.getYear());
		String month = String.valueOf(dp.getMonth() + 1);
		String day = String.valueOf(dp.getDayOfMonth());
		if (month.length() == 1) {
			month = "0" + month;
		}
		if (day.length() == 1) {
			day = "0" + day;
		}
		return year + "-" + month + "-" + day;
	}

	public void queryXiaoShouToday(View view) {
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		populateMenu(menu);
	}

	public void populateMenu(Menu menu) {
		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "返回主界面");
		menu.add(Menu.NONE, APPLIST, Menu.NONE, "清空");
		// menu.add(Menu.NONE, APPLIST, Menu.NONE, "应用列表");
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

		case SEARCHPLUGIN:
			finish();
			return true;
		case APPLIST:
			finish();
			return true;
		}
		return false;
	}

	public void onResume() {
		super.onResume();
		if (Convert.newtel) {
			Convert.newtel = false;
			tel.setText("");
			name.setText("");
			number.setText("");
			address.setText("");
		}
		
		startNFC_Listener();
	}

	public void onPause() {
		super.onPause();
		stopNFC_Listener();
	}

	/* --------------------------------- */
	@Override
	public void onNewIntent(Intent intent) {
		Log.e("nfc", "Discovered tag with intent: " + intent);
		
		
		if(readflag == 1){
			return ;
		}
		readflag = 1;
		btn.setEnabled(true);
		inintent = intent;
		
		int tt = ReadCardAPI.NfcReadCard(inintent);
		if (tt == 2)
		{
			new AlertDialog.Builder(this)
					.setTitle("提示").setMessage("接收数据超时！")
					.setPositiveButton("确定", null).show();
		}
		if (tt == 41)
		{
			new AlertDialog.Builder(this)
					.setTitle("提示").setMessage("读卡失败！")
					.setPositiveButton("确定", null).show();
		}
		if (tt == 42)
		{
			new AlertDialog.Builder(this)
					.setTitle("提示").setMessage("没有找到服务器！")
					.setPositiveButton("确定", null).show();
		}
		if (tt == 43)
		{
			new AlertDialog.Builder(this)
					.setTitle("提示").setMessage("服务器忙！")
					.setPositiveButton("确定", null).show();
		}
		if (tt == 90)
		{
			name.setText(ReadCardAPI.Name());
			if (ReadCardAPI.SexL().equals("男")) {
				gender_male.setChecked(true);
				gender_famale.setChecked(false);
			} else {
				gender_male.setChecked(false);
				gender_famale.setChecked(true);
			}

			ethnic.setText(ReadCardAPI.NationL());
			date.setText(ReadCardAPI.BornL());
			address.setText(ReadCardAPI.Address());
			number.setText(ReadCardAPI.CardNo());
			danwei.setText(ReadCardAPI.Police());
			String qixian_start = ReadCardAPI.Activity();
			String qixian_end = ReadCardAPI.ActivityL();
			String qixian_str = qixian_start.substring(0, 4) + "." + qixian_start.substring(4, 6) + "."
					+ qixian_start.substring(6, 8)
					+ "-" + qixian_end.substring(0, 4) + "." + qixian_end.substring(4, 6) + "."
					+ qixian_end.substring(6, 8);
			Log.e("nfc",qixian_start+"-"+qixian_end);
			qixian.setText(ReadCardAPI.ActivityL());
			
			ReadCardAPI.release();
		}
	}

	private ArrayList<String> IPArray = null;
	public static String remoteIPA = "";
	public static String remoteIPB = "";
	public static String remoteIPC = "";

	private OTGReadCardAPI ReadCardAPI;
	private NfcAdapter mAdapter = null;
	private PendingIntent pi = null;
	// 滤掉组件无法响应和处理的Intent
	private IntentFilter tagDetected = null;
	private String[][] mTechLists;
	private Intent inintent = null;
	private int readflag = 0;

	private void startNFC_Listener() {
		mAdapter.enableForegroundDispatch(this, pi, new IntentFilter[] { tagDetected }, mTechLists);
	}

	private void stopNFC_Listener() {
		mAdapter.disableForegroundDispatch(this);
	}

	private void init_NFC() {
		pi = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		tagDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);// .ACTION_TAG_DISCOVERED);
		tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
		mTechLists = new String[][] { new String[] { NfcB.class.getName() } };
	}

}