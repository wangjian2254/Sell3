package com.wj.sell.db.models;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.wj.sell3.SellApplication;
import org.json.JSONObject;

import java.io.Serializable;

public class Shiming implements Serializable{

	private int id;
	private int s_id;



	private String phone_number;
	private String cardno;
	private String name;
	private String address;
	private String qfjg;
	private String yxqx;

	public int getS_id() {
		return s_id;
	}

	public void setS_id(int s_id) {
		this.s_id = s_id;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getQfjg() {
		return qfjg;
	}

	public void setQfjg(String qfjg) {
		this.qfjg = qfjg;
	}

	public String getYxqx() {
		return yxqx;
	}

	public void setYxqx(String yxqx) {
		this.yxqx = yxqx;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	private int success;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 重置用户个人信息
	 * by:王健 at:2015-3-13
	 */
	public void initUserInfo(){

		Shiming p = null;
		try {
			if(this.phone_number!=null){
				p = SellApplication.db.findFirst(Selector.from(Shiming.class).where("phone_number","=",this.phone_number).orderBy("id", true));
			}else{
				p = SellApplication.db.findFirst(Selector.from(Shiming.class).where("s_id","=",this.s_id).orderBy("id", true));
			}

		} catch (DbException e) {
			e.printStackTrace();
		}
		if (p!=null){
			this.id = p.getId();
			this.phone_number = p.getPhone_number();
			this.name = p.getName();
			this.cardno = p.getCardno();
			this.address = p.getAddress();
			this.qfjg = p.getQfjg();
			this.yxqx = p.getYxqx();
			this.success = p.getSuccess();

		}

	}

	/**
	 * 根据json生成 model的构造函数，增加环信密码和环信注册状态
	 * by:王健 at:2015-1-21
	 * 优化服务器返回json数据
	 * by黄海杰 at:2015-3-10
	 * @param json
	 */
	public Shiming(JSONObject json){
		this.s_id = json.optInt("id");
		if (json.has("phone_number")){
			this.phone_number = json.optString("phone_number", null);
		}
		initUserInfo();

		if (json.has("name")){
			this.name = json.optString("name");
		}
		if (json.has("cardno")){
			this.cardno = json.optString("cardno");
		}
		if (json.has("address")){
			this.address = json.optString("address");
		}
		if (json.has("qfjg")){
			this.qfjg = json.optString("qfjg");
		}
		if (json.has("yxqx")){
			this.yxqx = json.optString("yxqx");
		}
		if (json.has("success")){
			this.success = json.optInt("success");
		}


	}
	public Shiming(){

	}

}
