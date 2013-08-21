package com.wj.sell.db.models;

import java.io.Serializable;


public class WenDangImage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 22L;
	private int id;
	private String url;
	private int index;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	

	
	
}
