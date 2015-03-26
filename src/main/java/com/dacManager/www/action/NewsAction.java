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
 * ������ũ��Ҫ���������
 * @author Administrator
 *
 */
public class NewsAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(NewsAction.class);
	/**
	 * ��ҳ���󣬸��𴫵ݷ�ҳ����
	 */
	private Page page = new Page();
	
	/**
	 * �������޸ġ�ɾ���������ݲ�Ʒ��Ϣ����
	 */
	private News news = new News();
	
	/**
	 * ��Ʒ�б�ҳ�����ݼ���
	 */
	private List<News> newsList = new ArrayList<News>();
	
	/**
	 * ���������
	 */
    private INewsServer newsServer;//�ӿ�photoServices����ע��  photoServices�����Ʊ����application_bean������һ��  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * ��ȡҵ���б���Ϣ
     * @param map	
     * @return
     */
    public String newsList() {
    	/**
    	 * �����ݿ��з�ҳ����ҵ����Ϣ������Ϊ���϶���󷵻�ǰ̨ҳ��
    	 */
    	Map<String,Object> parameterMap = new HashMap<String,Object>();
    	parameterMap.put("page", this.page);//���÷�ҳ����
    	newsList = newsServer.selectEntryList4Page(parameterMap);
    	return SUCCESS;
    }

    /**
     * ����ҵ��-��תҳ��
     * @param map
     * @return
     */
    public String toNewsAdd() {
    	return SUCCESS;
    }
    
    
    /**
     * ����ũ��Ҫ��-��תҳ��
     * @return
     */
    public String toNewsUpdate() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			//���ݲ�������׼�����µ�ũ��Ҫ����Ϣ
			contextMap.put(StaticVariable.MS_NEWS_OBJECT, this.news);
			Map<String,Object> newsMap = newsServer.selectEntry4ID(contextMap);
			if( null != newsMap ){
				//����ҵ����Ϣ����
				this.news = News.Map2Product(newsMap);
			}else{
				//����ҵ����Ϣ������
			}
		}catch(Exception ex) {
			logger.error("��ת����ҵ��ҳ���쳣" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * ����ũ��Ҫ��-ִ�и���
     * @return
     */
    public String newsUpdate() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//��������
    		this.news = this.filterCode(this.news);
    		contextMap.put(StaticVariable.MS_NEWS_OBJECT, this.news);
    		//ִ�и��²���
    		newsServer.updateEntryServer( contextMap );
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
    public String newsDelete() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		contextMap.put(StaticVariable.MS_NEWS_OBJECT, this.news);
    		//ִ��ɾ������
    		newsServer.deleteEntryServer(contextMap);
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
    public String newsAdd() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		//��ʼ����HTML���ش������ַ���
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//��������
	    	this.news = this.filterCode(this.news);
	    	contextMap.put(StaticVariable.MS_NEWS_OBJECT, this.news);
	    	//ִ����Ӳ���
	    	newsServer.saveEntryServer(contextMap);
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