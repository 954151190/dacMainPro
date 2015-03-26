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

import com.dacManager.www.dao.db.rowMapper.NewsRowMapper;
import com.dacManager.www.entry.News;
import com.dacManager.www.entry.Scheme;
import com.dacManager.www.entry.User;
import com.dacManager.www.server.INewsServer;
import com.dacManager.www.util.BuildSQLUtil;
import com.dacManager.www.util.QueryHelper;
import com.dacManager.www.util.StaticVariable;

public class NewsServerImpl implements INewsServer{
	private static final Logger logger = LoggerFactory.getLogger(NewsServerImpl.class);
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
			News news = (News)contextMap.get(StaticVariable.MS_NEWS_OBJECT);
			//初始化ID
			news.setId(UUID.randomUUID().toString());
			//初始化创建时间
			news.setCreate_time( new Date() );
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
			News news = (News)contextMap.get(StaticVariable.MS_NEWS_OBJECT);
			//判断业务信息是否存在
			Map<String,Object> tempEntryMap = this.selectEntry4ID(contextMap);
			if( null != tempEntryMap ) {
				//更新业务信息
				this.updateEntry(contextMap);
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "农信要闻信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("更新业务信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "农信要闻信息更新失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public Map<String,Object> deleteEntryServer(Map<String, Object> contextMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取农信要闻信息
			News news = (News)contextMap.get(StaticVariable.MS_NEWS_OBJECT);
			//判断农信要闻信息是否存在
			Map<String,Object> tempProductMap = this.selectEntry4ID(contextMap);
			if( null != tempProductMap ) {
				//删除农信要闻信息
				boolean managerBoolean = this.deleteEntry(contextMap);
				if( managerBoolean ) {
					//执行成功
				}else{
					//执行失败
					returnMap.put(StaticVariable.MANAGER_RESULT, false);
					returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "农信要闻信息删除失败，请重新操作。");
				}
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "农信要闻信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("删除农信要闻信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "农信要闻信息删除失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public void saveEntry(Map<String, Object> contextMap) throws Exception {
		News news = (News)contextMap.get(StaticVariable.MS_NEWS_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		fields.add("TITLE");
		fields.add("CONTENT");
		fields.add("STATE");
		fields.add("CREATE_TIME");
		fields.add("AUTHOR_NAME");
		fields.add("AUTHOR_ID");
		
		values.add(news.getId());
		values.add(news.getTitle());
		values.add(news.getContent());
		values.add(news.getState());
		values.add( new java.sql.Date( news.getCreate_time().getTime() ) );
		values.add(news.getAuthor_name());
		values.add(news.getAuthor_id());
		
		String insertSql = BuildSQLUtil.buildSaveSQL(StaticVariable.TABLE_NAME_NEWS, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		QueryHelper.updateSql(conn, insertSql, values.toArray()); 
	}

	
	
	public boolean updateEntry(Map<String, Object> contextMap) throws Exception {
		News news = (News)contextMap.get(StaticVariable.MS_NEWS_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("TITLE");
		fields.add("CONTENT");
		
		whereFields.add("ID");
		
		values.add(news.getTitle());
		values.add(news.getContent());
		values.add(news.getId());
		
		String insertSql = BuildSQLUtil.buildUpdateWithConditionSQL(StaticVariable.TABLE_NAME_NEWS, fields.toArray(), whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, insertSql, values.toArray()); 
		if(i != 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean deleteEntry(Map<String, Object> contextMap) throws Exception {
		News news = (News)contextMap.get(StaticVariable.MS_NEWS_OBJECT);
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		whereFields.add("ID");
		values.add(news.getId());
		String deleteSQL = BuildSQLUtil.buildDeleteWithCondtionSQL(StaticVariable.TABLE_NAME_NEWS, whereFields.toArray());
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
		News news = (News)contextMap.get(StaticVariable.MS_NEWS_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		values.add(news.getId());
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_NEWS, fields.toArray());
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
	
	public List<News> selectEntryList4Page(Map<String, Object> contextMap) {
		String sql = "select * from " + StaticVariable.TABLE_NAME_NEWS+ "";
		List<News> newsList = jdbcTemplate.query(sql , new NewsRowMapper());
		return newsList;
	}
}