package com.dacManager.www.dao.db.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.dacManager.www.entry.Basis;


/**
 * 基础对象转换
 * 负责将数据库ResultSet对象转换为Basis对象
 * @author Administrator
 */
public class BasisMapper implements RowMapper{
	private static final Logger logger = LoggerFactory.getLogger(BasisMapper.class);
	public BasisMapper() {
	}
	
	public Basis mapRow(ResultSet rs, int rowNum) throws SQLException {
		Basis basis = new Basis();
		basis.setPhone(rs.getString("PHOTO"));
		basis.setAddress(rs.getString("ADDRESS"));
		basis.setQq(rs.getString("QQ"));
		basis.setWx(rs.getString("WX"));
		return basis;
	}
}
