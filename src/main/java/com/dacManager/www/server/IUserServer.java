package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

import com.dacManager.www.entry.User;

public interface IUserServer {
	/**
	 * 登陆服务
	 * @return
	 */
	public Map<String,Object> loginServer( Map<String,Object> userMap );
	
	/**
	 * 增加用户
	 * @param UserMap
	 */
	public void saveUser( Map<String,Object> userMap ) throws Exception;
	
	/**
	 * 对外添加用户信息服务方法
	 * @param userMap
	 * @return
	 */
	public Map<String,Object> saveUserServer( Map<String,Object> userMap );
	
	/**
	 * 对外更新用户信息服务方法
	 * @param userMap
	 * @return
	 */
	public Map<String,Object> updateUserServer( Map<String,Object> userMap );
	
	/**
	 * 对外删除用户服务方法
	 * @param userMap
	 * @return
	 */
	public Map<String,Object> deleteUserServer( Map<String,Object> userMap );
	
	/**
	 * 更新用户
	 * @param userMap
	 * @return
	 */
	public boolean updateUser( Map<String,Object> userMap ) throws Exception;
	
	/**
	 * 删除用户
	 * @param userMap
	 * @return
	 */
	public boolean deleteUser( Map<String,Object> userMap ) throws Exception ;
	
	/**
	 * 查找用户根据ID
	 * @param userMap
	 * @return
	 */
	public Map<String,Object> selectUser4ID( Map<String,Object> userMap ) throws Exception; 
	
	/**
	 * 分页查询用户信息集合
	 * @param userMap
	 * @return
	 */
	public List<User> selectUserList4Page( Map<String,Object> userMap );
	
	/**
	 * 查询信息个数
	 * @param contextMap
	 */
	public Long countEntry( Map<String,Object> contextMap );
}
