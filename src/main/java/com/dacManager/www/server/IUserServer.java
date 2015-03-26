package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

import com.dacManager.www.entry.User;

public interface IUserServer {
	/**
	 * ��½����
	 * @return
	 */
	public Map<String,Object> loginServer( Map<String,Object> userMap );
	
	/**
	 * �����û�
	 * @param UserMap
	 */
	public void saveUser( Map<String,Object> userMap ) throws Exception;
	
	/**
	 * ��������û���Ϣ���񷽷�
	 * @param userMap
	 * @return
	 */
	public Map<String,Object> saveUserServer( Map<String,Object> userMap );
	
	/**
	 * ��������û���Ϣ���񷽷�
	 * @param userMap
	 * @return
	 */
	public Map<String,Object> updateUserServer( Map<String,Object> userMap );
	
	/**
	 * ����ɾ���û����񷽷�
	 * @param userMap
	 * @return
	 */
	public Map<String,Object> deleteUserServer( Map<String,Object> userMap );
	
	/**
	 * �����û�
	 * @param userMap
	 * @return
	 */
	public boolean updateUser( Map<String,Object> userMap ) throws Exception;
	
	/**
	 * ɾ���û�
	 * @param userMap
	 * @return
	 */
	public boolean deleteUser( Map<String,Object> userMap ) throws Exception ;
	
	/**
	 * �����û�����ID
	 * @param userMap
	 * @return
	 */
	public Map<String,Object> selectUser4ID( Map<String,Object> userMap ) throws Exception; 
	
	/**
	 * ��ҳ��ѯ�û���Ϣ����
	 * @param userMap
	 * @return
	 */
	public List<User> selectUserList4Page( Map<String,Object> userMap );
}
