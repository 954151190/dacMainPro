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

import com.dacManager.www.entry.Bulletin;
import com.dacManager.www.entry.Page;
import com.dacManager.www.entry.User;
import com.dacManager.www.server.IBulletinServer;
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
/**
 * 负责处理公示公告相关请求
 * @author Administrator
 *
 */
public class BulletinAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(BulletinAction.class);
	/**
	 * 分页对象，负责传递分页参数
	 */
	private Page page = new Page();
	
	/**
	 * 新增、修改、删除操作传递公示公告信息对象
	 */
	private Bulletin bulletin = new Bulletin();
	
	/**
	 * 用户列表页面数据集合
	 */
	private List<Bulletin> bulletinList = new ArrayList<Bulletin>();
	
	/**
	 * 服务类对象
	 */
    private IBulletinServer bulletinServer;//接口photoServices依赖注入  photoServices的名称必须和application_bean中名称一致  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * 获取公示公告列表信息
     * @param map	
     * @return
     */
    public String bulletinList() {
    	/**
    	 * 从数据库中分页查找公式公告信息，保存为集合对象后返回前台页面
    	 */
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	contextMap.put(StaticVariable.PAGE_BULLETIN, this.page);//设置分页属性
    	bulletinList = bulletinServer.selectBulletinList4Page(contextMap);
    	//处理分页对象
    	//查询总数
    	Long allEntry = bulletinServer.countEntry( contextMap );
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
     * 新增公示公告-跳转页面
     * @param map
     * @return
     */
    public String toBulletinAdd() {
    	return SUCCESS;
    }
    
    
    /**
     * 更新用户-跳转页面
     * @return
     */
    public String toBulletinUpdate() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			//根据参数查找准备更新的用户信息
			contextMap.put(StaticVariable.MS_BULLETIN_OBJECT, this.bulletin);
			Map<String,Object> bulletinMap = bulletinServer.selectBulletin4ID(contextMap);
			if( null != bulletinMap ){
				//更新公示公告信息存在
				this.bulletin = Bulletin.Map2Bulletin(bulletinMap);
			}else{
				//更新公示公告信息不存在
				
			}
		}catch(Exception ex) {
			logger.error("跳转更新公示公告页面异常" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * 更新用户-执行更新
     * @return
     */
    public String bulletinUpdate() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//过滤乱码
    		this.bulletin = this.filterCode(this.bulletin);
    		contextMap.put(StaticVariable.MS_BULLETIN_OBJECT, this.bulletin);
    		//执行更新操作
    		bulletinServer.updateBulletinServer( contextMap );
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("更新公示公告失败，失败原因。", ex);
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * 删除用户
     * @return
     */
    public String bulletinDelete() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		contextMap.put(StaticVariable.MS_BULLETIN_OBJECT, this.bulletin);
    		//执行删除操作
    		bulletinServer.deleteBulletinServer(contextMap);
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("删除公示公告失败，失败原因。", ex);
    		//设置返回结果
    		contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * 新增公示公告-执行添加
     * AJAX调用
     * @return
     */
    public String bulletinAdd() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		//初始化向HTML返回处理结果字符串
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//过滤乱码
	    	this.bulletin = this.filterCode(this.bulletin);
	    	contextMap.put(StaticVariable.MS_BULLETIN_OBJECT, this.bulletin);
	    	//执行添加操作
	    	bulletinServer.saveBulletinServer(contextMap);
			//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
		} catch (Exception ex) {
			logger.error("保存公示公告信息失败，失败原因",ex);
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
    private Bulletin filterCode( Bulletin bulletin ) throws UnsupportedEncodingException {
    	if( null != bulletin.getTitle() ){
    		bulletin.setTitle( URLDecoder.decode( bulletin.getTitle() ,"UTF-8") );
    	}

    	if( null != bulletin.getContent() ) {
    		bulletin.setContent( URLDecoder.decode( bulletin.getContent() ,"UTF-8") );
    	}
    	return bulletin;
    }

	public IBulletinServer getBulletinServer() {
		return bulletinServer;
	}

	public void setBulletinServer(IBulletinServer bulletinServer) {
		this.bulletinServer = bulletinServer;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<Bulletin> getBulletinList() {
		return bulletinList;
	}

	public void setBulletinList(List<Bulletin> bulletinList) {
		this.bulletinList = bulletinList;
	}

	public Bulletin getBulletin() {
		return bulletin;
	}

	public void setBulletin(Bulletin bulletin) {
		this.bulletin = bulletin;
	}
}  