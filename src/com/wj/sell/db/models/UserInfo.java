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
	private String username;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	private String department;
	private String name;
	private String nickName;
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getS_id() {
		return s_id;
	}

	public void setS_id(int s_id) {
		this.s_id = s_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String QQ) {
		this.QQ = QQ;
	}

	private String QQ;

	private String password;
	private String icon;
	private String sex;
	private String hxpassword;
	private boolean hx_reg;
	private String birthday;

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	private String idnumber;
	private int hkaddress;
	private int address;

	public String getHunyin() {
		return hunyin;
	}

	public void setHunyin(String hunyin) {
		this.hunyin = hunyin;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public int getHkaddress() {
		return hkaddress;
	}

	public void setHkaddress(int hkaddress) {
		this.hkaddress = hkaddress;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}



	private String hunyin;
	private String email;
	private String company;
	private String title;
	private String icon_url;
	public String getHxpassword() {
		return hxpassword;
	}

	public void setHxpassword(String hxpassword) {
		this.hxpassword = hxpassword;
	}

	public boolean isHx_reg() {
		return hx_reg;
	}

	public void setHx_reg(boolean hx_reg) {
		this.hx_reg = hx_reg;
	}



	private String guanzhu_list;
	private String canyu_list;
	private int xueli;

	public String getCanyu_list() {
		return canyu_list;
	}

	public void setCanyu_list(String canyu_list) {
		this.canyu_list = canyu_list;
	}

	public int getWorkage() {
		return workage;
	}

	public void setWorkage(int workage) {
		this.workage = workage;
	}

	public int getXueli() {
		return xueli;
	}

	public void setXueli(int xueli) {
		this.xueli = xueli;
	}

	public String getZhicheng() {
		return zhicheng;
	}

	public void setZhicheng(String zhicheng) {
		this.zhicheng = zhicheng;
	}

	public String getZhiyezigezheng() {
		return zhiyezigezheng;
	}

	public void setZhiyezigezheng(String zhiyezigezheng) {
		this.zhiyezigezheng = zhiyezigezheng;
	}

	private int workage;
	private String zhicheng;
	private String zhiyezigezheng;

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
			this.hx_reg = p.isHx_reg();
			this.username = p.getUsername();
			this.icon = p.getIcon();
			this.sex = p.getSex();
			this.name = p.getName();
			this.birthday = p.getBirthday();
			this.address = p.getAddress();
			this.xueli = p.getXueli();
			this.workage = p.getWorkage();
			this.zhicheng = p.getZhicheng();
			this.zhiyezigezheng = p.getZhiyezigezheng();
			this.hunyin = p.getHunyin();
			this.company = p.getCompany();
			this.nickName = p.getNickName();
			this.email = p.getEmail();
			this.QQ = p.getQQ();
			this.title = p.getTitle();
			this.department = p.getDepartment();
			this.icon_url = p.getIcon_url();
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
		this.s_id = json.optInt("uid");
		initUserInfo();
		if (json.has("username")){
			this.username = json.optString("username");
		}
		if (json.has("icon")){
			this.icon = json.optString("icon");
		}
		if (json.has("sex")){
			this.sex = json.optString("sex");
		}
		if (json.has("hx_reg")){
			this.hx_reg = json.optBoolean("hx_reg");
		}
		if (json.has("name")){
			this.name = json.optString("name");
		}
		if (json.has("birthday")){
			this.birthday = json.optString("birthday");
		}
		if (json.has("address")){
			this.address = json.optInt("address");
		}
		if (json.has("xueli")){
			this.xueli = json.optInt("xueli");
		}
		if (json.has("workage")){
			this.workage = json.optInt("workage");
		}
		if (json.has("zhicheng")){
			this.zhicheng = json.optString("zhicheng");
		}
		if (json.has("zhiyezigezheng")){
			this.zhiyezigezheng = json.optString("zhiyezigezheng");
		}
		if (json.has("company")){
			this.company = json.optString("company");
		}
		if (json.has("nickname")){
			this.nickName = json.optString("nickname");
		}
		if (json.has("email")){
			this.email = json.optString("email");
		}
		if (json.has("title")){
			this.title = json.optString("title");
		}
		if (json.has("department")){
			this.department = json.optString("department");
		}
		if (json.has("icon_url")){
			this.icon_url = json.optString("icon_url");
		}
		if (json.has("guanzhuprojectlist")){
			this.guanzhu_list = json.optJSONArray("guanzhuprojectlist").toString();
		}
		if (json.has("canyuprojectlist")){
			this.canyu_list = json.optJSONArray("canyuprojectlist").toString();
		}
		if (json.has("hunyin")){
			this.hunyin = json.optString("hunyin");
		}
		if (json.has("qq")){
			this.QQ = json.optString("qq");

		}
		if (json.has("hxpassword")){
			this.hxpassword = json.optString("hxpassword");
		}

	}
	public UserInfo(){

	}

}
