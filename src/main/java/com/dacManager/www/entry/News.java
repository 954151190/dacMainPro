package com.dacManager.www.entry;

import java.io.Reader;
import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;
import java.util.Map;

/**
 * 农信要闻实体类
 * @author Administrator
 *
 */
public class News extends Entry implements Serializable{
	private String id;
	private String title;
	private String content;
	private Date create_time;
	private String state;
	private String author_name;
	private String author_id;
	
	/**
	 * MAP转换News
	 * @param dataMap
	 * @return
	 */
	public static News Map2Product( Map<String,Object> dataMap ){
		if( null != dataMap && !dataMap.isEmpty() ) {
			News scheme  = new News();
			if( !isEmpty(dataMap, "ID") ) {
				scheme.setId(dataMap.get("ID").toString());
			}
			if( !isEmpty(dataMap, "TITLE") ) {
				scheme.setTitle(dataMap.get("TITLE").toString());
			}
			if( !isEmpty(dataMap, "CONTENT") ) {
				scheme.setContent( clob2String( (Clob)dataMap.get("CONTENT") ));
			}
			if( !isEmpty(dataMap, "CREATE_TIME") ) {
				java.sql.Timestamp timestamp = (java.sql.Timestamp)dataMap.get("CREATE_TIME");
				java.util.Date userCreateTime = new java.util.Date( timestamp.getTime() );
				scheme.setCreate_time( userCreateTime );
			}
			if( !isEmpty(dataMap, "STATE") ) {
				scheme.setState(dataMap.get("STATE").toString());
			}
			if( !isEmpty(dataMap, "AUTHOR_NAME") ) {
				scheme.setAuthor_name(dataMap.get("AUTHOR_NAME").toString());
			}
			if( !isEmpty(dataMap, "AUTHOR_ID") ) {
				scheme.setAuthor_id(dataMap.get("AUTHOR_ID").toString());
			}
			return scheme;
		}else{
			return null;
		}
	}
	
	public static String clob2String( Clob clob ) {
		try {
			Reader inStream = clob.getCharacterStream();
			char[] c = new char[(int) clob.length()];
			inStream.read(c);
			return new String (c);
		} catch (Exception e) {
		} 	
		return "null";
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
}