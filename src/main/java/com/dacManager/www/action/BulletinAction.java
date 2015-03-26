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
 * ������ʾ�����������
 * @author Administrator
 *
 */
public class BulletinAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(BulletinAction.class);
	/**
	 * ��ҳ���󣬸��𴫵ݷ�ҳ����
	 */
	private Page page = new Page();
	
	/**
	 * �������޸ġ�ɾ���������ݹ�ʾ������Ϣ����
	 */
	private Bulletin bulletin = new Bulletin();
	
	/**
	 * �û��б�ҳ�����ݼ���
	 */
	private List<Bulletin> bulletinList = new ArrayList<Bulletin>();
	
	/**
	 * ���������
	 */
    private IBulletinServer bulletinServer;//�ӿ�photoServices����ע��  photoServices�����Ʊ����application_bean������һ��  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * ��ȡ��ʾ�����б���Ϣ
     * @param map	
     * @return
     */
    public String bulletinList() {
    	/**
    	 * �����ݿ��з�ҳ���ҹ�ʽ������Ϣ������Ϊ���϶���󷵻�ǰ̨ҳ��
    	 */
    	Map<String,Object> parameterMap = new HashMap<String,Object>();
    	parameterMap.put("page", this.page);//���÷�ҳ����
    	bulletinList = bulletinServer.selectBulletinList4Page(parameterMap);
    	return SUCCESS;
    }

    /**
     * ������ʾ����-��תҳ��
     * @param map
     * @return
     */
    public String toBulletinAdd() {
    	return SUCCESS;
    }
    
    
    /**
     * �����û�-��תҳ��
     * @return
     */
    public String toBulletinUpdate() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			//���ݲ�������׼�����µ��û���Ϣ
			contextMap.put(StaticVariable.MS_BULLETIN_OBJECT, this.bulletin);
			Map<String,Object> bulletinMap = bulletinServer.selectBulletin4ID(contextMap);
			if( null != bulletinMap ){
				//���¹�ʾ������Ϣ����
				this.bulletin = Bulletin.Map2Bulletin(bulletinMap);
			}else{
				//���¹�ʾ������Ϣ������
				
			}
		}catch(Exception ex) {
			logger.error("��ת���¹�ʾ����ҳ���쳣" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * �����û�-ִ�и���
     * @return
     */
    public String bulletinUpdate() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//��������
    		this.bulletin = this.filterCode(this.bulletin);
    		contextMap.put(StaticVariable.MS_BULLETIN_OBJECT, this.bulletin);
    		//ִ�и��²���
    		bulletinServer.updateBulletinServer( contextMap );
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("���¹�ʾ����ʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//������������
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * ɾ���û�
     * @return
     */
    public String bulletinDelete() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		contextMap.put(StaticVariable.MS_BULLETIN_OBJECT, this.bulletin);
    		//ִ��ɾ������
    		bulletinServer.deleteBulletinServer(contextMap);
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("ɾ����ʾ����ʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
    		contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//������������
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * ������ʾ����-ִ�����
     * AJAX����
     * @return
     */
    public String bulletinAdd() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		//��ʼ����HTML���ش������ַ���
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//��������
	    	this.bulletin = this.filterCode(this.bulletin);
	    	contextMap.put(StaticVariable.MS_BULLETIN_OBJECT, this.bulletin);
	    	//ִ����Ӳ���
	    	bulletinServer.saveBulletinServer(contextMap);
			//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
		} catch (Exception ex) {
			logger.error("���湫ʾ������Ϣʧ�ܣ�ʧ��ԭ��",ex);
			//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
		} 
    	//������������
    	createHtmlMsg(contextMap);
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