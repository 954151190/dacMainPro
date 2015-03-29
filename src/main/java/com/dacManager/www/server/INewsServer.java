package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

import com.dacManager.www.entry.News;

public interface INewsServer {
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
	 * 对外更新产品信息服务方法
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> updateEntryServer( Map<String,Object> contextMap );
	
	/**
	 * 对外删除产品服务方法
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> deleteEntryServer( Map<String,Object> contextMap );
	
	/**
	 * 更新产品
	 * @param bulletinMap
	 * @return
	 */
	public boolean updateEntry( Map<String,Object> contextMap ) throws Exception;
	
	/**
	 * 删除产品
	 * @param bulletinMap
	 * @return
	 */
	public boolean deleteEntry( Map<String,Object> contextMap ) throws Exception ;
	
	/**
	 * 查找产品根据ID
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> selectEntry4ID( Map<String,Object> contextMap ) throws Exception; 
	
	/**
	 * 分页查询产品信息集合
	 * @param bulletinMap
	 * @return
	 */
	public List<News> selectEntryList4Page( Map<String,Object> contextMap );
	
	/**
	 * 查询信息个数
	 * @param contextMap
	 */
	public Long countEntry( Map<String,Object> contextMap );
}
