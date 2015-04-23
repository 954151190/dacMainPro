package com.dacManager.www.action;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dacManager.www.entry.Basis;
import com.dacManager.www.server.IBasisServer;
import com.dacManager.www.util.JsonUtil;
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
/**
 * 负责处理公示公告相关请求
 * @author Administrator
 *
 */
public class BasisAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(BasisAction.class);
	/**
	 * 返回结果JSON对象
	 */
	private String retJson;
	/**
	 * 新增、修改、删除操作传递公示公告信息对象
	 */
	private Basis basis = new Basis();
	/**
	 * 服务类对象
	 */
    private IBasisServer basisServer;//接口photoServices依赖注入  photoServices的名称必须和application_bean中名称一致  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * 更新基础-跳转页面
     * @return
     */
    public String toBasisUpdate() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			//根据参数查找准备更新的用户信息
			this.basis = basisServer.selectEntry(contextMap);
		}catch(Exception ex) {
			logger.error("跳转更新公示公告页面异常" , ex);
		}
    	return SUCCESS;
    }
    
    /**
     * 更新基础-执行更新
     * @return
     */
    public String basisUpdate() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	try{
    		//过滤乱码
    		this.basis = this.filterCode(this.basis);
    		contextMap.put(StaticVariable.MS_BASIS_OBJECT, this.basis);
    		//执行更新操作
    		boolean falg = basisServer.updateEntry(contextMap);
    		if( !falg ){
    			throw new Exception("更新失败");
    		}
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
			//生成返回JSON
			this.retJson = JsonUtil.createRetJson(contextMap);
    	}catch(Exception ex) {
    		logger.error("更新基础信息失败，失败原因。", ex);
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
			//生成返回JSON
			this.retJson = JsonUtil.createErrorRetJson();
    	}
    	return SUCCESS;
    }
    
    /**
     * 过滤乱码信息
     * @param user
     * @return
     * @throws UnsupportedEncodingException 
     */
    private Basis filterCode( Basis basis ) throws UnsupportedEncodingException {
    	if( null != basis.getPhone() ){
    		basis.setPhone( URLDecoder.decode( basis.getPhone() ,"UTF-8") );
    	}
    	if( null != basis.getAddress() ){
    		basis.setAddress( URLDecoder.decode( basis.getAddress() ,"UTF-8") );
    	}
    	if( null != basis.getQq() ){
    		basis.setQq( URLDecoder.decode( basis.getQq() ,"UTF-8") );
    	}
    	if( null != basis.getWx() ){
    		basis.setWx( URLDecoder.decode( basis.getWx() ,"UTF-8") );
    	}
    	return basis;
    }

	public Basis getBasis() {
		return basis;
	}

	public void setBasis(Basis basis) {
		this.basis = basis;
	}

	public IBasisServer getBasisServer() {
		return basisServer;
	}

	public void setBasisServer(IBasisServer basisServer) {
		this.basisServer = basisServer;
	}

	public String getRetJson() {
		return retJson;
	}

	public void setRetJson(String retJson) {
		this.retJson = retJson;
	}
}  