package com.dacManager.www.dao.db.rowMapper;

import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.dacManager.www.entry.News;


/**
 * 农业要闻对象转换类
 * 负责将数据库ResultSet对象转换为Scheme对象
 * @author Administrator
 */
public class NewsRowMapper implements RowMapper{
	private static final Logger logger = LoggerFactory.getLogger(NewsRowMapper.class);
	public NewsRowMapper() {
	}
	
	public News mapRow(ResultSet rs, int rowNum) throws SQLException {
		News news = new News();
		news.setId( rs.getString("ID") );
		news.setTitle( rs.getString("TITLE") );
		
		Clob clob = rs.getClob("CONTENT");
		String content = clob2String(clob);
		news.setContent( content );
		
		news.setAuthor_id( rs.getString("AUTHOR_ID") );
		news.setAuthor_name( rs.getString("AUTHOR_NAME") );;
		news.setState(rs.getString("STATE"));
		news.setCreate_time(new java.util.Date( rs.getDate("CREATE_TIME").getTime() ) );
		return news;
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
