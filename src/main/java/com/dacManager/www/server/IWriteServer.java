package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

/**
 * ���·�����
 * @author Administrator
 *
 */
public interface IWriteServer {
	/**
	 * ��������
	 * @param writeMap
	 * @return
	 */
	public boolean saveWrite( Map<String,Object> writeMap );
	
	/**
	 * ��������
	 * @param writeMap
	 * @return
	 */
	public boolean updateWrite( Map<String,Object> writeMap );
	
	/**
	 * ɾ������
	 * @param writeMap
	 * @return
	 */
	public boolean deleteWrite( Map<String,Object> writeMap );
	
	/**
	 * ���������б������������
	 * @param writeMap
	 * @return
	 */
	public List<Map<String,Object>> selectWriteList4Type( Map<String,Object> writeMap );
	
	/**
	 * �������¸�������ID
	 * @param wirteMap
	 * @return
	 */
	public Map<String,Object> selectWrite4ID( Map<String,Object> wirteMap );
}
