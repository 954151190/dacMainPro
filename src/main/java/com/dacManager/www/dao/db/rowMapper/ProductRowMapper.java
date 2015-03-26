package com.dacManager.www.dao.db.rowMapper;

import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.dacManager.www.entry.Product;

/**
 * 产品对象转换类
 * 负责将数据库ResultSet对象转换为Product对象
 * @author Administrator
 */
public class ProductRowMapper implements RowMapper{
	private static final Logger logger = LoggerFactory.getLogger(ProductRowMapper.class);
	public ProductRowMapper() {
	}
	
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		Product product = new Product();
		product.setId( rs.getString("ID") );
		product.setTitle( rs.getString("TITLE") );
		
		Clob clob = rs.getClob("CONTENT");
		String content = clob2String(clob);
		product.setContent( content );
		
		product.setAuthor_id( rs.getString("AUTHOR_ID") );
		product.setAuthor_name( rs.getString("AUTHOR_NAME") );;
		product.setState(rs.getString("STATE"));
		product.setCreate_time(new java.util.Date( rs.getDate("CREATE_TIME").getTime() ) );
		product.setPhoto_id( rs.getString("PHOTO_ID"));
		return product;
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
