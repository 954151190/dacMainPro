package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

import com.dacManager.www.entry.Basis;
import com.dacManager.www.entry.Bulletin;

public interface IBasisServer {
	
	/**
	 * 获取基础信息
	 */
	public Basis selectEntry( Map<String,Object> entryMap ) throws Exception;
	
	/**
	 * 更新基础信息
	 */
	public boolean updateEntry( Map<String,Object> entryMap ) throws Exception; 
}