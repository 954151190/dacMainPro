package com.dacManager.www.entry;

import java.io.Serializable;
import java.util.Map;

/**
 * 基础实体类
 * @author Administrator
 */
public class Basis extends Entry implements Serializable{
	private String phone;
	private String address;
	private String qq;
	private String wx;
	/**
	 * MAP转换Basis
	 * @param Basis
	 * @return
	 */
	public static Basis Map2Basis( Map<String,Object> entryMap ){
		if( null != entryMap && !entryMap.isEmpty() ) {
			Basis basis  = new Basis();
			if( !isEmpty(entryMap, "PHONE") ) {
				basis.setPhone(entryMap.get("PHONE").toString());
			}
			if( !isEmpty(entryMap, "ADDRESS") ) {
				basis.setAddress(entryMap.get("ADDRESS").toString());
			}
			if( !isEmpty(entryMap, "QQ") ) {
				basis.setQq(entryMap.get("QQ").toString());
			}
			if( !isEmpty(entryMap, "WX") ) {
				basis.setWx(entryMap.get("WX").toString());
			}
			return basis;
		}else{
			return null;
		}
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWx() {
		return wx;
	}

	public void setWx(String wx) {
		this.wx = wx;
	}
}