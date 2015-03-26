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
import com.dacManager.www.entry.Product;
import com.dacManager.www.server.IProductServer;
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
/**
 * 负责处理产品相关请求
 * @author Administrator
 *
 */
public class ProductAction_bak extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(ProductAction_bak.class);
	/**
	 * 分页对象，负责传递分页参数
	 */
	private Page page = new Page();
	
	/**
	 * 新增、修改、删除操作传递产品信息对象
	 */
	private Product product = new Product();
	
	/**
	 * 产品列表页面数据集合
	 */
	private List<Product> productList = new ArrayList<Product>();
	
	/**
	 * 服务类对象
	 */
    private IProductServer productServer;//接口photoServices依赖注入  photoServices的名称必须和application_bean中名称一致  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * 获取产品列表信息
     * @param map	
     * @return
     */
    public String productList() {
    	/**
    	 * 从数据库中分页查找产品信息，保存为集合对象后返回前台页面
    	 */
    	Map<String,Object> parameterMap = new HashMap<String,Object>();
    	parameterMap.put("page", this.page);//设置分页属性
    	productList = productServer.selectEntryList4Page(parameterMap);
    	return SUCCESS;
    }

    /**
     * 新增产品-跳转页面
     * @param map
     * @return
     */
    public String toProductAdd() {
    	return SUCCESS;
    }
    
    
    /**
     * 更新产品-跳转页面
     * @return
     */
    public String toProductUpdate() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			//根据参数查找准备更新的产品信息
			contextMap.put(StaticVariable.MS_PRODUCT_OBJECT, this.product);
			Map<String,Object> bulletinMap = productServer.selectEntry4ID(contextMap);
			if( null != bulletinMap ){
				//更新产品信息存在
				this.product = Product.Map2Product(bulletinMap);
			}else{
				//更新产品信息不存在
			}
		}catch(Exception ex) {
			logger.error("跳转更新产品页面异常" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * 更新产品-执行更新
     * @return
     */
    public String productUpdate() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//过滤乱码
    		this.product = this.filterCode(this.product);
    		contextMap.put(StaticVariable.MS_PRODUCT_OBJECT, this.product);
    		//执行更新操作
    		productServer.updateEntryServer( contextMap );
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("更新产品失败，失败原因。", ex);
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * 删除产品
     * @return
     */
    public String productDelete() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		contextMap.put(StaticVariable.MS_PRODUCT_OBJECT, this.product);
    		//执行删除操作
    		productServer.deleteEntryServer(contextMap);
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("删除产品失败，失败原因。", ex);
    		//设置返回结果
    		contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * 新增产品-执行添加
     * AJAX调用
     * @return
     */
    public String productAdd() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		//初始化向HTML返回处理结果字符串
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//过滤乱码
	    	this.product = this.filterCode(this.product);
	    	contextMap.put(StaticVariable.MS_PRODUCT_OBJECT, this.product);
	    	//执行添加操作
	    	productServer.saveEntryServer(contextMap);
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
    private Product filterCode( Product product ) throws UnsupportedEncodingException {
    	if( null != product.getTitle() ){
    		product.setTitle( URLDecoder.decode( product.getTitle() ,"UTF-8") );
    	}

    	if( null != product.getContent() ) {
    		product.setContent( URLDecoder.decode( product.getContent() ,"UTF-8") );
    	}
    	return product;
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
} 