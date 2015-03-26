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
import com.dacManager.www.entry.Scheme;
import com.dacManager.www.entry.SchemeType;
import com.dacManager.www.server.ISchemeServer;
import com.dacManager.www.server.ISchemeTypeServer;
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
/**
 * 负责处理业务相关请求
 * @author Administrator
 *
 */
public class SchemeAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(SchemeAction.class);
	/**
	 * 分页对象，负责传递分页参数
	 */
	private Page page = new Page();
	
	/**
	 * 新增、修改、删除操作传递产品信息对象
	 */
	private Scheme scheme = new Scheme();
	
	/**
	 * 业务列表页面数据集合
	 */
	private List<Scheme> schemeList = new ArrayList<Scheme>();
	
	/**
	 * 业务类型数据集合
	 */
	private Map<String , String> schemeTypeMap = new HashMap<String,String>();
	
	/**
	 * 服务类对象
	 */
    private ISchemeServer schemeServer;//接口photoServices依赖注入  photoServices的名称必须和application_bean中名称一致  
    
    /**
     * 业务类型服务对象
     */
    private ISchemeTypeServer schemeTypeServer;
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * 获取业务列表信息
     * @param map	
     * @return
     */
    public String schemeList() {
    	try {
    		/**
        	 * 从数据库中查找业务类型信息，保存为集合对象后返回前台页面
        	 */
			 List<SchemeType> schemeTypeList = schemeTypeServer.selectEntryList(null);
			 this.schemeTypeMap = schemeTypeList2Map(schemeTypeList);
	    	/**
	    	 * 从数据库中分页查找业务信息，保存为集合对象后返回前台页面
	    	 */
	    	Map<String,Object> parameterMap = new HashMap<String,Object>();
	    	parameterMap.put("page", this.page);//设置分页属性
	    	schemeList = schemeServer.selectEntryList4Page(parameterMap);
    	} catch (Exception e) {
			logger.error("");
		}
    	return SUCCESS;
    }
    
    private Map<String,String> schemeTypeList2Map( List<SchemeType> schemeTypeList ) {
    	Map<String,String> stMap = new HashMap<String,String>();
    	for( SchemeType st : schemeTypeList ) {
    		stMap.put(st.getId(), st.getTitle());
    	}
    	return stMap;
    }

    /**
     * 新增业务-跳转页面
     * @param map
     * @return
     */
    public String toSchemeAdd() {
    	/**
    	 * 从数据库中查找业务类型信息，保存为集合对象后返回前台页面
    	 */
    	try {
			List<SchemeType> schemeTypeList = schemeTypeServer.selectEntryList(null);
			this.schemeTypeMap = schemeTypeList2Map(schemeTypeList);
		} catch (Exception ex) {
			logger.error("异常信息" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * 更新业务-跳转页面
     * @return
     */
    public String toSchemeUpdate() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			/**
	    	 * 从数据库中查找业务类型信息，保存为集合对象后返回前台页面
	    	 */
			List<SchemeType> schemeTypeList = schemeTypeServer.selectEntryList(null);
			this.schemeTypeMap = schemeTypeList2Map(schemeTypeList);
			/**
			 * 根据参数查找准备更新的业务信息
			 */
			contextMap.put(StaticVariable.MS_SCHEME_OBJECT, this.scheme);
			Map<String,Object> schemeMap = schemeServer.selectEntry4ID(contextMap);
			if( null != schemeMap ){
				//更新业务信息存在
				this.scheme = Scheme.Map2Product(schemeMap);
			}else{
				//更新业务信息不存在
			}
		}catch(Exception ex) {
			logger.error("跳转更新业务页面异常" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * 更新业务-执行更新
     * @return
     */
    public String schemeUpdate() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//过滤乱码
    		this.scheme = this.filterCode(this.scheme);
    		contextMap.put(StaticVariable.MS_SCHEME_OBJECT, this.scheme);
    		//执行更新操作
    		schemeServer.updateEntryServer( contextMap );
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("更新业务失败，失败原因。", ex);
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * 删除业务
     * @return
     */
    public String schemeDelete() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		contextMap.put(StaticVariable.MS_SCHEME_OBJECT, this.scheme);
    		//执行删除操作
    		schemeServer.deleteEntryServer(contextMap);
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("删除业务失败，失败原因。", ex);
    		//设置返回结果
    		contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * 新增业务-执行添加
     * AJAX调用
     * @return
     */
    public String schemeAdd() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		//初始化向HTML返回处理结果字符串
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//过滤乱码
	    	this.scheme = this.filterCode(this.scheme);
	    	contextMap.put(StaticVariable.MS_SCHEME_OBJECT, this.scheme);
	    	//执行添加操作
	    	schemeServer.saveEntryServer(contextMap);
			//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
		} catch (Exception ex) {
			logger.error("保存业务信息失败，失败原因",ex);
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
    private Scheme filterCode( Scheme scheme ) throws UnsupportedEncodingException {
    	if( null != scheme.getTitle() ){
    		scheme.setTitle( URLDecoder.decode( scheme.getTitle() ,"UTF-8") );
    	}

    	if( null != scheme.getContent() ) {
    		scheme.setContent( URLDecoder.decode( scheme.getContent() ,"UTF-8") );
    	}
    	return scheme;
    }

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Scheme getScheme() {
		return scheme;
	}

	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}

	public List<Scheme> getSchemeList() {
		return schemeList;
	}

	public void setSchemeList(List<Scheme> schemeList) {
		this.schemeList = schemeList;
	}

	public ISchemeServer getSchemeServer() {
		return schemeServer;
	}

	public void setSchemeServer(ISchemeServer schemeServer) {
		this.schemeServer = schemeServer;
	}

	public Map<String, String> getSchemeTypeMap() {
		return schemeTypeMap;
	}

	public void setSchemeTypeMap(Map<String, String> schemeTypeMap) {
		this.schemeTypeMap = schemeTypeMap;
	}

	public ISchemeTypeServer getSchemeTypeServer() {
		return schemeTypeServer;
	}

	public void setSchemeTypeServer(ISchemeTypeServer schemeTypeServer) {
		this.schemeTypeServer = schemeTypeServer;
	}
} 