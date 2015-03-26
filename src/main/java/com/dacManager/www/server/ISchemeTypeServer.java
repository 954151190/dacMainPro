package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

import com.dacManager.www.entry.SchemeType;

public interface ISchemeTypeServer {
	/**
	 * 增加产品
	 * @param bulletinMap
	 */
	public void saveEntry( Map<String,Object> contextMap ) throws Exception;
	
	/**
	 * 对外添加产品信息服务方法
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> saveEntryServer( Map<String,Object> contextMap );
	
	/**
	 * 对外更新业务信息服务方法
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> updateEntryServer( Map<String,Object> contextMap );
	
	/**
	 * 对外删除业务服务方法
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> deleteEntryServer( Map<String,Object> contextMap );
	
	/**
	 * 更新业务
	 * @param bulletinMap
	 * @return
	 */
	public boolean updateEntry( Map<String,Object> contextMap ) throws Exception;
	
	/**
	 * 删除业务
	 * @param bulletinMap
	 * @return
	 */
	public boolean deleteEntry( Map<String,Object> contextMap ) throws Exception ;
	
	/**
	 * 查找业务根据ID
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> selectEntry4ID( Map<String,Object> contextMap ) throws Exception; 
	
	/**
	 * 分页查询业务信息集合
	 * @param bulletinMap
	 * @return
	 */
	public List<SchemeType> selectEntryList4Page( Map<String,Object> contextMap );
	
	/**
	 * 查询业务类型信息集合
	 * @param contextMap
	 * @return
	 */
	public List<SchemeType> selectEntryList( Map<String,Object> contextMap ) throws Exception;
}
