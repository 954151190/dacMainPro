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
import com.dacManager.www.entry.User;
import com.dacManager.www.server.IUserServer;
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
/**
 * 负责处理用户相关请求
 * @author Administrator
 *
 */
public class UserAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(UserAction.class);
	/**
	 * 分页对象，负责传递分页参数
	 */
	private Page page = new Page();
	
	/**
	 * 新增、修改、删除操作传递用户信息对象
	 */
	private User user = new User();
	
	/**
	 * 用户列表页面数据集合
	 */
	private List<User> userList = new ArrayList<User>();
	
	/**
	 * 服务类对象
	 */
    private IUserServer userService;//接口photoServices依赖注入  photoServices的名称必须和application_bean中名称一致  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * 获取用户列表信息
     * @param map	
     * @return
     */
    public String userList() {
    	/**
    	 * 从数据库中分页查找用户信息，保存为集合对象后返回前台页面
    	 */
    	Map<String,Object> parameterMap = new HashMap<String,Object>();
    	parameterMap.put("page", this.page);//设置分页属性
    	userList = userService.selectUserList4Page(parameterMap);
    	return SUCCESS;
    }

    /**
     * 新增用户-跳转页面
     * @param map
     * @return
     */
    public String toUserAdd() {
    	return SUCCESS;
    }
    
    
    /**
     * 更新用户-跳转页面
     * @return
     */
    public String toUserUpdate() {
    	//初始化上下文对象
		Map<String,Object> userMap = new HashMap<String,Object>();
		try{
			//根据参数查找准备更新的用户信息
			userMap.put(StaticVariable.MS_USER_OBJECT, this.user);
			Map<String,Object> userObjectMap = userService.selectUser4ID(userMap);
			if( null != userObjectMap ){
				//更新用户信息存在
				this.user = User.Map2User( userObjectMap);
			}else{
				//更新用户信息不存在
				
			}
		}catch(Exception ex) {
			logger.error("跳转更新用户页面异常" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * 更新用户-执行更新
     * @return
     */
    public String userUpdate() {
    	//初始化上下问对象
    	Map<String,Object> userMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//过滤乱码
    		this.user = this.filterCode(this.user);
    		userMap.put(StaticVariable.MS_USER_OBJECT, this.user);
    		//执行更新操作
    		userService.updateUserServer( userMap );
    		//设置返回结果
			userMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("更新用户失败，失败原因。", ex);
    		//设置返回结果
			userMap.put( StaticVariable.MANAGER_RESULT , false);
			userMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(userMap);
    	return null;
    }
    
    /**
     * 删除用户
     * @return
     */
    public String userDelete() {
    	//初始化上下问对象
    	Map<String,Object> userMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		userMap.put(StaticVariable.MS_USER_OBJECT, this.user);
    		//执行删除操作
    		userService.deleteUserServer(userMap);
    		//设置返回结果
			userMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("删除用户失败，失败原因。", ex);
    		//设置返回结果
    		userMap.put( StaticVariable.MANAGER_RESULT , false);
			userMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(userMap);
    	return null;
    }
    
    /**
     * 新增用户-执行添加
     * AJAX调用
     * @return
     */
    public String userAdd() {
    	//初始化上下文对象
		Map<String,Object> userMap = new HashMap<String,Object>();
		//初始化向HTML返回处理结果字符串
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//过滤乱码
	    	this.user = this.filterCode(this.user);
	    	userMap.put(StaticVariable.MS_USER_OBJECT, this.user);
	    	//执行添加操作
	    	userService.saveUserServer(userMap);
			//设置返回结果
			userMap.put( StaticVariable.MANAGER_RESULT , true);
		} catch (Exception ex) {
			logger.error("保存用户信息失败，失败原因",ex);
			//设置返回结果
			userMap.put( StaticVariable.MANAGER_RESULT , false);
			userMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
		} 
    	//创建返回内容
    	createHtmlMsg(userMap);
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
    private User filterCode( User user ) throws UnsupportedEncodingException {
    	if( null != user.getName() ) {
    		user.setName( URLDecoder.decode( user.getName() ,"UTF-8") ); 
    	}
    	if( null != user.getUser_role() ) {
    		user.setUser_role( URLDecoder.decode( user.getUser_role() ,"UTF-8") );
    	}
    	if( null != user.getRemark() ) {
    		user.setRemark( URLDecoder.decode( user.getRemark() ,"UTF-8") );
    	}
    	return user;
    }
   
    
    

	public IUserServer getUserService() {
		return userService;
	}

	public void setUserService(IUserServer userService) {
		this.userService = userService;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}  