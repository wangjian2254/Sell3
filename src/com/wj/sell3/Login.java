package com.wj.sell3;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.wj.sell3.R;
import com.wj.sell3.ui.AlertDialogCustom;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogOKListener;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogCancelListener;
import com.wj.sell3.ui.ChannelApplication;
import com.wj.sell3.ui.EditTextCustom;
import com.wj.sell3.ui.IMELayout;
import com.wj.sell3.ui.NetWork;
import com.wj.sell3.ui.TitleBar;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends Activity implements AlertDialogCancelListener,AlertDialogOKListener {

	Button login;
	Button reset;
	EditTextCustom username;
	EditTextCustom password;
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
	
	private int checkNetWork()
	  {
	    return NetWork.getConnectionType(this);
	  }
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ChannelApplication.mContext=this;
		context=this;
//		h=new Handler();
		File dirFile = new File(Convert.infPath);   
        if(!dirFile.exists())
        {
            dirFile.mkdir();
        } 
        dirFile=new File(Convert.picPath);
        if(!dirFile.exists())
        {
            dirFile.mkdir();
        } 
        
		
		setContentView(R.layout.load);
		client = new DefaultHttpClient();
		this.loadImage = ((ImageView)findViewById(R.id.image_load));
	    Animation localAnimation = AnimationUtils.loadAnimation(this, R.anim.load);
	    this.loadImage.setAnimation(localAnimation);
	    localAnimation.startNow();
	    this.loadingView = findViewById(R.id.loading_layout_id);
	    this.loginView = findViewById(R.id.login_with_name_pwd_layout_id);
	    this.rLayout = ((IMELayout)findViewById(R.id.b_layout));
	    this.bg = ((ImageView)findViewById(R.id.image_bg));
		login = (Button) findViewById(R.id.login_btn_ok_id);
		username = (EditTextCustom) findViewById(R.id.channal_num_edittext_id);
		password = (EditTextCustom) findViewById(R.id.channal_pwd_edittext_id);
		
//		this.titleBar = ((TitleBar)findViewById(R.id.titlebar));
//	    this.titleBar.setTitle(R.string.login_title);
//	    this.titleBar.setUp();
//		repassword = (EditText) findViewById(R.id.repassword);
//		cpassword = (CheckBox) findViewById(R.id.writeme);
//		repd = (LinearLayout) findViewById(R.id.repd);
		
//		repd.setVisibility(View.GONE);
		// 检测是否记录密码了
		
		
		mMainHandler = new Handler() {
			 
            @Override
            public void handleMessage(Message msg) {
//                Log.i(TAG, "Got an incoming message from the child thread - "
//                        + (String) msg.obj);
            	if(msg.obj.toString().indexOf("reg")==0){
            		String u=msg.obj.toString().substring(3);
            		username.setText(u);
//            		back(null);
            		return;
            	}
//            	if(msg.obj.toString().indexOf("login")==0){
////            		String u=msg.obj.toString().substring(5);
////            		username.setText(u);
////					password.setText(u);
//					onLogin(null);
//					return;
//            	}
            	
            		// 接收子线程的消息
                	Toast.makeText(context, msg.obj.toString(), 4000).show();
            	
                
//                info.setText((String) msg.obj);
            }
 
        };
        	user=UserInfoUtil.getCurrentUserInfo(this);
        	if(user!=null){
        		username.setText(user.getUsername());
        		password.setText(user.getPassword());
        	}
       
        	if(this.getIntent().getExtras()!=null){
        		
        		msgstr = (String)this.getIntent().getExtras().getString("message");
        	}
    		if (msgstr!=null){
    			new Thread() {
            		public void run() {
            			try {
    						sleep(1000);
    						Message toMain = mMainHandler.obtainMessage();
                			toMain.obj=msgstr;
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
		 if(android.os.Build.VERSION.SDK_INT > 10 ){
		     //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
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
	
	public void setMsg(String m){
		msg.setText(m);
	}

	

	

	
public void onLogin(View view){
		
		if(myDialog==null||!myDialog.isShowing()){
			myDialog = ProgressDialog.show  
	    	(
	    			this,
	    		"正在同步",
	    		"正在下载数据，请稍候......"
	    	);
		}
		
		
		new Thread() {
			public void run() {
		
    	uname=username.getText().toString().trim();
    	String pwd=password.getText().toString().trim();
    	
    	String url=Convert.hosturl+"/oa/clientLogin/";
    	HttpPost getMethod=new HttpPost(url);
    	List<NameValuePair> param=new ArrayList<NameValuePair>();
    	param.add(new BasicNameValuePair("username",uname));
    	param.add(new BasicNameValuePair("password",pwd));
    	try {
			getMethod.setEntity(new UrlEncodedFormEntity(param,HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
    	try{
    		// 创建一个本地Cookie存储的实例  
            CookieStore cookieStore = new BasicCookieStore();  
            //创建一个本地上下文信息  
            HttpContext localContext = new BasicHttpContext();  
            //在本地上下问中绑定一个本地存储  
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);  
    		httpResponse=client.execute(getMethod,localContext);
    		if(httpResponse.getStatusLine().getStatusCode()==200){ 
                //取出回应字串 
                String strResult=EntityUtils.toString(httpResponse.getEntity()); 
                JSONObject result=new JSONObject(strResult);
                if(result.getBoolean("success")){
                	
                	user=new UserInfo();
                	user.setUsername(uname);
                	user.setPassword(pwd);
                	
                  //获取cookie中的各种信息  
                    List<Cookie> cookies = cookieStore.getCookies();  
                    Cookie cookie;
                    for (int i = 0; i < cookies.size(); i++) {  
                    	cookie=cookies.get(i);
                    	cookie.getName();
                    	if("sessionid".equals(cookie.getName())){
                    		user.setCookies("sessionid="+cookie.getValue()+";"+user.getCookies());
                    	}
                    	
                    	
                    	
                    }  
                	if(UserInfoUtil.updateUserInfo(user, context)){
						myDialog.dismiss();
	        			goWelcome();
                	}else{
                		myDialog.dismiss();
                    	Message toMain = mMainHandler.obtainMessage();
            			toMain.obj="更新用户信息错误。";
            			mMainHandler.sendMessage(toMain);
                	}
                }else{
                	myDialog.dismiss();
                	Message toMain = mMainHandler.obtainMessage();
        			toMain.obj=result.getString("message");
        			mMainHandler.sendMessage(toMain);
                }
            }else{ 
            	myDialog.dismiss();
            	Message toMain = mMainHandler.obtainMessage();
    			toMain.obj="网络不给力,请检查网络设置。";
    			mMainHandler.sendMessage(toMain);
            } 
    		
    	}catch(Throwable t){
    		myDialog.dismiss();
    		Message toMain = mMainHandler.obtainMessage();
			toMain.obj="网络不给力,请检查网络设置。";
			mMainHandler.sendMessage(toMain);
    	}
		}
		
	}.start();
    	
    }
	public void goWelcome(){
		Intent mainIntent = new Intent(Login.this, Main.class);
		Bundle extras=new Bundle();
//		user=new UserInfo();
//    	user.setUsername("test1");
//    	user.setPassword("111");
//    	UserInfoUtil.updateUserInfo(user, context);
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
	    if(localAlertDialogCustom==null||!localAlertDialogCustom.isShowing()){
	    	if(checkNetWork()!=-1){
	    		loginView.setVisibility(View.VISIBLE);
	    	}else{
	    		loginView.setVisibility(View.GONE);
	    		localAlertDialogCustom= new AlertDialogCustom(this);
	    		localAlertDialogCustom.show();
	    		localAlertDialogCustom.setMessage("网络不通，请设置");
	    		localAlertDialogCustom.setOnOKListener("设置",this);
	    		localAlertDialogCustom.setOnCancelListener("退出",this);
	    	}
	    }
	}
	public void onPause() {
	    super.onPause();
	}
    
    
}