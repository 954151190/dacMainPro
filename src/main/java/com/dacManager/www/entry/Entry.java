package com.dacManager.www.entry;

import java.util.Map;

/**
 * 实体类基类
 * @author Administrator
 *
 */
public class Entry {
	/**
	 * 判断MAP中是否存在有效内容
	 * @param entryMap
	 * @param key
	 * @return
	 */
	protected static boolean isEmpty( Map<String,Object> entryMap , String key ) {
		if( entryMap.containsKey(key) ) {
			if( null != entryMap.get(key) ) {
				return false;
			}
		}
		return true;
	}
}
