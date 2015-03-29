package com.dacManager.www.server.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
import com.dacManager.www.entry.Bulletin;
import com.dacManager.www.entry.Page;
import com.dacManager.www.entry.User;
import com.dacManager.www.server.IBulletinServer;
import com.dacManager.www.util.BuildSQLUtil;
import com.dacManager.www.util.QueryHelper;
import com.dacManager.www.util.StaticVariable;

public class BulletinServerImpl implements IBulletinServer {
	private static final Logger logger = LoggerFactory.getLogger(BulletinServerImpl.class);
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return this .jdbcTemplate;
	}
	
	public void setJdbcTemplate( JdbcTemplate jdbcTemplate ) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * 对外添加公示公告信息服务方法
	 * @param contextMap
	 */
	public Map<String,Object> saveBulletinServer( Map<String,Object> contextMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取居民信息
			Bulletin bulletin = (Bulletin)contextMap.get(StaticVariable.MS_BULLETIN_OBJECT);
			//初始化ID
			bulletin.setId(UUID.randomUUID().toString());
			//初始化创建时间
			bulletin.setCreate_time( new Date() );
			this.saveBulletin(contextMap);
		}catch(Exception ex) {
			logger.error("公示公告信息保存失败，失败信息",ex );
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "公示公告信息保存失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	} 
	
	public Map<String,Object> updateBulletinServer( Map<String,Object> bulletinMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取公示公告信息
			Bulletin bulletin = (Bulletin)bulletinMap.get(StaticVariable.MS_BULLETIN_OBJECT);
			//判断公示公告信息是否存在
			Map<String,Object> tempBulletinMap = this.selectBulletin4ID(bulletinMap);
			if( null != tempBulletinMap ) {
				//更新公示公告信息
				this.updateBulletin(bulletinMap);
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "公示公告信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("更新公示公告信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "公示公告信息更新失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public Map<String,Object> deleteBulletinServer(Map<String, Object> contextMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取公示公告信息
			Bulletin bulletin = (Bulletin)contextMap.get(StaticVariable.MS_BULLETIN_OBJECT);
			//判断公示公告信息是否存在
			Map<String,Object> tempBulletinMap = this.selectBulletin4ID(contextMap);
			if( null != tempBulletinMap ) {
				//删除公示公告信息
				boolean managerBoolean = this.deleteBulletin(contextMap);
				if( managerBoolean ) {
					//执行成功
				}else{
					//执行失败
					returnMap.put(StaticVariable.MANAGER_RESULT, false);
					returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "公示公告信息删除失败，请重新操作。");
				}
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "公示公告信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("删除公示公告信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "公示公告信息删除失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public void saveBulletin(Map<String, Object> bulletinMap) throws Exception {
		Bulletin bulletin = (Bulletin)bulletinMap.get(StaticVariable.MS_BULLETIN_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		fields.add("TITLE");
		fields.add("CONTENT");
		fields.add("STATE");
		fields.add("CREATE_TIME");
		fields.add("AUTHOR_NAME");
		fields.add("AUTHOR_ID");
		
		values.add(bulletin.getId());
		values.add(bulletin.getTitle());
		values.add(bulletin.getContent());
		values.add(bulletin.getState());
		values.add( new java.sql.Date( bulletin.getCreate_time().getTime() ) );
		values.add(bulletin.getAuthor_name());
		values.add(bulletin.getAuthor_id());
		
		String insertSql = BuildSQLUtil.buildSaveSQL(StaticVariable.TABLE_NAME_BULLETIN, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		QueryHelper.updateSql(conn, insertSql, values.toArray()); 
	}

	
	
	public boolean updateBulletin(Map<String, Object> bulletinMap) throws Exception {
		Bulletin bulletin = (Bulletin)bulletinMap.get(StaticVariable.MS_BULLETIN_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("TITLE");
		fields.add("CONTENT");
		
		whereFields.add("ID");
		
		values.add(bulletin.getTitle());
		values.add(bulletin.getContent());
		values.add(bulletin.getId());
		
		String insertSql = BuildSQLUtil.buildUpdateWithConditionSQL(StaticVariable.TABLE_NAME_BULLETIN, fields.toArray(), whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, insertSql, values.toArray()); 
		if(i != 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean deleteBulletin(Map<String, Object> bulletinMap) throws Exception {
		Bulletin bulletin = (Bulletin)bulletinMap.get(StaticVariable.MS_BULLETIN_OBJECT);
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		whereFields.add("ID");
		values.add(bulletin.getId());
		String deleteSQL = BuildSQLUtil.buildDeleteWithCondtionSQL(StaticVariable.TABLE_NAME_BULLETIN, whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, deleteSQL, values.toArray());
		if( i == 1 ) {
			return true;
		}else{
			return false;
		}
	}

	private Map<String,Object> selectBulletin4Login( Map<String,Object> bulletinMap ) throws Exception {
		User loginUser = (User)bulletinMap.get(StaticVariable.MS_USER_OBJECT);
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
	
	public Map<String, Object> selectBulletin4ID(Map<String, Object> bulletinMap) throws Exception{
		Bulletin bulletin = (Bulletin)bulletinMap.get(StaticVariable.MS_BULLETIN_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		values.add(bulletin.getId());
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_BULLETIN, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public Map<String,Object> selectBulletin4UserName( Map<String,Object> bulletinMap ) throws Exception{
		User user = (User)bulletinMap.get(StaticVariable.MS_USER_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("USER_NAME");
		values.add(user.getUser_name());
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_USER, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public List<Bulletin> selectBulletinList4Page(Map<String, Object> contextMap) {
		Page page = (Page)contextMap.get(StaticVariable.PAGE_BULLETIN);
		//计算最大序号
		int numberMax = page.getCount() * page.getNumber();
		//计算最小序号
		int numberMin = (page.getNumber()-1) * page.getCount();
		String sql = "SELECT * FROM ( SELECT A.*, ROWNUM RN FROM ( SELECT * FROM " + StaticVariable.TABLE_NAME_BULLETIN + " ) A WHERE ROWNUM <= "+numberMax+" ) WHERE RN > "+numberMin+"";
		List<Bulletin> bulletinList = jdbcTemplate.query(sql , new BulletinRowMapper());
		return bulletinList;
	}
	
	/**
	 * 查询信息个数
	 * @param contextMap
	 */
	public Long countEntry( Map<String,Object> contextMap ){
		String countStr = BuildSQLUtil.buildCountAllSQL( StaticVariable.TABLE_NAME_BULLETIN );
		Long l = jdbcTemplate.queryForLong(countStr);
		return l;
	}
}