package com.wj.sell.db.models;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.exception.DbException;
import com.wj.sell3.SellApplication;
import org.json.JSONObject;

import java.io.Serializable;

public class UserInfo implements Serializable{

	@Id()
	@NoAutoIncrement()
	private int s_id;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public int getS_id() {
		return s_id;
	}

	public void setS_id(int s_id) {
		this.s_id = s_id;
	}

	private String username;
	private String nickname;
	private boolean is_active;


	/**
	 * 重置用户个人信息
	 * by:王健 at:2015-3-13
	 */
	public void initUserInfo(){

		UserInfo p = null;
		try {
			p = SellApplication.db.findById(UserInfo.class, this.s_id);
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (p!=null){
			this.username = p.getUsername();
			this.nickname = p.getNickname();
			this.is_active = p.isIs_active();
		}

	}

	/**
	 * 根据json生成 model的构造函数，增加环信密码和环信注册状态
	 * by:王健 at:2015-1-21
	 * 优化服务器返回json数据
	 * by黄海杰 at:2015-3-10
	 * @param json
	 */
	public UserInfo(JSONObject json){
		this.s_id = json.optInt("id");
		initUserInfo();
		if (json.has("app_username")){
			this.username = json.optString("app_username");
		}
		if (json.has("nickname")){
			this.nickname = json.optString("nickname");
		}
		if (json.has("is_active")){
			this.is_active = json.optBoolean("is_active");
		}


	}
	public UserInfo(){

	}

}
