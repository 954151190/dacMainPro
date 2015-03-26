package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

import com.dacManager.www.entry.Bulletin;

public interface IBulletinServer {
	/**
	 * ���ӹ�ʾ����
	 * @param bulletinMap
	 */
	public void saveBulletin( Map<String,Object> bulletinMap ) throws Exception;
	
	/**
	 * ������ӹ�ʾ������Ϣ���񷽷�
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> saveBulletinServer( Map<String,Object> bulletinMap );
	
	/**
	 * ������¹�ʾ������Ϣ���񷽷�
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> updateBulletinServer( Map<String,Object> bulletinMap );
	
	/**
	 * ����ɾ����ʾ������񷽷�
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> deleteBulletinServer( Map<String,Object> bulletinMap );
	
	/**
	 * ���¹�ʾ����
	 * @param bulletinMap
	 * @return
	 */
	public boolean updateBulletin( Map<String,Object> bulletinMap ) throws Exception;
	
	/**
	 * ɾ����ʾ����
	 * @param bulletinMap
	 * @return
	 */
	public boolean deleteBulletin( Map<String,Object> bulletinMap ) throws Exception ;
	
	/**
	 * ���ҹ�ʾ�������ID
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> selectBulletin4ID( Map<String,Object> bulletinMap ) throws Exception; 
	
	/**
	 * ��ҳ��ѯ��ʾ������Ϣ����
	 * @param bulletinMap
	 * @return
	 */
	public List<Bulletin> selectBulletinList4Page( Map<String,Object> bulletinMap );
}
