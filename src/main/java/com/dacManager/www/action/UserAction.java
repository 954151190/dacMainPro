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
 * �������û��������
 * @author Administrator
 *
 */
public class UserAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(UserAction.class);
	/**
	 * ��ҳ���󣬸��𴫵ݷ�ҳ����
	 */
	private Page page = new Page();
	
	/**
	 * �������޸ġ�ɾ�����������û���Ϣ����
	 */
	private User user = new User();
	
	/**
	 * �û��б�ҳ�����ݼ���
	 */
	private List<User> userList = new ArrayList<User>();
	
	/**
	 * ���������
	 */
    private IUserServer userService;//�ӿ�photoServices����ע��  photoServices�����Ʊ����application_bean������һ��  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * ��ȡ�û��б���Ϣ
     * @param map	
     * @return
     */
    public String userList() {
    	/**
    	 * �����ݿ��з�ҳ�����û���Ϣ������Ϊ���϶���󷵻�ǰ̨ҳ��
    	 */
    	Map<String,Object> parameterMap = new HashMap<String,Object>();
    	parameterMap.put("page", this.page);//���÷�ҳ����
    	userList = userService.selectUserList4Page(parameterMap);
    	return SUCCESS;
    }

    /**
     * �����û�-��תҳ��
     * @param map
     * @return
     */
    public String toUserAdd() {
    	return SUCCESS;
    }
    
    
    /**
     * �����û�-��תҳ��
     * @return
     */
    public String toUserUpdate() {
    	//��ʼ�������Ķ���
		Map<String,Object> userMap = new HashMap<String,Object>();
		try{
			//���ݲ�������׼�����µ��û���Ϣ
			userMap.put(StaticVariable.MS_USER_OBJECT, this.user);
			Map<String,Object> userObjectMap = userService.selectUser4ID(userMap);
			if( null != userObjectMap ){
				//�����û���Ϣ����
				this.user = User.Map2User( userObjectMap);
			}else{
				//�����û���Ϣ������
				
			}
		}catch(Exception ex) {
			logger.error("��ת�����û�ҳ���쳣" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * �����û�-ִ�и���
     * @return
     */
    public String userUpdate() {
    	//��ʼ�������ʶ���
    	Map<String,Object> userMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//��������
    		this.user = this.filterCode(this.user);
    		userMap.put(StaticVariable.MS_USER_OBJECT, this.user);
    		//ִ�и��²���
    		userService.updateUserServer( userMap );
    		//���÷��ؽ��
			userMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("�����û�ʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
			userMap.put( StaticVariable.MANAGER_RESULT , false);
			userMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//������������
    	createHtmlMsg(userMap);
    	return null;
    }
    
    /**
     * ɾ���û�
     * @return
     */
    public String userDelete() {
    	//��ʼ�������ʶ���
    	Map<String,Object> userMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		userMap.put(StaticVariable.MS_USER_OBJECT, this.user);
    		//ִ��ɾ������
    		userService.deleteUserServer(userMap);
    		//���÷��ؽ��
			userMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("ɾ���û�ʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
    		userMap.put( StaticVariable.MANAGER_RESULT , false);
			userMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//������������
    	createHtmlMsg(userMap);
    	return null;
    }
    
    /**
     * �����û�-ִ�����
     * AJAX����
     * @return
     */
    public String userAdd() {
    	//��ʼ�������Ķ���
		Map<String,Object> userMap = new HashMap<String,Object>();
		//��ʼ����HTML���ش������ַ���
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//��������
	    	this.user = this.filterCode(this.user);
	    	userMap.put(StaticVariable.MS_USER_OBJECT, this.user);
	    	//ִ����Ӳ���
	    	userService.saveUserServer(userMap);
			//���÷��ؽ��
			userMap.put( StaticVariable.MANAGER_RESULT , true);
		} catch (Exception ex) {
			logger.error("�����û���Ϣʧ�ܣ�ʧ��ԭ��",ex);
			//���÷��ؽ��
			userMap.put( StaticVariable.MANAGER_RESULT , false);
			userMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
		} 
    	//������������
    	createHtmlMsg(userMap);
    	return null;
    }
    
    /**
     * ������HTML�����ַ���
     * ΪAJAX���󷵻ش�����
     * ����JSON��ʽ�ַ���
     * @param userMap
     * @return
     */
    private void createHtmlMsg( Map<String,Object> userMap ) {
    	try{
    		JSONObject jsonObject = JSONObject.fromObject(userMap);
    		//��ʼ����HTML���ض���
        	HttpServletResponse response = ServletActionContext.getResponse(); 
        	response.setContentType("text/plain;charset=UTF-8");
        	PrintWriter out = response.getWriter();
        	out.print(jsonObject.toString());
         	out.close();
    	}catch(Exception ex) {
    		ex.printStackTrace();
    		logger.error("MAPתJSONʧ�ܣ���HTML����Ĭ�ϵĴ�������Ϣ��");
    	}
    }
    
    /**
     * ������HTML�����ַ���(Ĭ��)
     * ΪAJAX���󷵻ش�����
     * ����JSON��ʽ�ַ���
     * @return
     */
    private String createHtmlMsg() {
    	Map<String,Object> returnMap = new HashMap<String,Object>();
    	returnMap.put("managerErrorMessage","����ʧ�������²�����");
    	JSONObject jsonObject = JSONObject.fromObject(returnMap);
    	return jsonObject.toString();
    }
    
    /**
     * ����������Ϣ
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