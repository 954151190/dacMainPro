package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

/**
 * 文章服务类
 * @author Administrator
 *
 */
public interface IWriteServer {
	/**
	 * 保存文章
	 * @param writeMap
	 * @return
	 */
	public boolean saveWrite( Map<String,Object> writeMap );
	
	/**
	 * 更新文章
	 * @param writeMap
	 * @return
	 */
	public boolean updateWrite( Map<String,Object> writeMap );
	
	/**
	 * 删除文章
	 * @param writeMap
	 * @return
	 */
	public boolean deleteWrite( Map<String,Object> writeMap );
	
	/**
	 * 查找文章列表根据文章类型
	 * @param writeMap
	 * @return
	 */
	public List<Map<String,Object>> selectWriteList4Type( Map<String,Object> writeMap );
	
	/**
	 * 查找文章根据文章ID
	 * @param wirteMap
	 * @return
	 */
	public Map<String,Object> selectWrite4ID( Map<String,Object> wirteMap );
}
