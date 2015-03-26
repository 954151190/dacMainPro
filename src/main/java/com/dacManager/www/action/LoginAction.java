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
 * �������¼����
 * @author Administrator
 *
 */
public class LoginAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

	/**
	 * �û���
	 */
	private String user_name;
	/**
	 * ����
	 */
	private String user_password;
	/**
	 * ��¼ʧ�ܷ�����Ϣ
	 */
	private String error_message;
	/**
	 * ���������
	 */
    private IUserServer userService;//�ӿ�photoServices����ע��  photoServices�����Ʊ����application_bean������һ��  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * ��¼
     * @param map	
     * @return
     */
    public String login() {
    	try{
    		//�ж����������Ƿ���Ч
    		if( null == this.user_name || this.user_name.isEmpty() ){
    			logger.error("��¼ʧ�ܣ��û���Ϊ�ա�");
    			this.error_message = "��¼ʧ�ܣ��û���Ϊ�ա�";
    			return ERROR;
    		}
    		if( null == this.user_password || this.user_password.isEmpty() ) {
    			logger.error("��¼ʧ��,��¼����Ϊ�ա�");
    			this.error_message = "��¼ʧ��,��¼����Ϊ��";
    			return ERROR;
    		}
    		//�ж��û���Ϣ�Ƿ���Ч
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
    		logger.error("��¼ʧ��,�쳣��Ϣ��" , ex);
    		this.error_message = "��¼ʧ�ܡ�";
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