package com.dacManager.www.entry;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户实体类
 * @author Administrator
 *
 */
public class User extends Entry implements Serializable{
	private String id;
	private String user_name;
	private String user_password;
	private String state;
	private String name;
	private String age;
	private String user_role;
	private Date create_time;
	private String remark;
	
	public User() {
	}

	/**
	 * MAP转USER
	 * @param userMap
	 * @return
	 */
	public static User Map2User (  Map<String,Object> userMap ) {
		if( null != userMap && !userMap.isEmpty() ) {
			User user = new User();
			if( !isEmpty(userMap, "ID") ) {
				user.setId(userMap.get("ID").toString());
			}
			if( !isEmpty(userMap, "USER_NAME") ) {
				user.setUser_name( userMap.get("USER_NAME").toString() );
			}
			if( !isEmpty(userMap, "USER_PASSWORD") ) {
				user.setUser_password( userMap.get("USER_PASSWORD").toString() );
			}
			if( !isEmpty(userMap, "STATE") ) {
				user.setState( userMap.get("STATE").toString() );
			}
			if( !isEmpty(userMap, "NAME") ) {
				user.setName( userMap.get("NAME").toString() );
			}
			if( !isEmpty(userMap, "AGE") ) {
				user.setAge( userMap.get("AGE").toString() );
			}
			if( !isEmpty(userMap, "USER_ROLE") ) {
				user.setUser_role( userMap.get("USER_ROLE").toString() );
			}
			if( !isEmpty(userMap, "CREATE_TIME") ) {
				java.sql.Timestamp timestamp = (java.sql.Timestamp)userMap.get("CREATE_TIME");
				java.util.Date userCreateTime = new java.util.Date( timestamp.getTime() );
				user.setCreate_time( userCreateTime );
			}
			if( !isEmpty(userMap, "REMARK") ) {
				user.setRemark( userMap.get("REMARK").toString() );
			}
			return user;
		}else{
			return null;
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getUser_role() {
		return user_role;
	}
	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}