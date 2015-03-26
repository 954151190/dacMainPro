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
 * ������ҵ�������������
 * @author Administrator
 *
 */
public class SchemeTypeAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(SchemeTypeAction.class);
	/**
	 * ��ҳ���󣬸��𴫵ݷ�ҳ����
	 */
	private Page page = new Page();
	
	/**
	 * �������޸ġ�ɾ����������ҵ��������Ϣ����
	 */
	private SchemeType schemeType = new SchemeType();
	
	/**
	 * ��Ʒ�б�ҳ�����ݼ���
	 */
	private List<SchemeType> schemeTypeList = new ArrayList<SchemeType>();
	
	/**
	 * ���������
	 */
    private ISchemeTypeServer schemeTypeServer;//�ӿ�photoServices����ע��  photoServices�����Ʊ����application_bean������һ��  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * ��ȡҵ�������б���Ϣ
     * @param map	
     * @return
     */
    public String schemeTypeList() {
    	/**
    	 * �����ݿ��з�ҳ����ҵ��������Ϣ������Ϊ���϶���󷵻�ǰ̨ҳ��
    	 */
    	Map<String,Object> parameterMap = new HashMap<String,Object>();
    	parameterMap.put("page", this.page);//���÷�ҳ����
    	schemeTypeList = schemeTypeServer.selectEntryList4Page(parameterMap);
    	return SUCCESS;
    }

    /**
     * ����ҵ��-��תҳ��
     * @param map
     * @return
     */
    public String toSchemeTypeAdd() {
    	return SUCCESS;
    }
    
    
    /**
     * ����ҵ������-��תҳ��
     * @return
     */
    public String toSchemeTypeUpdate() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			//���ݲ�������׼�����µ�ҵ����Ϣ
			contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.schemeType);
			Map<String,Object> schemeMap = schemeTypeServer.selectEntry4ID(contextMap);
			if( null != schemeMap ){
				//����ҵ����Ϣ����
				this.schemeType = SchemeType.Map2Product(schemeMap);
			}else{
				//����ҵ����Ϣ������
			}
		}catch(Exception ex) {
			logger.error("��ת����ҵ��ҳ���쳣" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * ����ҵ������-ִ�и���
     * @return
     */
    public String schemeTypeUpdate() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//��������
    		this.schemeType = this.filterCode(this.schemeType);
    		contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.schemeType);
    		//ִ�и��²���
    		schemeTypeServer.updateEntryServer( contextMap );
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("����ҵ������ʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//������������
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * ɾ��ҵ������
     * @return
     */
    public String schemeTypeDelete() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.schemeType);
    		//ִ��ɾ������
    		schemeTypeServer.deleteEntryServer(contextMap);
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("ɾ��ҵ������ʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
    		contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//������������
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * ����ҵ������-ִ�����
     * AJAX����
     * @return
     */
    public String schemeTypeAdd() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		//��ʼ����HTML���ش������ַ���
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//��������
	    	this.schemeType = this.filterCode(this.schemeType);
	    	//���Ĭ��ֵ
	    	this.schemeType.setState("1");
	    	contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.schemeType);
	    	//ִ����Ӳ���
	    	schemeTypeServer.saveEntryServer(contextMap);
			//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
		} catch (Exception ex) {
			logger.error("����ҵ��������Ϣʧ�ܣ�ʧ��ԭ��",ex);
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