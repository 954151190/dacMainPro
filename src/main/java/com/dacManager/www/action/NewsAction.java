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

import com.dacManager.www.entry.News;
import com.dacManager.www.entry.Page;
import com.dacManager.www.server.INewsServer;
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
/**
 * 负责处理农信要闻相关请求
 * @author Administrator
 *
 */
public class NewsAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(NewsAction.class);
	/**
	 * 分页对象，负责传递分页参数
	 */
	private Page page = new Page();
	
	/**
	 * 新增、修改、删除操作传递产品信息对象
	 */
	private News news = new News();
	
	/**
	 * 产品列表页面数据集合
	 */
	private List<News> newsList = new ArrayList<News>();
	
	/**
	 * 服务类对象
	 */
    private INewsServer newsServer;//接口photoServices依赖注入  photoServices的名称必须和application_bean中名称一致  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * 获取业务列表信息
     * @param map	
     * @return
     */
    public String newsList() {
    	/**
    	 * 从数据库中分页查找业务信息，保存为集合对象后返回前台页面
    	 */
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	contextMap.put(StaticVariable.PAGE_NEWS, this.page);//设置分页属性
    	newsList = newsServer.selectEntryList4Page(contextMap);
    	//处理分页对象
    	//查询总数
    	Long allEntry = newsServer.countEntry( contextMap );
    	this.page.setAllCount( allEntry );
    	//计算总页数
    	if( (this.page.getAllCount() % this.page.getCount()) != 0) {
    		this.page.setAllPage( (this.page.getAllCount() / this.page.getCount()) +1 );
    	}else{
    		this.page.setAllPage( (this.page.getAllCount() / this.page.getCount())  );
    	}
    	return SUCCESS;
    }

    /**
     * 新增业务-跳转页面
     * @param map
     * @return
     */
    public String toNewsAdd() {
    	return SUCCESS;
    }
    
    
    /**
     * 更新农信要闻-跳转页面
     * @return
     */
    public String toNewsUpdate() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			//根据参数查找准备更新的农信要闻信息
			contextMap.put(StaticVariable.MS_NEWS_OBJECT, this.news);
			Map<String,Object> newsMap = newsServer.selectEntry4ID(contextMap);
			if( null != newsMap ){
				//更新业务信息存在
				this.news = News.Map2Product(newsMap);
			}else{
				//更新业务信息不存在
			}
		}catch(Exception ex) {
			logger.error("跳转更新业务页面异常" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * 更新农信要闻-执行更新
     * @return
     */
    public String newsUpdate() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//过滤乱码
    		this.news = this.filterCode(this.news);
    		contextMap.put(StaticVariable.MS_NEWS_OBJECT, this.news);
    		//执行更新操作
    		newsServer.updateEntryServer( contextMap );
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
    public String newsDelete() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		contextMap.put(StaticVariable.MS_NEWS_OBJECT, this.news);
    		//执行删除操作
    		newsServer.deleteEntryServer(contextMap);
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
    public String newsAdd() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		//初始化向HTML返回处理结果字符串
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//过滤乱码
	    	this.news = this.filterCode(this.news);
	    	contextMap.put(StaticVariable.MS_NEWS_OBJECT, this.news);
	    	//执行添加操作
	    	newsServer.saveEntryServer(contextMap);
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
    private News filterCode( News entry ) throws UnsupportedEncodingException {
    	if( null != entry.getTitle() ){
    		entry.setTitle( URLDecoder.decode( entry.getTitle() ,"UTF-8") );
    	}

    	if( null != entry.getContent() ) {
    		entry.setContent( URLDecoder.decode( entry.getContent() ,"UTF-8") );
    	}
    	return entry;
    }

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public List<News> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<News> newsList) {
		this.newsList = newsList;
	}

	public INewsServer getNewsServer() {
		return newsServer;
	}

	public void setNewsServer(INewsServer newsServer) {
		this.newsServer = newsServer;
	}
} 