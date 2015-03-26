package com.dacManager.www.action;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dacManager.www.entry.Page;
import com.dacManager.www.entry.SchemeType;
import com.dacManager.www.server.ISchemeTypeServer;
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
/**
 * 负责处理业务类型相关请求
 * @author Administrator
 *
 */
public class SchemeTypeAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(SchemeTypeAction.class);
	/**
	 * 分页对象，负责传递分页参数
	 */
	private Page page = new Page();
	
	/**
	 * 新增、修改、删除操作传递业务类型信息对象
	 */
	private SchemeType schemeType = new SchemeType();
	
	/**
	 * 产品列表页面数据集合
	 */
	private List<SchemeType> schemeTypeList = new ArrayList<SchemeType>();
	
	/**
	 * 服务类对象
	 */
    private ISchemeTypeServer schemeTypeServer;//接口photoServices依赖注入  photoServices的名称必须和application_bean中名称一致  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * 获取业务类型列表信息
     * @param map	
     * @return
     */
    public String schemeTypeList() {
    	/**
    	 * 从数据库中分页查找业务类型信息，保存为集合对象后返回前台页面
    	 */
    	Map<String,Object> parameterMap = new HashMap<String,Object>();
    	parameterMap.put("page", this.page);//设置分页属性
    	schemeTypeList = schemeTypeServer.selectEntryList4Page(parameterMap);
    	return SUCCESS;
    }

    /**
     * 新增业务-跳转页面
     * @param map
     * @return
     */
    public String toSchemeTypeAdd() {
    	return SUCCESS;
    }
    
    
    /**
     * 更新业务类型-跳转页面
     * @return
     */
    public String toSchemeTypeUpdate() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			//根据参数查找准备更新的业务信息
			contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.schemeType);
			Map<String,Object> schemeMap = schemeTypeServer.selectEntry4ID(contextMap);
			if( null != schemeMap ){
				//更新业务信息存在
				this.schemeType = SchemeType.Map2Product(schemeMap);
			}else{
				//更新业务信息不存在
			}
		}catch(Exception ex) {
			logger.error("跳转更新业务页面异常" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * 更新业务类型-执行更新
     * @return
     */
    public String schemeTypeUpdate() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//过滤乱码
    		this.schemeType = this.filterCode(this.schemeType);
    		contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.schemeType);
    		//执行更新操作
    		schemeTypeServer.updateEntryServer( contextMap );
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("更新业务类型失败，失败原因。", ex);
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * 删除业务类型
     * @return
     */
    public String schemeTypeDelete() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.schemeType);
    		//执行删除操作
    		schemeTypeServer.deleteEntryServer(contextMap);
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("删除业务类型失败，失败原因。", ex);
    		//设置返回结果
    		contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * 新增业务类型-执行添加
     * AJAX调用
     * @return
     */
    public String schemeTypeAdd() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		//初始化向HTML返回处理结果字符串
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//过滤乱码
	    	this.schemeType = this.filterCode(this.schemeType);
	    	//添加默认值
	    	this.schemeType.setState("1");
	    	contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.schemeType);
	    	//执行添加操作
	    	schemeTypeServer.saveEntryServer(contextMap);
			//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
		} catch (Exception ex) {
			logger.error("保存业务类型信息失败，失败原因",ex);
			//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
		} 
    	//创建返回内容
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * 创建向HTML返回字符串
     * 为AJAX请求返回处理结果
     * 返回JSON格式字符串
     * @param userMap
     * @return
     */
    private void createHtmlMsg( Map<String,Object> userMap ) {
    	try{
    		JSONObject jsonObject = JSONObject.fromObject(userMap);
    		//初始化向HTML返回对象
        	HttpServletResponse response = ServletActionContext.getResponse(); 
        	response.setContentType("text/plain;charset=UTF-8");
        	PrintWriter out = response.getWriter();
        	out.print(jsonObject.toString());
         	out.close();
    	}catch(Exception ex) {
    		ex.printStackTrace();
    		logger.error("MAP转JSON失败，向HTML返回默认的错误结果信息。");
    	}
    }
    
    /**
     * 创建向HTML返回字符串(默认)
     * 为AJAX请求返回处理结果
     * 返回JSON格式字符串
     * @return
     */
    private String createHtmlMsg() {
    	Map<String,Object> returnMap = new HashMap<String,Object>();
    	returnMap.put("managerErrorMessage","处理失败请重新操作。");
    	JSONObject jsonObject = JSONObject.fromObject(returnMap);
    	return jsonObject.toString();
    }
    
    /**
     * 过滤乱码信息
     * @param user
     * @return
     * @throws UnsupportedEncodingException 
     */
    private SchemeType filterCode( SchemeType schemeType ) throws UnsupportedEncodingException {
    	if( null != schemeType.getTitle() ){
    		schemeType.setTitle( URLDecoder.decode( schemeType.getTitle() ,"UTF-8") );
    	}

    	if( null != schemeType.getContent() ) {
    		schemeType.setContent( URLDecoder.decode( schemeType.getContent() ,"UTF-8") );
    	}
    	return schemeType;
    }

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public SchemeType getSchemeType() {
		return schemeType;
	}

	public void setSchemeType(SchemeType schemeType) {
		this.schemeType = schemeType;
	}

	public List<SchemeType> getSchemeTypeList() {
		return schemeTypeList;
	}

	public void setSchemeTypeList(List<SchemeType> schemeTypeList) {
		this.schemeTypeList = schemeTypeList;
	}

	public ISchemeTypeServer getSchemeTypeServer() {
		return schemeTypeServer;
	}

	public void setSchemeTypeServer(ISchemeTypeServer schemeTypeServer) {
		this.schemeTypeServer = schemeTypeServer;
	}
} 