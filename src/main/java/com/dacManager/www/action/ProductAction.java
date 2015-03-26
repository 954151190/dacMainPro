package com.dacManager.www.action;
import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dacManager.www.entry.Page;
import com.dacManager.www.entry.Product;
import com.dacManager.www.server.IProductServer;
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
/**
 * �������Ʒ�������
 * @author Administrator
 *
 */
public class ProductAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(ProductAction.class);
	/**
     * ����ͼ�洢·������ʱ��
     */
    public static String photoPath = "C:\\impageManage\\product\\";
    
    /**
     * ����ͼĬ��չʾͼƬ�Ĵ洢·��(��ʱ)
     */
    public static String photoPathDef = "proDef.jpg";
	/**
	 * ����ͼ
	 */
	private File file;
	/**
	 * ��ҳ���󣬸��𴫵ݷ�ҳ����
	 */
	private Page page = new Page();
	
	/**
	 * �������޸ġ�ɾ���������ݲ�Ʒ��Ϣ����
	 */
	private Product product = new Product();
	
	/**
	 * ��Ʒ�б�ҳ�����ݼ���
	 */
	private List<Product> productList = new ArrayList<Product>();
	
	/**
	 * ���������
	 */
    private IProductServer productServer;//�ӿ�photoServices����ע��  photoServices�����Ʊ����application_bean������һ��
    
    /**
     * ����ҳ����Ϣ�洢����
     */
    private Map<String,Object> retMap = new HashMap<String,Object>();
    
    /**
     * ����ҳ����ϢJSON����
     */
    private String retJson;
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * ��ȡ��Ʒ�б���Ϣ
     * @param map	
     * @return
     */
    public String productList() {
    	/**
    	 * �����ݿ��з�ҳ���Ҳ�Ʒ��Ϣ������Ϊ���϶���󷵻�ǰ̨ҳ��
    	 */
    	Map<String,Object> parameterMap = new HashMap<String,Object>();
    	parameterMap.put("page", this.page);//���÷�ҳ����
    	productList = productServer.selectEntryList4Page(parameterMap);
    	return SUCCESS;
    }

    /**
     * ������Ʒ-��תҳ��
     * @param map
     * @return
     */
    public String toProductAdd() {
    	return SUCCESS;
    }
    
    
    /**
     * ���²�Ʒ-��תҳ��
     * @return
     */
    public String toProductUpdate() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			//���ݲ�������׼�����µĲ�Ʒ��Ϣ
			contextMap.put(StaticVariable.MS_PRODUCT_OBJECT, this.product);
			Map<String,Object> entryMap = productServer.selectEntry4ID(contextMap);
			if( null != entryMap ){
				//���²�Ʒ��Ϣ����
				this.product = Product.Map2Product(entryMap);
			}else{
				//���²�Ʒ��Ϣ������
			}
		}catch(Exception ex) {
			logger.error("��ת���²�Ʒҳ���쳣" , ex);
		}
    	return SUCCESS;
    }
    
    /**
     * ���²�Ʒ-ִ�и���
     * @return
     */
    public String productUpdate() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//��������
    		this.product = this.filterCode(this.product);
    		contextMap.put(StaticVariable.MS_PRODUCT_FILE, this.file);
    		contextMap.put(StaticVariable.MS_PRODUCT_OBJECT, this.product);
    		//ִ�и��²���
    		productServer.updateEntryServer( contextMap );
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("���²�Ʒʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	Map<String,Object> retMap = createJSONMap(contextMap);
    	JSONObject jsonObject = JSONObject.fromObject(retMap);
    	retJson = jsonObject.toString();
    	return SUCCESS;
    }
    
    /**
     * ɾ����Ʒ
     * @return
     */
    public String productDelete() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	try{
    		contextMap.put(StaticVariable.MS_PRODUCT_OBJECT, this.product);
    		//ִ��ɾ������
    		productServer.deleteEntryServer(contextMap);
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("ɾ����Ʒʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
    		contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//������������
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * ������Ʒ-ִ�����
     * ����JSON����ǰ̨����
     * @return
     */
    public String productAdd() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		//��ʼ����HTML���ش������ַ���
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//��������
	    	this.product = this.filterCode(this.product);
	    	contextMap.put(StaticVariable.MS_PRODUCT_OBJECT, this.product);
	    	contextMap.put(StaticVariable.MS_PRODUCT_FILE, this.file);
	    	//ִ����Ӳ���
	    	productServer.saveEntryServer(contextMap);
			//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
		} catch (Exception ex) {
			logger.error("���湫ʾ������Ϣʧ�ܣ�ʧ��ԭ��",ex);
			//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
		} 
    	Map<String,Object> retMap = createJSONMap(contextMap);
    	JSONObject jsonObject = JSONObject.fromObject(retMap);
    	retJson = jsonObject.toString();
    	return SUCCESS;
    }
    
    private Map<String,Object> createJSONMap( Map<String,Object> contextMap ) {
    	Map<String,Object> retMap = new HashMap<String,Object>();
    	retMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, contextMap.get(StaticVariable.MANAGER_ERROR_MESSAGE));
    	retMap.put(StaticVariable.MANAGER_RESULT, contextMap.get(StaticVariable.MANAGER_RESULT));
    	return retMap;
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
    private Product filterCode( Product product ) throws UnsupportedEncodingException {
    	if( null != product.getTitle() ){
    		product.setTitle( URLDecoder.decode( product.getTitle() ,"UTF-8") );
    	}

    	if( null != product.getContent() ) {
    		product.setContent( URLDecoder.decode( product.getContent() ,"UTF-8") );
    	}
    	return product;
    }
    
    public static void main(String[] args) {
    	try{
	    	String str = "%253Cp%253E1%253Cbr/%253E%253C/p%253E";
	    	String jieguo  = URLDecoder.decode( str ,"UTF-8");
	    	System.out.println(jieguo);
    	}catch(Exception ex) {
    		
    	}
    	
	}
    

	public IProductServer getBulletinServer() {
		return productServer;
	}

	public void setBulletinServer(IProductServer bulletinServer) {
		this.productServer = bulletinServer;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public IProductServer getProductServer() {
		return productServer;
	}

	public void setProductServer(IProductServer productServer) {
		this.productServer = productServer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Map<String, Object> getRetMap() {
		return retMap;
	}

	public void setRetMap(Map<String, Object> retMap) {
		this.retMap = retMap;
	}

	public String getRetJson() {
		return retJson;
	}

	public void setRetJson(String retJson) {
		this.retJson = retJson;
	}
} 