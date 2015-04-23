package com.dacManager.www.server.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dacManager.www.dao.db.rowMapper.BulletinRowMapper;
import com.dacManager.www.entry.Basis;
import com.dacManager.www.entry.Bulletin;
import com.dacManager.www.entry.Page;
import com.dacManager.www.entry.User;
import com.dacManager.www.server.IBasisServer;
import com.dacManager.www.util.BuildSQLUtil;
import com.dacManager.www.util.QueryHelper;
import com.dacManager.www.util.StaticVariable;

public class BasisServerImpl implements IBasisServer {
	private static final Logger logger = LoggerFactory.getLogger(BasisServerImpl.class);
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return this .jdbcTemplate;
	}
	
	public void setJdbcTemplate( JdbcTemplate jdbcTemplate ) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public BasisServerImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public Basis selectEntry(Map<String, Object> entryMap) throws Exception {
		String selectsql = BuildSQLUtil.buildSelectAllFieldsWithoutConditionSQL(StaticVariable.TABLE_NAME_BASIS);
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectsql);
		return Basis.Map2Basis(tempMap);
	}
	
	public boolean updateEntry(Map<String, Object> entryMap) throws Exception {
		Basis basis = (Basis)entryMap.get(StaticVariable.MS_BASIS_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("PHONE");
		fields.add("ADDRESS");
		fields.add("QQ");
		fields.add("WX");
		values.add(basis.getPhone());
		values.add(basis.getAddress());
		values.add(basis.getQq());
		values.add(basis.getWx());
		String updateSql = BuildSQLUtil.buildUpdateWithoutConditionSQL(StaticVariable.TABLE_NAME_BASIS, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, updateSql, values.toArray()); 
		if(i != 0) {
			return true;
		}else{
			return false;
		}
	}
}