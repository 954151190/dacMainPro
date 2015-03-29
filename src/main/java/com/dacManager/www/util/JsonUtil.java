package com.dacManager.www.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ��JSON��صĹ�����
 * @author Administrator
 *
 */
public class JsonUtil {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    /**
     * ��������JSON����
     * @param	contextMap	�����Ķ���
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
    		logger.error("����JSON����������ʧ��",ex);
    		throw ex;
    	}
    }
    
    /**
     * ����δ֪�쳣��������JSON�ַ���
     * @return
     */
    public static String createErrorRetJson() {
    	Map<String,Object> errorRetMap = new HashMap<String,Object>();
    	errorRetMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "����ʧ�ܣ�δ֪����");
    	errorRetMap.put(StaticVariable.MANAGER_RESULT, false);
    	JSONObject jsonObject = JSONObject.fromObject(errorRetMap);
    	return jsonObject.toString();
    }
    
    
}
