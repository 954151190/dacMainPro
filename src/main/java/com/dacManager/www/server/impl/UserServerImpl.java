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

import com.dacManager.www.dao.db.rowMapper.UserRowMapper;
import com.dacManager.www.entry.User;
import com.dacManager.www.server.IUserServer;
import com.dacManager.www.util.BuildSQLUtil;
import com.dacManager.www.util.QueryHelper;
import com.dacManager.www.util.StaticVariable;

public class UserServerImpl implements IUserServer {
	private static final Logger logger = LoggerFactory.getLogger(UserServerImpl.class);
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return this .jdbcTemplate;
	}
	
	public void setJdbcTemplate( JdbcTemplate jdbcTemplate ) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * 对外登录方法
	 */
	public Map<String, Object> loginServer(Map<String, Object> userMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try{
			//获取登录信息
			User loginUser = (User)userMap.get(StaticVariable.MS_USER_OBJECT);
			//查找是否存在相应用户
			Map<String,Object> userObjectMap = selectUser4Login(userMap);
			if( null == userObjectMap ) {
				//用户不存在
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "登录失败,登录名或密码错误。");
			}else{
				//用户存在
				returnMap.put(StaticVariable.MANAGER_RESULT, true);
			}
		}catch(Exception ex) {
			logger.error("用户登录异常，异常信息。" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "登录失败，请重新登录。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}

	/**
	 * 对外添加用户信息服务方法
	 * @param userMap
	 */
	public Map<String,Object> saveUserServer( Map<String,Object> userMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取居民信息
			User user = (User)userMap.get(StaticVariable.MS_USER_OBJECT);
			//判断居民信息是否存在
			Map<String,Object> tempUserMap = this.selectUser4UserName(userMap);
			if( null != tempUserMap ) {
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "用户信息已存在，请重新操作。");
			}else{
				//初始化ID
				user.setId(UUID.randomUUID().toString());
				//初始化创建时间
				user.setCreate_time( new Date() );
				this.saveUser(userMap);
			}
		}catch(Exception ex) {
			logger.error("用户信息保存失败，失败信息",ex );
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "用户信息保存失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	} 
	
	public Map<String,Object> updateUserServer( Map<String,Object> userMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取居民信息
			User user = (User)userMap.get(StaticVariable.MS_USER_OBJECT);
			//判断居民信息是否存在
			Map<String,Object> tempUserMap = this.selectUser4ID(userMap);
			if( null != tempUserMap ) {
				//更新用户信息
				this.updateUser(userMap);
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "用户信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("更新用户信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "用户信息更新失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public Map<String,Object> deleteUserServer(Map<String, Object> userMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取居民信息
			User user = (User)userMap.get(StaticVariable.MS_USER_OBJECT);
			//判断居民信息是否存在
			Map<String,Object> tempUserMap = this.selectUser4ID(userMap);
			if( null != tempUserMap ) {
				//删除用户信息
				boolean managerBoolean = this.deleteUser(userMap);
				if( managerBoolean ) {
					//执行成功
				}else{
					//执行失败
					returnMap.put(StaticVariable.MANAGER_RESULT, false);
					returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "用户信息删除失败，请重新操作。");
				}
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "用户信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("删除用户信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "用户信息删除失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public void saveUser(Map<String, Object> userMap) throws Exception {
		User user = (User)userMap.get(StaticVariable.MS_USER_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		fields.add("NAME");
		fields.add("USER_NAME");
		fields.add("USER_PASSWORD");
		fields.add("USER_ROLE");
		fields.add("AGE");
		fields.add("REMARK");
		fields.add("STATE");
		fields.add("CREATE_TIME");
		
		values.add(user.getId());
		values.add(user.getName());
		values.add(user.getUser_name());
		values.add(user.getUser_password());
		values.add(user.getUser_role());
		values.add(user.getAge());
		values.add(user.getRemark());
		values.add(user.getState());
		values.add( new java.sql.Date( user.getCreate_time().getTime() ) );
		
		String insertSql = BuildSQLUtil.buildSaveSQL(StaticVariable.TABLE_NAME_USER, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		QueryHelper.updateSql(conn, insertSql, values.toArray()); 
	}

	
	
	public boolean updateUser(Map<String, Object> userMap) throws Exception {
		User user = (User)userMap.get(StaticVariable.MS_USER_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("NAME");
		fields.add("USER_NAME");
		fields.add("USER_PASSWORD");
		fields.add("USER_ROLE");
		fields.add("AGE");
		fields.add("REMARK");
		fields.add("STATE");
		fields.add("CREATE_TIME");
		
		whereFields.add("ID");
		
		values.add(user.getName());
		values.add(user.getUser_name());
		values.add(user.getUser_password());
		values.add(user.getUser_role());
		values.add(user.getAge());
		values.add(user.getRemark());
		values.add(user.getState());
		values.add( new java.sql.Date( user.getCreate_time().getTime() ) );
		values.add(user.getId());
		
		String insertSql = BuildSQLUtil.buildUpdateWithConditionSQL(StaticVariable.TABLE_NAME_USER, fields.toArray(), whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, insertSql, values.toArray()); 
		if(i != 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean deleteUser(Map<String, Object> userMap) throws Exception {
		User user = (User)userMap.get(StaticVariable.MS_USER_OBJECT);
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		whereFields.add("ID");
		values.add(user.getId());
		String deleteSQL = BuildSQLUtil.buildDeleteWithCondtionSQL(StaticVariable.TABLE_NAME_USER, whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, deleteSQL, values.toArray());
		if( i == 1 ) {
			return true;
		}else{
			return false;
		}
	}

	private Map<String,Object> selectUser4Login( Map<String,Object> userMap ) throws Exception {
		User loginUser = (User)userMap.get(StaticVariable.MS_USER_OBJECT);
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
	
	public Map<String, Object> selectUser4ID(Map<String, Object> userMap) throws Exception{
		User user = (User)userMap.get(StaticVariable.MS_USER_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		values.add(user.getId());
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_USER, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public Map<String,Object> selectUser4UserName( Map<String,Object> userMap ) throws Exception{
		User user = (User)userMap.get(StaticVariable.MS_USER_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("USER_NAME");
		values.add(user.getUser_name());
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_USER, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public List<User> selectUserList4Page(
			Map<String, Object> userMap) {
		String sql = "select * from " + StaticVariable.TABLE_NAME_USER + "";
		List<User> userList = jdbcTemplate.query(sql , new UserRowMapper());
		return userList;
	}
}