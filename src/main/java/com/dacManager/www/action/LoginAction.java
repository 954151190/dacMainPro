package com.dacManager.www.action;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dacManager.www.entry.User;
import com.dacManager.www.server.IUserServer;
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
  
/**
 * 负责处理登录请求
 * @author Administrator
 *
 */
public class LoginAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

	/**
	 * 用户名
	 */
	private String user_name;
	/**
	 * 密码
	 */
	private String user_password;
	/**
	 * 登录失败返回信息
	 */
	private String error_message;
	/**
	 * 服务类对象
	 */
    private IUserServer userService;//接口photoServices依赖注入  photoServices的名称必须和application_bean中名称一致  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * 登录
     * @param map	
     * @return
     */
    public String login() {
    	try{
    		//判断请求内容是否有效
    		if( null == this.user_name || this.user_name.isEmpty() ){
    			logger.error("登录失败，用户名为空。");
    			this.error_message = "登录失败，用户名为空。";
    			return ERROR;
    		}
    		if( null == this.user_password || this.user_password.isEmpty() ) {
    			logger.error("登录失败,登录密码为空。");
    			this.error_message = "登录失败,登录密码为空";
    			return ERROR;
    		}
    		//判断用户信息是否有效
    		Map<String,Object> userMap = new HashMap<String,Object>();
    		User user = new User();
    		user.setUser_name(this.user_name);
    		user.setUser_password(this.user_password);
    		userMap.put(StaticVariable.MS_USER_OBJECT, user);
    		Map<String,Object> serverMap = userService.loginServer(userMap);
    		boolean serverMapResult = (Boolean)serverMap.get(StaticVariable.MANAGER_RESULT);
    		if( serverMapResult ) {
    			return SUCCESS;
    		}else{
    			String serverMapMessage = serverMap.get(StaticVariable.MANAGER_ERROR_MESSAGE).toString();
    			this.error_message = serverMapMessage;
    			return ERROR;
    		}
    	}catch(Exception ex) {
    		logger.error("登录失败,异常信息。" , ex);
    		this.error_message = "登录失败。";
    		return ERROR;
    	}
    }

	public IUserServer getUserService() {
		return userService;
	}

	public void setUserService(IUserServer userService) {
		this.userService = userService;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
}  