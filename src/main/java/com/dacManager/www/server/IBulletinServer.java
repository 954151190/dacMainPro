package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

import com.dacManager.www.entry.Bulletin;

public interface IBulletinServer {
	/**
	 * 增加公示公告
	 * @param bulletinMap
	 */
	public void saveBulletin( Map<String,Object> bulletinMap ) throws Exception;
	
	/**
	 * 对外添加公示公告信息服务方法
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> saveBulletinServer( Map<String,Object> bulletinMap );
	
	/**
	 * 对外更新公示公告信息服务方法
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> updateBulletinServer( Map<String,Object> bulletinMap );
	
	/**
	 * 对外删除公示公告服务方法
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> deleteBulletinServer( Map<String,Object> bulletinMap );
	
	/**
	 * 更新公示公告
	 * @param bulletinMap
	 * @return
	 */
	public boolean updateBulletin( Map<String,Object> bulletinMap ) throws Exception;
	
	/**
	 * 删除公示公告
	 * @param bulletinMap
	 * @return
	 */
	public boolean deleteBulletin( Map<String,Object> bulletinMap ) throws Exception ;
	
	/**
	 * 查找公示公告根据ID
	 * @param bulletinMap
	 * @return
	 */
	public Map<String,Object> selectBulletin4ID( Map<String,Object> bulletinMap ) throws Exception; 
	
	/**
	 * 分页查询公示公告信息集合
	 * @param bulletinMap
	 * @return
	 */
	public List<Bulletin> selectBulletinList4Page( Map<String,Object> bulletinMap );
}
