package com.dacManager.www.dao.db.rowMapper;

import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.dacManager.www.entry.Scheme;

/**
 * 产品对象转换类
 * 负责将数据库ResultSet对象转换为SchemeType对象
 * @author Administrator
 */
public class SchemeRowMapper implements RowMapper{
	private static final Logger logger = LoggerFactory.getLogger(SchemeRowMapper.class);
	public SchemeRowMapper() {
	}
	
	public Scheme mapRow(ResultSet rs, int rowNum) throws SQLException {
		Scheme scheme = new Scheme();
		scheme.setId( rs.getString("ID") );
		scheme.setTitle( rs.getString("TITLE") );
		scheme.setType( rs.getString("TYPE") );
		Clob clob = rs.getClob("CONTENT");
		String content = clob2String(clob);
		scheme.setContent( content );
		
		scheme.setAuthor_id( rs.getString("AUTHOR_ID") );
		scheme.setAuthor_name( rs.getString("AUTHOR_NAME") );;
		scheme.setState(rs.getString("STATE"));
		scheme.setCreate_time(new java.util.Date( rs.getDate("CREATE_TIME").getTime() ) );
		return scheme;
	}
	
	public String clob2String( Clob clob ) {
		try {
			Reader inStream = clob.getCharacterStream();
			char[] c = new char[(int) clob.length()];
			inStream.read(c);
			return new String (c);
		} catch (Exception e) {
			logger.error("读取clob类型信息失败！",e);
		} 	
		return "null";
	}
}
