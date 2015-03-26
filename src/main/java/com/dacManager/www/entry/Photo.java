package com.dacManager.www.entry;

import java.io.Reader;
import java.sql.Clob;
import java.util.Date;
import java.util.Map;

/**
 * 图片实体类
 * @author Administrator
 *
 */
public class Photo extends Entry {
	private String id;
	private String title;
	private String state;
	private String is_index;
	private Date create_time;
	private String author_name;
	private String author_id;
	private String path;
	
	/**
	 * MAP转换Scheme
	 * @param dataMap
	 * @return
	 */
	public static Photo Map2Product( Map<String,Object> dataMap ){
		if( null != dataMap && !dataMap.isEmpty() ) {
			Photo photo  = new Photo();
			if( !isEmpty(dataMap, "ID") ) {
				photo.setId(dataMap.get("ID").toString());
			}
			if( !isEmpty(dataMap, "TITLE") ) {
				photo.setTitle(dataMap.get("TITLE").toString());
			}
			if( !isEmpty(dataMap, "CREATE_TIME") ) {
				java.sql.Timestamp timestamp = (java.sql.Timestamp)dataMap.get("CREATE_TIME");
				java.util.Date userCreateTime = new java.util.Date( timestamp.getTime() );
				photo.setCreate_time( userCreateTime );
			}
			if( !isEmpty(dataMap, "STATE") ) {
				photo.setState(dataMap.get("STATE").toString());
			}
			if( !isEmpty(dataMap, "AUTHOR_NAME") ) {
				photo.setAuthor_name(dataMap.get("AUTHOR_NAME").toString());
			}
			if( !isEmpty(dataMap, "AUTHOR_ID") ) {
				photo.setAuthor_id(dataMap.get("AUTHOR_ID").toString());
			}
			if( !isEmpty(dataMap, "aaa") ) {
				photo.setPath( dataMap.get("PATH").toString() );
			}
			return photo;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIs_index() {
		return is_index;
	}
	public void setIs_index(String is_index) {
		this.is_index = is_index;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getAuthor_name() {
		return author_name;
	}
	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}
	public String getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}