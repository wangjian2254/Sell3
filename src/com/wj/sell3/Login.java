package com.wj.sell3;

import java.io.File;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.HttpCallResultBack;
import com.wj.sell.util.HttpCallResultBackLogin;
import com.wj.sell.util.HttpResult;
import com.wj.sell3.ui.AlertDialogCustom;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogCancelListener;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogOKListener;
import com.wj.sell3.ui.ChannelApplication;
import com.wj.sell3.ui.IMELayout;
import com.wj.sell3.ui.NetWork;

public class Login extends Activity implements AlertDialogCancelListener, AlertDialogOKListener {

	Button login;
	Button reset;
	EditText username;
	EditText password;
	private ImageView bg;
	private ImageView loadImage;
	private TextView loadingTextView;
	private View loadingView;
	private View loginView;
	private IMELayout rLayout;
	TextView msg;
	AlertDialogCustom localAlertDialogCustom;
	Context context;
	String uname;
	String msgstr;

	List<NameValuePair> params = null;
	DefaultHttpClient client;
	HttpResponse httpResponse;
	UserInfo user;
	public ProgressDialog myDialog = null;

	private Handler mMainHandler;
	
	NfcAdapter nfcAdapter;

	private int checkNetWork()
	{
		return NetWork.getConnectionType(this);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ChannelApplication.mContext = this;
		context = this;
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		// h=new Handler();
		File dirFile = new File(Convert.infPath);
		if (!dirFile.exists())
		{
			dirFile.mkdir();
		}
		dirFile = new File(Convert.picPath);
		if (!dirFile.exists())
		{
			dirFile.mkdir();
		}

		setContentView(R.layout.load);
		client = new DefaultHttpClient();
		this.loadImage = ((ImageView) findViewById(R.id.image_load));
		// Animation localAnimation = AnimationUtils.loadAnimation(this,
		// R.anim.load);
		// this.loadImage.setAnimation(localAnimation);
		// localAnimation.startNow();
		this.loadingView = findViewById(R.id.loading_layout_id);
		this.loginView = findViewById(R.id.login_with_name_pwd_layout_id);
		this.rLayout = ((IMELayout) findViewById(R.id.b_layout));
		this.bg = ((ImageView) findViewById(R.id.image_bg));
		login = (Button) findViewById(R.id.login_btn_ok_id);
		username = (EditText) findViewById(R.id.channal_num_edittext_id);
		// username.setText("bj040_01");
		password = (EditText) findViewById(R.id.channal_pwd_edittext_id);
		// password.setText("111111");

		// this.titleBar = ((TitleBar)findViewById(R.id.titlebar));
		// this.titleBar.setTitle(R.string.login_title);
		// this.titleBar.setUp();
		// repassword = (EditText) findViewById(R.id.repassword);
		// cpassword = (CheckBox) findViewById(R.id.writeme);
		// repd = (LinearLayout) findViewById(R.id.repd);

		// repd.setVisibility(View.GONE);
		// 检测是否记录密码了

		mMainHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// Log.i(TAG, "Got an incoming message from the child thread - "
				// + (String) msg.obj);
				if (msg.obj.toString().indexOf("reg") == 0) {
					String u = msg.obj.toString().substring(3);
					username.setText(u);
					// back(null);
					return;
				}
				// if(msg.obj.toString().indexOf("login")==0){
				// // String u=msg.obj.toString().substring(5);
				// // username.setText(u);
				// // password.setText(u);
				// onLogin(null);
				// return;
				// }

				// 接收子线程的消息
				Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_LONG).show();

				// info.setText((String) msg.obj);
			}

		};
		user = SellApplication.getUserInfoIdByUid(SellApplication.getUidCurrent());
		if (user != null) {
			username.setText(user.getUsername());
			password.setText("");
		}

		if (this.getIntent().getExtras() != null) {

			msgstr = (String) this.getIntent().getExtras().getString("message");
		}
		if (msgstr != null) {
			new Thread() {
				public void run() {
					try {
						sleep(1000);
						Message toMain = mMainHandler.obtainMessage();
						toMain.obj = msgstr;
						toMain.sendToTarget();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();

		}

	}

	public void onOKClick()
	{
		localAlertDialogCustom.dismiss();
		if (android.os.Build.VERSION.SDK_INT > 10) {
			// 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
			startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
		} else {
			startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
	}

	public void onCancelClick()
	{
		localAlertDialogCustom.dismiss();
		finish();
	}

	public void setMsg(String m) {
		msg.setText(m);
	}

	public void onLogin(View view) {

		SellApplication.showDialog("正在同步", "正在下载数据，请稍后……", context);

		uname = username.getText().toString().trim();
		String pwd = password.getText().toString().trim();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_username", uname);
		requestParams.addBodyParameter("app_password", pwd);
		requestParams.addBodyParameter("app_imei", this.getIMEI());
		if( nfcAdapter != null ){
			requestParams.addBodyParameter("app_nfc", "1");
		}

		HttpCallResultBackLogin httpCallResultBackLogin = new HttpCallResultBackLogin(new HttpCallResultBack() {
			@Override
			public void doresult(HttpResult result) {
				if (result.isSuccess()) {
					goWelcome();
				} else {
					SellApplication.failureResult(result);
				}

			}

			@Override
			public void dofailure() {

			}
		});
		httpCallResultBackLogin.setParams(requestParams);
		SellApplication.post(httpCallResultBackLogin);

	}

	public void goWelcome() {
		Intent mainIntent = new Intent(Login.this, Main.class);
		Bundle extras = new Bundle();
		// user=new UserInfo();
		// user.setUsername("test1");
		// user.setPassword("111");
		// UserInfoUtil.updateUserInfo(user, context);
		extras.putSerializable("user", user);
		mainIntent.putExtras(extras);
		Login.this.startActivity(mainIntent);
		Login.this.finish();
	}

	public void onDestroy() {
		super.onDestroy();
	}

	public void reSet(View view) {

	}

	public void onResume() {
		super.onResume();
		if (localAlertDialogCustom == null || !localAlertDialogCustom.isShowing()) {
			if (checkNetWork() != -1) {
				loginView.setVisibility(View.VISIBLE);
			} else {
				loginView.setVisibility(View.GONE);
				localAlertDialogCustom = new AlertDialogCustom(this);
				localAlertDialogCustom.show();
				localAlertDialogCustom.setMessage("网络不通，请设置");
				localAlertDialogCustom.setOnOKListener("设置", this);
				localAlertDialogCustom.setOnCancelListener("退出", this);
			}
		}
	}

	public void onPause() {
		super.onPause();
	}

	public String getIMEI() {
		String id = "";
		try {
			Context ctx = this.context;
			TelephonyManager mgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			id = mgr.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
}