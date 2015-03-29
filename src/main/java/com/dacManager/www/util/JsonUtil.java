package com.dacManager.www.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 与JSON相关的工具类
 * @author Administrator
 *
 */
public class JsonUtil {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    /**
     * 创建返回JSON对象
     * @param	contextMap	上下文对象
     * @return
     */
    public static String createRetJson( Map<String,Object> contextMap ) throws Exception {
    	try{
    		Map<String,Object> retMap = new HashMap<String,Object>();
        	retMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, contextMap.get(StaticVariable.MANAGER_ERROR_MESSAGE));
        	retMap.put(StaticVariable.MANAGER_RESULT, contextMap.get(StaticVariable.MANAGER_RESULT));
        	JSONObject jsonObject = JSONObject.fromObject(retMap);
        	return jsonObject.toString();
    	}catch(Exception ex) {
    		logger.error("生成JSON处理结果对象失败",ex);
    		throw ex;
    	}
    }
    
    /**
     * 创建未知异常处理结果的JSON字符串
     * @return
     */
    public static String createErrorRetJson() {
    	Map<String,Object> errorRetMap = new HashMap<String,Object>();
    	errorRetMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "操作失败，未知错误。");
    	errorRetMap.put(StaticVariable.MANAGER_RESULT, false);
    	JSONObject jsonObject = JSONObject.fromObject(errorRetMap);
    	return jsonObject.toString();
    }
    
    
}
