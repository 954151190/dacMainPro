package com.dacManager.www.action;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dacManager.www.entry.Page;
import com.dacManager.www.entry.Photo;
import com.dacManager.www.server.IPhotoServer;
import com.dacManager.www.util.JsonUtil;
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
  
public class PhotoAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(SchemeTypeAction.class);
	/**
	 * 分页对象，负责传递分页参数
	 */
	private Page page = new Page();
	
	/**
	 * 新增、修改、删除操作传递业务类型信息对象
	 */
	private Photo photo = new Photo();
	
	/**
	 * 产品列表页面数据集合
	 */
	private List<Photo> photoList = new ArrayList<Photo>();
	
	/**
	 * 服务类对象
	 */
    private IPhotoServer photoServer;//接口photoServices依赖注入  photoServices的名称必须和application_bean中名称一致
    
    /**
     * 新增操作传递图片信息对象
     */
    private File photoFile;
    
    /**
     * 存储图片的路径（临时）
     */
    public static String photoPuth = "C:\\impageManage\\";
    
    /**
     * 返回JSON对象
     */
    public String retJson;
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * 获取图片列表信息
     * @param map	
     * @return
     */
    public String photoList() {
    	/**
    	 * 从数据库中分页查找图片信息，保存为集合对象后返回前台页面
    	 */
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	contextMap.put(StaticVariable.PAGE_PHOTO, this.page);//设置分页属性
    	photoList = photoServer.selectEntryList4Page(contextMap);
    	//处理分页对象
    	//查询总数
    	Long allEntry = photoServer.countEntry( contextMap );
    	this.page.setAllCount( allEntry );
    	//计算总页数
    	if( (this.page.getAllCount() % this.page.getCount()) != 0) {
    		this.page.setAllPage( (this.page.getAllCount() / this.page.getCount()) +1 );
    	}else{
    		this.page.setAllPage( (this.page.getAllCount() / this.page.getCount())  );
    	}
    	return SUCCESS;
    }

    /**
     * 新增图片-跳转页面
     * @param map
     * @return
     */
    public String toPhotoAdd() {
    	return SUCCESS;
    }
    
    
    /**
     * 更新业务类型-跳转页面
     * @return
     */
    public String toSchemeTypeUpdate() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			//根据参数查找准备更新的业务信息
			contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.photo);
			Map<String,Object> schemeMap = photoServer.selectEntry4ID(contextMap);
			if( null != schemeMap ){
				//更新业务信息存在
				this.photo = Photo.Map2Product(schemeMap);
			}else{
				//更新业务信息不存在
			}
		}catch(Exception ex) {
			logger.error("跳转更新业务页面异常" , ex);
		}
    	return SUCCESS;
    }
    
    
    /**
     * 更新业务类型-执行更新
     * @return
     */
    public String schemeTypeUpdate() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		//过滤乱码
    		this.photo = this.filterCode(this.photo);
    		contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.photo);
    		//执行更新操作
    		photoServer.updateEntryServer( contextMap );
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("更新业务类型失败，失败原因。", ex);
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * 删除图片
     * @return
     */
    public String photoDelete() {
    	//初始化上下问对象
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//初始化向HTML返回处理结果字符串
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		contextMap.put(StaticVariable.MS_PHOTO_OBJECT, this.photo);
    		//执行删除文件操作
    		photoServer.deleteFileServer(contextMap);
    		//执行删除信息操作
    		photoServer.deleteEntryServer(contextMap);
    		//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("删除图片失败，失败原因。", ex);
    		//设置返回结果
    		contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//创建返回内容
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * 展示图片信息
     * @return
     */
    public synchronized String showPhoto()  {
    	synchronized (this.photo) {
    		try {
    	        HttpServletResponse response = ServletActionContext.getResponse();
    	    	ServletOutputStream out = response.getOutputStream();
    	    	String photoPath = ProductAction.photoPath+ this.photo.getId();
    			InputStream is = new FileInputStream(new File(photoPath));
    			int b = 0;
    			byte[] bytes = new byte[0xffff];
    			while ((b = is.read(bytes, 0, 0xffff)) > 0) {
    				out.write(bytes, 0, b);
    			}
    			is.close();
    			out.flush();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
		}
    	return null;
    }
    
    
    /**
     * 新增业务类型-执行添加
     * AJAX调用
     * @return
     */
    @SuppressWarnings("static-access")
	public String photoAdd() {
    	//初始化上下文对象
		Map<String,Object> contextMap = new HashMap<String,Object>();
    	try {
	    	//过滤乱码
	    	this.photo = this.filterCode(this.photo);
	    	//生成图片唯一标识
	    	photo.setId(UUID.randomUUID().toString());
	    	//生成图片存储路径
	    	photo.setPath( PhotoAction.photoPuth + photo.getId() + ".jpg" );
	    	
	    	contextMap.put(StaticVariable.MS_PHOTO_OBJECT, this.photo);
	    	contextMap.put(StaticVariable.MS_PHOTO_FILE_OBJECT, this.photoFile);
	    	//执行图片存储操作
	    	Map<String,Object> saveFileResult = photoServer.saveFileServer(contextMap);
	    	if( !(Boolean)saveFileResult.get(StaticVariable.MANAGER_RESULT) ){
	    		throw new Exception( "保存图片操作失败，存储图片文件失败。" );
	    	}
	    	//执行信息存储操作
	    	Map<String,Object> saveEntryResult = photoServer.saveEntryServer(contextMap);
	    	if( !(Boolean)saveEntryResult.get(StaticVariable.MANAGER_RESULT) ) {
	    		throw new Exception( "保存图片操作失败，存储图片信息失败。" );
	    	}
			//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
			//创建返回内容
	    	retJson = JsonUtil.createRetJson(contextMap);
		} catch (Exception ex) {
			logger.error("保存图片信息失败，失败原因",ex);
			//设置返回结果
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
			retJson = JsonUtil.createErrorRetJson();
		} 
    	return SUCCESS;
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
    private Photo filterCode( Photo photo ) throws UnsupportedEncodingException {
    	if( null != photo.getTitle() ){
    		photo.setTitle( URLDecoder.decode( photo.getTitle() ,"UTF-8") );
    	}
    	return photo;
    }

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public List<Photo> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<Photo> photoList) {
		this.photoList = photoList;
	}

	public IPhotoServer getPhotoServer() {
		return photoServer;
	}

	public void setPhotoServer(IPhotoServer photoServer) {
		this.photoServer = photoServer;
	}

	public File getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(File photoFile) {
		this.photoFile = photoFile;
	}

	public String getRetJson() {
		return retJson;
	}

	public void setRetJson(String retJson) {
		this.retJson = retJson;
	}
}  