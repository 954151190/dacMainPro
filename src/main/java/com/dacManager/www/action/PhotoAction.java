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
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
  
public class PhotoAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(SchemeTypeAction.class);
	/**
	 * ��ҳ���󣬸��𴫵ݷ�ҳ����
	 */
	private Page page = new Page();
	
	/**
	 * �������޸ġ�ɾ����������ҵ��������Ϣ����
	 */
	private Photo photo = new Photo();
	
	/**
	 * ��Ʒ�б�ҳ�����ݼ���
	 */
	private List<Photo> photoList = new ArrayList<Photo>();
	
	/**
	 * ���������
	 */
    private IPhotoServer photoServer;//�ӿ�photoServices����ע��  photoServices�����Ʊ����application_bean������һ��
    
    /**
     * ������������ͼƬ��Ϣ����
     */
    private File photoFile;
    
    /**
     * �洢ͼƬ��·������ʱ��
     */
    public static String photoPuth = "C:\\impageManage\\";
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * ��ȡͼƬ�б���Ϣ
     * @param map	
     * @return
     */
    public String photoList() {
    	/**
    	 * �����ݿ��з�ҳ����ͼƬ��Ϣ������Ϊ���϶���󷵻�ǰ̨ҳ��
    	 */
    	Map<String,Object> parameterMap = new HashMap<String,Object>();
    	parameterMap.put("page", this.page);//���÷�ҳ����
    	photoList = photoServer.selectEntryList4Page(parameterMap);
    	return SUCCESS;
    }

    /**
     * ����ͼƬ-��תҳ��
     * @param map
     * @return
     */
    public String toPhotoAdd() {
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
			contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.photo);
			Map<String,Object> schemeMap = photoServer.selectEntry4ID(contextMap);
			if( null != schemeMap ){
				//����ҵ����Ϣ����
				this.photo = Photo.Map2Product(schemeMap);
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
    		this.photo = this.filterCode(this.photo);
    		contextMap.put(StaticVariable.MS_SCHEME_TYPE_OBJECT, this.photo);
    		//ִ�и��²���
    		photoServer.updateEntryServer( contextMap );
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
     * ɾ��ͼƬ
     * @return
     */
    public String photoDelete() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	//��ʼ����HTML���ش������ַ���
    	String returnHtmlMsg = createHtmlMsg();
    	try{
    		contextMap.put(StaticVariable.MS_PHOTO_OBJECT, this.photo);
    		//ִ��ɾ���ļ�����
    		photoServer.deleteFileServer(contextMap);
    		//ִ��ɾ����Ϣ����
    		photoServer.deleteEntryServer(contextMap);
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
    	}catch(Exception ex) {
    		logger.error("ɾ��ͼƬʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
    		contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
    	}
    	//������������
    	createHtmlMsg(contextMap);
    	return null;
    }
    
    /**
     * չʾͼƬ��Ϣ
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
     * ����ҵ������-ִ�����
     * AJAX����
     * @return
     */
    public String photoAdd() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		//��ʼ����HTML���ش������ַ���
		String returnHtmlMsg = createHtmlMsg();
    	try {
	    	//��������
	    	this.photo = this.filterCode(this.photo);
	    	//����ͼƬΨһ��ʶ
	    	photo.setId(UUID.randomUUID().toString());
	    	//����ͼƬ�洢·��
	    	photo.setPath( PhotoAction.photoPuth + photo.getId() + ".jpg" );
	    	
	    	contextMap.put(StaticVariable.MS_PHOTO_OBJECT, this.photo);
	    	contextMap.put(StaticVariable.MS_PHOTO_FILE_OBJECT, this.photoFile);
	    	//ִ��ͼƬ�洢����
	    	Map<String,Object> saveFileResult = photoServer.saveFileServer(contextMap);
	    	if( !(Boolean)saveFileResult.get(StaticVariable.MANAGER_RESULT) ){
	    		throw new Exception( "����ͼƬ����ʧ�ܣ��洢ͼƬ�ļ�ʧ�ܡ�" );
	    	}
	    	//ִ����Ϣ�洢����
	    	Map<String,Object> saveEntryResult = photoServer.saveEntryServer(contextMap);
	    	if( !(Boolean)saveEntryResult.get(StaticVariable.MANAGER_RESULT) ) {
	    		throw new Exception( "����ͼƬ����ʧ�ܣ��洢ͼƬ��Ϣʧ�ܡ�" );
	    	}
			//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
		} catch (Exception ex) {
			logger.error("����ͼƬ��Ϣʧ�ܣ�ʧ��ԭ��",ex);
			//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
		} 
    	//������������
    	createHtmlMsg(contextMap);
    	return SUCCESS;
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
}  