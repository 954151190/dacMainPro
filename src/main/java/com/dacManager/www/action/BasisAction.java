package com.dacManager.www.action;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dacManager.www.entry.Basis;
import com.dacManager.www.server.IBasisServer;
import com.dacManager.www.util.JsonUtil;
import com.dacManager.www.util.StaticVariable;
import com.opensymphony.xwork2.ActionSupport;
  
/**
 * ������ʾ�����������
 * @author Administrator
 *
 */
public class BasisAction extends ActionSupport {  
	private static final Logger logger = LoggerFactory.getLogger(BasisAction.class);
	/**
	 * ���ؽ��JSON����
	 */
	private String retJson;
	/**
	 * �������޸ġ�ɾ���������ݹ�ʾ������Ϣ����
	 */
	private Basis basis = new Basis();
	/**
	 * ���������
	 */
    private IBasisServer basisServer;//�ӿ�photoServices����ע��  photoServices�����Ʊ����application_bean������һ��  
    
    @Override
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
    /**
     * ���»���-��תҳ��
     * @return
     */
    public String toBasisUpdate() {
    	//��ʼ�������Ķ���
		Map<String,Object> contextMap = new HashMap<String,Object>();
		try{
			//���ݲ�������׼�����µ��û���Ϣ
			this.basis = basisServer.selectEntry(contextMap);
		}catch(Exception ex) {
			logger.error("��ת���¹�ʾ����ҳ���쳣" , ex);
		}
    	return SUCCESS;
    }
    
    /**
     * ���»���-ִ�и���
     * @return
     */
    public String basisUpdate() {
    	//��ʼ�������ʶ���
    	Map<String,Object> contextMap = new HashMap<String,Object>();
    	try{
    		//��������
    		this.basis = this.filterCode(this.basis);
    		contextMap.put(StaticVariable.MS_BASIS_OBJECT, this.basis);
    		//ִ�и��²���
    		boolean falg = basisServer.updateEntry(contextMap);
    		if( !falg ){
    			throw new Exception("����ʧ��");
    		}
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , true);
			//���ɷ���JSON
			this.retJson = JsonUtil.createRetJson(contextMap);
    	}catch(Exception ex) {
    		logger.error("���»�����Ϣʧ�ܣ�ʧ��ԭ��", ex);
    		//���÷��ؽ��
			contextMap.put( StaticVariable.MANAGER_RESULT , false);
			contextMap.put( StaticVariable.MANAGER_ERROR_MESSAGE , ex);
			//���ɷ���JSON
			this.retJson = JsonUtil.createErrorRetJson();
    	}
    	return SUCCESS;
    }
    
    /**
     * ����������Ϣ
     * @param user
     * @return
     * @throws UnsupportedEncodingException 
     */
    private Basis filterCode( Basis basis ) throws UnsupportedEncodingException {
    	if( null != basis.getPhone() ){
    		basis.setPhone( URLDecoder.decode( basis.getPhone() ,"UTF-8") );
    	}
    	if( null != basis.getAddress() ){
    		basis.setAddress( URLDecoder.decode( basis.getAddress() ,"UTF-8") );
    	}
    	if( null != basis.getQq() ){
    		basis.setQq( URLDecoder.decode( basis.getQq() ,"UTF-8") );
    	}
    	if( null != basis.getWx() ){
    		basis.setWx( URLDecoder.decode( basis.getWx() ,"UTF-8") );
    	}
    	return basis;
    }

	public Basis getBasis() {
		return basis;
	}

	public void setBasis(Basis basis) {
		this.basis = basis;
	}

	public IBasisServer getBasisServer() {
		return basisServer;
	}

	public void setBasisServer(IBasisServer basisServer) {
		this.basisServer = basisServer;
	}

	public String getRetJson() {
		return retJson;
	}

	public void setRetJson(String retJson) {
		this.retJson = retJson;
	}
}  