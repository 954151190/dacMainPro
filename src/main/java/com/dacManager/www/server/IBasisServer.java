package com.dacManager.www.server;

import java.util.List;
import java.util.Map;

import com.dacManager.www.entry.Basis;
import com.dacManager.www.entry.Bulletin;

public interface IBasisServer {
	
	/**
	 * ��ȡ������Ϣ
	 */
	public Basis selectEntry( Map<String,Object> entryMap ) throws Exception;
	
	/**
	 * ���»�����Ϣ
	 */
	public boolean updateEntry( Map<String,Object> entryMap ) throws Exception; 
}