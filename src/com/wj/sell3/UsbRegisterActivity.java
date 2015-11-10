package com.wj.sell3;

import java.util.Date;

import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.cmcc.nativepackage.UsbIDCard;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
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

public class UsbRegisterActivity extends Activity {
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
	public Button btnUsb;

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

		btnUsb = (Button) findViewById(R.id.usb);
		btnUsb.setVisibility(View.VISIBLE);
		btnUsb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnUsb.setEnabled(false);

				Handler handler = new CardHandler(UsbRegisterActivity.this);
				CardReader reader = new CardReader(handler);
				new Thread(reader).start();
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
		Intent mainIntent = new Intent(UsbRegisterActivity.this, XiaoShouAnalysisConfirm.class);
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
	}

	public void onPause() {
		super.onPause();
	}

	public final class CardHandler extends Handler {
		UsbRegisterActivity activity;

		public CardHandler(UsbRegisterActivity activity) {
			this.activity = activity;
		}

		public void handleMessage(Message paramMessage)
		{
			activity.btnUsb.setEnabled(true);
			switch (paramMessage.what)
			{
			case 110:
				ToastCustom.showMessage(activity, "未检测到二代证设备连接到手机，请重启二代证设备后进行重试");
				return;
			case 101:
				ToastCustom.showMessage(activity, "打开设备失败，请尝试重新连接二代证设备");
				return;
			case 109:
				ToastCustom.showMessage(activity, "初始化设备失败，请尝试重启二代证设备并重新操作");
				return;
			case 107:
				ToastCustom.showMessage(activity, "读取信息失败，请尝试调整证件摆放位置并重新操作");
				return;
			case 106: {
				Bundle localBundle = paramMessage.getData();
				String[] cardInfo = localBundle.getStringArray("cardInfo");
				int i = 0;
				String name = cardInfo[i++].trim();
				String sex = cardInfo[i++].trim();
				String ethnic = cardInfo[i++].trim();
				String date = cardInfo[i++].trim();
				String address = cardInfo[i++].trim();
				String number = cardInfo[i++].trim();
				String danwei = cardInfo[i++].trim();
				String qixian_str = "";
				try {
					String[] qixian = cardInfo[i++].trim().replace(" ", "").split("-");
					String qixian_start = qixian[0];
					String qixian_end = qixian[1];
					qixian_str = qixian_start.substring(0, 4) + "." + qixian_start.substring(4, 6) + "."
							+ qixian_start.substring(6, 8)
							+ "-" + qixian_end.substring(0, 4) + "." + qixian_end.substring(4, 6) + "."
							+ qixian_end.substring(6, 8);
				} catch (Exception e) {
				}
				UsbRegisterActivity.this.name.setText(name);
				UsbRegisterActivity.this.number.setText(number);

				if (sex.equals("男")) {
					gender_male.setChecked(true);
					gender_famale.setChecked(false);
				} else {
					gender_male.setChecked(false);
					gender_famale.setChecked(true);
				}
				UsbRegisterActivity.this.ethnic.setText(ethnic);
				UsbRegisterActivity.this.date.setText(date);
				UsbRegisterActivity.this.qixian.setText(qixian_str);
				UsbRegisterActivity.this.address.setText(address);
				UsbRegisterActivity.this.danwei.setText(danwei);
				return;
			}
			case 200: {
				Bundle localBundle = paramMessage.getData();
				String e = localBundle.getString("e");
			}
			default:

				return;
			} // switch
		} // fn
	} // cls

	public final class CardReader implements Runnable {
		private Handler handler;
		private byte[] b = new byte[65530];
		private String[] c = new String[9];

		public CardReader(Handler paramHandler)
		{
			handler = paramHandler;
		}

		private void read() {
			try {
				if (UsbIDCard.isOTGDevice() != 0) {
					this.handler.sendEmptyMessage(110);
					return;
				}
				if (UsbIDCard.openIDCard(2, "", "") != 0) {
					this.handler.sendEmptyMessage(101);
					return;
				}
				if (UsbIDCard.initialIDCard() == 0) {
					int i = UsbIDCard.getIdCardInfo(this.c, this.b);
					UsbIDCard.closeIDCard();
					if (i != 0) {
						this.handler.sendEmptyMessage(107);
					} else {
						Bundle localBundle = new Bundle();
						Message localMessage = new Message();
						localMessage.what = 106;
						localBundle.putStringArray("cardInfo", this.c);
						localBundle.putByteArray("base64Img", this.b);
						localMessage.setData(localBundle);
						this.handler.sendMessage(localMessage);
					}
				} else {
					this.handler.sendEmptyMessage(109);
				}
			} catch (Exception e) {
				Bundle localBundle = new Bundle();
				Message localMessage = new Message();
				localMessage.what = 200;
				localBundle.putString("e", e.toString());
				localMessage.setData(localBundle);
				this.handler.sendMessage(localMessage);
			} finally {
			}
		}

		@Override
		public void run() {
			read();
		}

	} // cls
}