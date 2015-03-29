package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

import com.dacManager.www.entry.Photo;

public interface IPhotoServer {
	/**
	 * 增加图片
	 * @param bulletinMap
	 */
	public void saveEntry( Map<String,Object> contextMap ) throws Exception;
	
	/**
	 * 对外添加图片信息服务方法
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> saveEntryServer( Map<String,Object> contextMap );

	/**
	 * 对外添加图片对象服务方法
	 * @param contextMap
	 * @return
	 */
	public Map<String,Object> saveFileServer( Map<String,Object> contextMap );
	
	/**
	 * 对外更新图片信息服务方法
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> updateEntryServer( Map<String,Object> contextMap );
	
	/**
	 * 对外删除图片服务方法
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> deleteEntryServer( Map<String,Object> contextMap );
	
	/**
	 * 对外删除图片文件服务方法
	 * @param contextMap
	 * @return
	 */
	public Map<String,Object> deleteFileServer( Map<String,Object> contextMap );
	
	/**
	 * 更新图片
	 * @param bulletinMap
	 * @return
	 */
	public boolean updateEntry( Map<String,Object> contextMap ) throws Exception;
	
	/**
	 * 删除图片
	 * @param bulletinMap
	 * @return
	 */
	public boolean deleteEntry( Map<String,Object> contextMap ) throws Exception ;
	
	/**
	 * 查找图片根据ID
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> selectEntry4ID( Map<String,Object> contextMap ) throws Exception; 
	
	/**
	 * 分页查询图片信息集合
	 * @param bulletinMap
	 * @return
	 */
	public List<Photo> selectEntryList4Page( Map<String,Object> contextMap );
	
	/**
	 * 查询总数
	 */
	public Long countEntry( Map<String,Object> contextMap );
	
}
