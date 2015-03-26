package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

import com.dacManager.www.entry.Scheme;

public interface ISchemeServer {
	/**
	 * ���Ӳ�Ʒ
	 * @param bulletinMap
	 */
	public void saveEntry( Map<String,Object> contextMap ) throws Exception;
	
	/**
	 * ������Ӳ�Ʒ��Ϣ���񷽷�
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> saveEntryServer( Map<String,Object> contextMap );
	
	/**
	 * ������²�Ʒ��Ϣ���񷽷�
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> updateEntryServer( Map<String,Object> contextMap );
	
	/**
	 * ����ɾ����Ʒ���񷽷�
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> deleteEntryServer( Map<String,Object> contextMap );
	
	/**
	 * ���²�Ʒ
	 * @param bulletinMap
	 * @return
	 */
	public boolean updateEntry( Map<String,Object> contextMap ) throws Exception;
	
	/**
	 * ɾ����Ʒ
	 * @param bulletinMap
	 * @return
	 */
	public boolean deleteEntry( Map<String,Object> contextMap ) throws Exception ;
	
	/**
	 * ���Ҳ�Ʒ����ID
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> selectEntry4ID( Map<String,Object> contextMap ) throws Exception; 
	
	/**
	 * ��ҳ��ѯ��Ʒ��Ϣ����
	 * @param bulletinMap
	 * @return
	 */
	public List<Scheme> selectEntryList4Page( Map<String,Object> contextMap );
}
