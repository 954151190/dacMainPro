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

import com.dacManager.www.dao.db.rowMapper.SchemeRowMapper;
import com.dacManager.www.entry.Product;
import com.dacManager.www.entry.Scheme;
import com.dacManager.www.entry.User;
import com.dacManager.www.server.ISchemeServer;
import com.dacManager.www.util.BuildSQLUtil;
import com.dacManager.www.util.QueryHelper;
import com.dacManager.www.util.StaticVariable;

public class SchemeServerImpl implements ISchemeServer{
	private static final Logger logger = LoggerFactory.getLogger(SchemeServerImpl.class);
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return this .jdbcTemplate;
	}
	
	public void setJdbcTemplate( JdbcTemplate jdbcTemplate ) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * 对外添加业务信息服务方法
	 * @param contextMap
	 */
	public Map<String,Object> saveEntryServer( Map<String,Object> contextMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取居民信息
			Scheme scheme = (Scheme)contextMap.get(StaticVariable.MS_SCHEME_OBJECT);
			//初始化ID
			scheme.setId(UUID.randomUUID().toString());
			//初始化创建时间
			scheme.setCreate_time( new Date() );
			this.saveEntry(contextMap);
		}catch(Exception ex) {
			logger.error("业务信息保存失败，失败信息",ex );
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "产品信息保存失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	} 
	
	public Map<String,Object> updateEntryServer( Map<String,Object> contextMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取业务信息
			Scheme scheme = (Scheme)contextMap.get(StaticVariable.MS_SCHEME_OBJECT);
			//判断业务信息是否存在
			Map<String,Object> tempEntryMap = this.selectEntry4ID(contextMap);
			if( null != tempEntryMap ) {
				//更新业务信息
				this.updateEntry(contextMap);
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("更新业务信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务信息更新失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public Map<String,Object> deleteEntryServer(Map<String, Object> contextMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取业务信息
			Scheme scheme = (Scheme)contextMap.get(StaticVariable.MS_SCHEME_OBJECT);
			//判断业务信息是否存在
			Map<String,Object> tempProductMap = this.selectEntry4ID(contextMap);
			if( null != tempProductMap ) {
				//删除业务信息
				boolean managerBoolean = this.deleteEntry(contextMap);
				if( managerBoolean ) {
					//执行成功
				}else{
					//执行失败
					returnMap.put(StaticVariable.MANAGER_RESULT, false);
					returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务信息删除失败，请重新操作。");
				}
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("删除业务信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务信息删除失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public void saveEntry(Map<String, Object> contextMap) throws Exception {
		Scheme scheme = (Scheme)contextMap.get(StaticVariable.MS_SCHEME_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		fields.add("TITLE");
		fields.add("CONTENT");
		fields.add("STATE");
		fields.add("CREATE_TIME");
		fields.add("AUTHOR_NAME");
		fields.add("AUTHOR_ID");
		fields.add("TYPE");
		
		values.add(scheme.getId());
		values.add(scheme.getTitle());
		values.add(scheme.getContent());
		values.add(scheme.getState());
		values.add( new java.sql.Date( scheme.getCreate_time().getTime() ) );
		values.add(scheme.getAuthor_name());
		values.add(scheme.getAuthor_id());
		values.add(scheme.getType());
		
		String insertSql = BuildSQLUtil.buildSaveSQL(StaticVariable.TABLE_NAME_SCHEME, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		QueryHelper.updateSql(conn, insertSql, values.toArray()); 
	}

	
	
	public boolean updateEntry(Map<String, Object> contextMap) throws Exception {
		Scheme scheme = (Scheme)contextMap.get(StaticVariable.MS_SCHEME_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("TITLE");
		fields.add("TYPE");
		fields.add("CONTENT");
		
		whereFields.add("ID");
		
		values.add(scheme.getTitle());
		values.add(scheme.getType());
		values.add(scheme.getContent());
		values.add(scheme.getId());
		
		String insertSql = BuildSQLUtil.buildUpdateWithConditionSQL(StaticVariable.TABLE_NAME_SCHEME, fields.toArray(), whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, insertSql, values.toArray()); 
		if(i != 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean deleteEntry(Map<String, Object> contextMap) throws Exception {
		Scheme scheme = (Scheme)contextMap.get(StaticVariable.MS_SCHEME_OBJECT);
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		whereFields.add("ID");
		values.add(scheme.getId());
		String deleteSQL = BuildSQLUtil.buildDeleteWithCondtionSQL(StaticVariable.TABLE_NAME_SCHEME, whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, deleteSQL, values.toArray());
		if( i == 1 ) {
			return true;
		}else{
			return false;
		}
	}

	private Map<String,Object> selectEntry4Login( Map<String,Object> contextMap ) throws Exception {
		User loginUser = (User)contextMap.get(StaticVariable.MS_USER_OBJECT);
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		whereFields.add("USER_NAME");
		whereFields.add("USER_PASSWORD");
		
		values.add(loginUser.getUser_name());
		values.add(loginUser.getUser_password());
		
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_USER, whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public Map<String, Object> selectEntry4ID(Map<String, Object> contextMap) throws Exception{
		Scheme scheme = (Scheme)contextMap.get(StaticVariable.MS_SCHEME_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		values.add(scheme.getId());
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_SCHEME, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public Map<String,Object> selectEntry4UserName( Map<String,Object> contextMap ) throws Exception{
		User user = (User)contextMap.get(StaticVariable.MS_USER_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("USER_NAME");
		values.add(user.getUser_name());
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_USER, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public List<Scheme> selectEntryList4Page(Map<String, Object> contextMap) {
		String sql = "select * from " + StaticVariable.TABLE_NAME_SCHEME+ "";
		List<Scheme> schemeList = jdbcTemplate.query(sql , new SchemeRowMapper());
		return schemeList;
	}
}