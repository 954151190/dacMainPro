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

import com.dacManager.www.dao.db.rowMapper.SchemeTypeRowMapper;
import com.dacManager.www.entry.Scheme;
import com.dacManager.www.entry.SchemeType;
import com.dacManager.www.entry.User;
import com.dacManager.www.server.ISchemeTypeServer;
import com.dacManager.www.util.BuildSQLUtil;
import com.dacManager.www.util.QueryHelper;
import com.dacManager.www.util.StaticVariable;

public class SchemeTypeServerImpl implements ISchemeTypeServer{
	private static final Logger logger = LoggerFactory.getLogger(SchemeTypeServerImpl.class);
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return this .jdbcTemplate;
	}
	
	public void setJdbcTemplate( JdbcTemplate jdbcTemplate ) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * 对外添加业务类型信息服务方法
	 * @param contextMap
	 */
	public Map<String,Object> saveEntryServer( Map<String,Object> contextMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取居民信息
			SchemeType schemeType = (SchemeType)contextMap.get(StaticVariable.MS_SCHEME_TYPE_OBJECT);
			//初始化ID
			schemeType.setId(UUID.randomUUID().toString());
			//初始化创建时间
			schemeType.setCreate_time( new Date() );
			this.saveEntry(contextMap);
		}catch(Exception ex) {
			logger.error("业务类型信息保存失败，失败信息",ex );
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务类型信息保存失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	} 
	
	public Map<String,Object> updateEntryServer( Map<String,Object> contextMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取业务信息
			SchemeType schemeType = (SchemeType)contextMap.get(StaticVariable.MS_SCHEME_TYPE_OBJECT);
			//判断业务信息是否存在
			Map<String,Object> tempEntryMap = this.selectEntry4ID(contextMap);
			if( null != tempEntryMap ) {
				//更新业务信息
				this.updateEntry(contextMap);
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务类型信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("更新业务类型信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务类型信息更新失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public Map<String,Object> deleteEntryServer(Map<String, Object> contextMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取业务类型信息
			@SuppressWarnings("unused")
			SchemeType schemeType = (SchemeType)contextMap.get(StaticVariable.MS_SCHEME_TYPE_OBJECT);
			statcREV(schemeType);
			//判断业务信息是否存在
			Map<String,Object> tempProductMap = this.selectEntry4ID(contextMap);
			if( null != tempProductMap ) {
				//删除业务类型信息
				boolean managerBoolean = this.deleteEntryState(contextMap);
				if( managerBoolean ) {
					//执行成功
				}else{
					//执行失败
					returnMap.put(StaticVariable.MANAGER_RESULT, false);
					returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务类型信息删除失败，请重新操作。");
				}
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务类型信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("删除业务类型信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务类型信息删除失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public void saveEntry(Map<String, Object> contextMap) throws Exception {
		SchemeType schemeType = (SchemeType)contextMap.get(StaticVariable.MS_SCHEME_TYPE_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		fields.add("TITLE");
		fields.add("CONTENT");
		fields.add("STATE");
		fields.add("CREATE_TIME");
		fields.add("AUTHOR_NAME");
		fields.add("AUTHOR_ID");
		fields.add("IS_SHOW");
		
		values.add(schemeType.getId());
		values.add(schemeType.getTitle());
		values.add(schemeType.getContent());
		values.add(schemeType.getState());
		values.add( new java.sql.Date( schemeType.getCreate_time().getTime() ) );
		values.add(schemeType.getAuthor_name());
		values.add(schemeType.getAuthor_id());
		values.add(schemeType.getIs_show());
		
		String insertSql = BuildSQLUtil.buildSaveSQL(StaticVariable.TABLE_NAME_SCHEME_TYPE, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		QueryHelper.updateSql(conn, insertSql, values.toArray()); 
	}
	
	public void statcREV( SchemeType schemeType ) {
		if( schemeType.getState().equals("0") ) {
			schemeType.setState("1");
		}else {
			schemeType.setState("0");
		}
	}

	public boolean deleteEntryState( Map<String,Object> contextMap ) throws Exception { 
		SchemeType schemeType = (SchemeType)contextMap.get(StaticVariable.MS_SCHEME_TYPE_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("STATE");
		
		whereFields.add("ID");
		
		values.add(schemeType.getState());
		values.add(schemeType.getId());
		
		String insertSql = BuildSQLUtil.buildUpdateWithConditionSQL(StaticVariable.TABLE_NAME_SCHEME_TYPE, fields.toArray(), whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, insertSql, values.toArray()); 
		if(i != 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean updateEntry(Map<String, Object> contextMap) throws Exception {
		SchemeType schemeType = (SchemeType)contextMap.get(StaticVariable.MS_SCHEME_TYPE_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("TITLE");
		fields.add("CONTENT");
		fields.add("IS_SHOW");
		
		whereFields.add("ID");
		
		values.add(schemeType.getTitle());
		values.add(schemeType.getContent());
		values.add(schemeType.getIs_show());
		
		values.add(schemeType.getId());
		
		String insertSql = BuildSQLUtil.buildUpdateWithConditionSQL(StaticVariable.TABLE_NAME_SCHEME_TYPE, fields.toArray(), whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, insertSql, values.toArray()); 
		if(i != 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean deleteEntry(Map<String, Object> contextMap) throws Exception {
		SchemeType schemeType = (SchemeType)contextMap.get(StaticVariable.MS_SCHEME_TYPE_OBJECT);
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		whereFields.add("ID");
		values.add(schemeType.getId());
		String deleteSQL = BuildSQLUtil.buildDeleteWithCondtionSQL(StaticVariable.TABLE_NAME_SCHEME_TYPE, whereFields.toArray());
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
		SchemeType schemeType = (SchemeType)contextMap.get(StaticVariable.MS_SCHEME_TYPE_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		values.add(schemeType.getId());
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_SCHEME_TYPE, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public List<SchemeType> selectEntryList4Page(Map<String, Object> contextMap) {
		String sql = "select * from " + StaticVariable.TABLE_NAME_SCHEME_TYPE+ "";
		List<SchemeType> schemeList = jdbcTemplate.query(sql , new SchemeTypeRowMapper());
		return schemeList;
	}
	
	public List<SchemeType> selectEntryList(Map<String, Object> contextMap) throws Exception {
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithoutConditionSQL(StaticVariable.TABLE_NAME_SCHEME_TYPE);
		List<SchemeType> schemeList = jdbcTemplate.query(selectSQL , new SchemeTypeRowMapper());
		return schemeList;
	}
}