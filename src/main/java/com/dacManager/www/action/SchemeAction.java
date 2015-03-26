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
 * ������ҵ���������
 * @author Administrator
 *
 */
public class SchemeAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(SchemeAction.class);
	/**
	 * ��ҳ���󣬸��𴫵ݷ�ҳ����
	 */
	private Page page = new Page();
	
	/**
	 * �������޸ġ�ɾ���������ݲ�Ʒ��Ϣ����
	 */
	private Scheme scheme = new Scheme();
	
	/**
	 * ҵ���б�ҳ�����ݼ���
	 */
	private List<Scheme> schemeList = new ArrayList<Scheme>();
	
	/**
	 * ҵ���������ݼ���
	 */
	private Map<String , String> schemeTypeMap = new HashMap<String,String>();
	
	/**
	 * ���������
	 */
    private ISchemeServer schemeServer;//�ӿ�photoServices����ע��  photoServices�����Ʊ����application_bean������һ��  
    
    /**
     * ҵ�����ͷ������
     */
    private ISchemeTypeServer schemeTypeServer;
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * ��ȡҵ���б���Ϣ
     * @param map	
     * @return
     */
    public String schemeList() {
    	try {
    		/**
        	 * �����ݿ��в���ҵ��������Ϣ������Ϊ���϶���󷵻�ǰ̨ҳ��
        	 */
			 List<SchemeType> schemeTypeList = schemeTypeServer.selectEntryList(null);
			 this.schemeTypeMap = schemeTypeList2Map(schemeTypeList);
	    	/**
	    	 * �����ݿ��з�ҳ����ҵ����Ϣ������Ϊ���϶���󷵻�ǰ̨ҳ��
	    	 */
	    	Map<String,Object> parameterMap = new HashMap<String,Object>();
	    	parameterMap.put("page", this.page);//���÷�ҳ����
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
     * ����ҵ��-��תҳ��
     * @param map
     * @return
     */
    public String toSchemeAdd() {
    	/**
    	 * �����ݿ��в���ҵ��������Ϣ������Ϊ���϶���󷵻�ǰ̨ҳ��
    	 */
    	try {
			List<SchemeType> schemeTypeList = schemeTypeServer.selectEntryList(null);
			this.schemeTypeMap = schemeTypeList2Map(schemeTypeList);
		} catch (Exception ex) {
			logger.error("�쳣��Ϣ" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * ����ҵ��-��תҳ��
     * @return
     */
    public String toSchemeUpdate() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			/**
	    	 * �����ݿ��в���ҵ��������Ϣ������Ϊ���϶���󷵻�ǰ̨ҳ��
	    	 */
			List<SchemeType> schemeTypeList = schemeTypeServer.selectEntryList(null);
			this.schemeTypeMap = schemeTypeList2Map(schemeTypeList);
			/**
			 * ���ݲ�������׼�����µ�ҵ����Ϣ
			 */
			contextMap.put(StaticVariable.MS_SCHEME_OBJECT, this.scheme);
			Map<String,Object> schemeMap = schemeServer.selectEntry4ID(contextMap);
			if( null != schemeMap ){
				//����ҵ����Ϣ����
				this.scheme = Scheme.Map2Product(schemeMap);
			}else{
				//����ҵ����Ϣ������
			}
		}catch(Exception ex) {
			logger.error("��ת����ҵ��ҳ���쳣" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * ����ҵ��-ִ�и���
     * @return
     */
    public String schemeUpdate() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//��������
    		this.scheme = this.filterCode(this.scheme);
    		contextMap.put(StaticVariable.MS_SCHEME_OBJECT, this.scheme);
    		//ִ�и��²���
    		schemeServer.updateEntryServer( contextMap );
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("����ҵ��ʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//������������
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * ɾ��ҵ��
     * @return
     */
    public String schemeDelete() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		contextMap.put(StaticVariable.MS_SCHEME_OBJECT, this.scheme);
    		//ִ��ɾ������
    		schemeServer.deleteEntryServer(contextMap);
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("ɾ��ҵ��ʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
    		contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//������������
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * ����ҵ��-ִ�����
     * AJAX����
     * @return
     */
    public String schemeAdd() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		//��ʼ����HTML���ش������ַ���
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//��������
	    	this.scheme = this.filterCode(this.scheme);
	    	contextMap.put(StaticVariable.MS_SCHEME_OBJECT, this.scheme);
	    	//ִ����Ӳ���
	    	schemeServer.saveEntryServer(contextMap);
			//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
		} catch (Exception ex) {
			logger.error("����ҵ����Ϣʧ�ܣ�ʧ��ԭ��",ex);
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