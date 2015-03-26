package com.dacManager.www.dao.db.rowMapper;

import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.dacManager.www.entry.Bulletin;

/**
 * ��ʾ�������ת����
 * �������ݿ�ResultSet����ת��ΪBulletin����
 * @author Administrator
 */
public class BulletinRowMapper implements RowMapper{
	private static final Logger logger = LoggerFactory.getLogger(BulletinRowMapper.class);
	public BulletinRowMapper() {
	}
	
	public Bulletin mapRow(ResultSet rs, int rowNum) throws SQLException {
		Bulletin bulletin = new Bulletin();
		bulletin.setId( rs.getString("ID") );
		bulletin.setTitle( rs.getString("TITLE") );
		
		Clob clob = rs.getClob("CONTENT");
		String content = clob2String(clob);
		bulletin.setContent( content );
		
		bulletin.setAuthor_id( rs.getString("AUTHOR_ID") );
		bulletin.setAuthor_name( rs.getString("AUTHOR_NAME") );;
		bulletin.setState(rs.getString("STATE"));
		bulletin.setCreate_time(new java.util.Date( rs.getDate("CREATE_TIME").getTime() ) );
		return bulletin;
	}
	
	public String clob2String( Clob clob ) {
		try {
			Reader inStream = clob.getCharacterStream();
			char[] c = new char[(int) clob.length()];
			inStream.read(c);
			return new String (c);
		} catch (Exception e) {
			logger.error("��ȡclob������Ϣʧ�ܣ�",e);
		} 	
		return "null";
	}
} 
