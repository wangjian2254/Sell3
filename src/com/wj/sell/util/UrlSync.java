package com.wj.sell.util;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import com.wj.sell.db.models.UserInfo;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;

public class UrlSync {
	
	public static final String GET="GET";
	public static final String POST="POST";
	public static final String HTTP="http";
	
	private int connectNum;
	private String uri=null;
	private Context mainContext;
	private String modth=GET;
	private String urlparam=null;
	private List<NameValuePair> prarm=null;
	private String resultType=null;
	
	private String toastContentSu=null;
	private String toastContentFa=null;
	private boolean isToast=true;
	
	private String result=null;
	private String userinfoparam=null;
	private JSONObject jsonobj=null;
	
	private Handler handler=null;
	
	private UserInfo user=null;
	
	// 通知列表
	
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public boolean isGet(){
		if(GET.equals(this.modth)){
			return true;
		}
		return false;
	}
	public String getUri() {
		return uri;
	}

	public String getAllUri(){
//		return null;
		return uri+(urlparam==null?"?deviceid="+Secure.getString(getMainContext().getContentResolver(), Secure.ANDROID_ID):urlparam+"&deviceid="+Secure.getString(getMainContext().getContentResolver(), Secure.ANDROID_ID));
	}
	public void setUri(String uri) {
			if(uri.indexOf(HTTP)!=0){
				this.uri=HTTP+uri;
			}else{
			this.uri = uri;
			}
		
	}
	public String getModth() {
		return modth;
	}
	public void setModth(String modth) {
		this.modth = modth;
	}

	
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	
	
	public UrlSync() {
	}
	
	
	
	public String getUrlparam() {
		return urlparam;
	}
	
	public void setUrlparam(String urlparam) {
		if(urlparam==null){
			return;
		}
		this.urlparam = urlparam;
	}
	
	
	
	public String getToastContentSu() {
		return toastContentSu;
	}
	
	public void setToastContentSu(String toastContentSu) {
		this.toastContentSu = toastContentSu;
	}
	
	public String getToastContentFa() {
		return toastContentFa;
	}
	
	public void setToastContentFa(String toastContentFa) {
		this.toastContentFa = toastContentFa;
	}
	
	
	public boolean isToast() {
		return isToast;
	}
	
	public void setToast(boolean isToast) {
		this.isToast = isToast;
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	public List<NameValuePair> getPrarm() {
		return prarm;
	}
	
	public void setPrarm(List<NameValuePair> prarm) {
		this.prarm = prarm;
	}
	
	

	
	public void doResult() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean doPerResult()throws Exception{
		jsonobj=new JSONObject(getResult());
		int status=jsonobj.getInt("status_code");
		if(status==200){
			return true;
		}else{
			if(getHandler()!=null){
				
				Message hmsg=getHandler().obtainMessage();
				hmsg.arg1=404;
				hmsg.obj=jsonobj.getString("message");
				Log.e("login_error", jsonobj.getString("message"));
				
				getHandler().sendMessage(hmsg);
				setHandler(null);
			}
			return false;
		}
	}

	
	public void setMainContext(Context context) {
		// TODO Auto-generated method stub
		this.mainContext=context;
	}
	
	public Context getMainContext() {
		// TODO Auto-generated method stub
		return this.mainContext;
	}

	
	public String getUserinfoparam() {
		return userinfoparam==null?"":userinfoparam;
	}
	public void setUserinfoparam(String userinfoparam) {
		this.userinfoparam = userinfoparam;
	}
	
	public void doFailureResult() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isNeedConnect(){
		if(this.connectNum>3){
			return false;
		}else{
			return true;
		}
	}
	public void addConnectNum(){
		this.connectNum++;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public JSONObject getJsonobj() {
		return jsonobj;
	}
	public void setJsonobj(JSONObject jsonobj) {
		this.jsonobj = jsonobj;
	}
}
