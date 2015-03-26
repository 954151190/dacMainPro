package com.dacManager.www.server.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dacManager.www.action.ProductAction;
import com.dacManager.www.dao.db.rowMapper.ProductRowMapper;
import com.dacManager.www.entry.Product;
import com.dacManager.www.entry.User;
import com.dacManager.www.server.IProductServer;
import com.dacManager.www.util.BuildSQLUtil;
import com.dacManager.www.util.QueryHelper;
import com.dacManager.www.util.StaticVariable;

public class ProductServerImpl implements IProductServer {
	private static final Logger logger = LoggerFactory.getLogger(ProductServerImpl.class);
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return this .jdbcTemplate;
	}
	
	public void setJdbcTemplate( JdbcTemplate jdbcTemplate ) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * ������ӹ�ʾ������Ϣ���񷽷�
	 * @param contextMap
	 */
	public Map<String,Object> saveEntryServer( Map<String,Object> contextMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//������Ƭ��Ϣ
			productSaveFileServer(contextMap);
			//����ҵ����Ϣ
			productMessManagerServer(contextMap);
		}catch(Exception ex) {
			logger.error("��Ʒ��Ϣ����ʧ�ܣ�ʧ����Ϣ",ex );
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "��Ʒ��Ϣ����ʧ�ܣ������²�����");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	private void productUpdateFileServer( Map<String,Object> contextMap ) throws Exception {
		try{
			//��ȡ��ƷͼƬ��Ϣ
			File newFile = (File) contextMap.get(StaticVariable.MS_PRODUCT_FILE);
			if( null == newFile ) {
				logger.debug("����ʱδ�ṩ��ͼƬ��ͼƬ�����κ��޸ġ�");
			}else {
				logger.debug("����ʱ�ṩ��ͼƬ��ɾ��ԭʼͼƬ���洢��ͼƬ");
				//ɾ��ԭͼƬ
				productDeleteFileServer( contextMap );
				//������ͼƬ
				//��ȡ��Ʒ��Ϣ
				Product product  = (Product) contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
				//Ϊ��Ʒ����ͼ����Ψһ��ʶ
	    		String photo_id = UUID.randomUUID().toString();
	    		product.setPhoto_id(photo_id+".jpg");
	    		saveFile( contextMap , product);
			}
		}catch(Exception ex) {
			logger.error("�����ƷͼƬ����ʧ��");
			throw new Exception("�����ƷͼƬ����ʧ��" , ex);
		}
	}
	
	/**
	 * ɾ����ƷͼƬ�Ĺ��߷���
	 * @param contextMap
	 * @throws Exception
	 */
	private void productDeleteFileServer( Map<String,Object> contextMap ) throws Exception {
		try{
			//��ȡ��Ʒ��Ϣ
			Product product =(Product) contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
			String photo_id = product.getPhoto_id();
			if( !ProductAction.photoPathDef.equals(photo_id) ) {
				//����ͼƬ�����ַ
				String photo_path = ProductAction.photoPath + photo_id;
				try{
					File deleteFile = new File( photo_path );
					deleteFile.delete();
				}catch(Exception ex) {
					logger.error("�쳣������δ�׳���ԭ��ɾ����ƷͼƬʧ�ܣ�δ�ҵ�ͼƬ��Ϣ��");
				}
			}else{
				logger.debug("Ĭ��ͼƬ����ɾ����");
			}
			
		}catch(Exception ex) {
			logger.error("ɾ����ƷͼƬʧ��");
			throw new Exception("ɾ����ƷͼƬʧ��",ex);
		}
	}

	/**
	 * ��Ʒ�ư��������
	 * @param contextMap
	 */
	private void productSaveFileServer( Map<String,Object> contextMap ) throws Exception {
		try{
			//��ȡ��Ʒ��Ϣ
			Product product = (Product)contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
			//��ȡ��ƷͼƬ��Ϣ
			File newFile = (File) contextMap.get(StaticVariable.MS_PRODUCT_FILE);
			if( null != newFile ) {//ͼƬ����ִ�д洢ͼƬ����
				//Ϊ��Ʒ����ͼ����Ψһ��ʶ
	    		String photo_id = UUID.randomUUID().toString();
	    		product.setPhoto_id(photo_id+".jpg");
				//��ȡ��ƷͼƬ��Ϣ
				saveFile(contextMap, product);
			}else{
				//ͼƬ�����ڣ��趨Ĭ��ֵ
				product.setPhoto_id(ProductAction.photoPathDef);
			}
		}catch(Exception ex) {
			logger.error("�����ƷͼƬ��Ϣʧ��" , ex);
			throw new Exception("�����ƷͼƬ��Ϣʧ��" , ex);
		} 
	}

	/**
	 * �����ƷͼƬ�Ĺ��߷���
	 * @param contextMap
	 * @param product
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveFile(Map<String, Object> contextMap, Product product) throws FileNotFoundException, IOException {
		File inFile = (File) contextMap.get(StaticVariable.MS_PRODUCT_FILE);
		File outFile = new File( ProductAction.photoPath + product.getPhoto_id() );
		InputStream input = new FileInputStream(inFile);
		OutputStream out = new FileOutputStream(outFile);
		int temp = 0;
		while( ( temp = input.read() ) != -1 ) {
			out.write(temp);
		}
		out.close();
	}
	
	/**
	 * ��Ʒ��Ϣ���洦�����
	 * @param contextMap
	 * @throws Exception
	 */
	private void productMessManagerServer(Map<String, Object> contextMap) throws Exception {
		try{
			//��ȡҵ����Ϣ
			Product product = (Product)contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
			//��ʼ��ID
			product.setId(UUID.randomUUID().toString());
			//��ʼ������ʱ��
			product.setCreate_time( new Date() );
			this.saveEntry(contextMap);
		}catch(Exception ex) {
			logger.error("�����Ʒ��Ϣʧ��",ex);
			throw new Exception("�����Ʒ��Ϣʧ��",ex);
		}
		
	} 
	
	public Map<String,Object> updateEntryServer( Map<String,Object> contextMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//��ȡ��Ʒ��Ϣ
			Product product = (Product)contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
			//�жϲ�Ʒ��Ϣ�Ƿ����
			Map<String,Object> tempProductMap = this.selectEntry4ID(contextMap);
			if( null != tempProductMap ) {
				//����ͼƬ��Ϣ
				this.productUpdateFileServer( contextMap );
				//���²�Ʒ��Ϣ
				this.updateEntry(contextMap);
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "��Ʒ��Ϣ�����ڣ������²�����");
			}
		}catch(Exception ex) {
			logger.error("���¹�ʾ������Ϣʧ��,�쳣��Ϣ" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "��Ʒ��Ϣ����ʧ�ܣ������²�����");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public Map<String,Object> deleteEntryServer(Map<String, Object> contextMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//�жϲ�Ʒ��Ϣ�Ƿ����
			Map<String,Object> tempProductMap = this.selectEntry4ID(contextMap);
			Product product = Product.Map2Product(tempProductMap);
			if( null != tempProductMap ) {
				contextMap.put(StaticVariable.MS_PRODUCT_OBJECT, product);
				//ɾ����ƷͼƬ��Ϣ
				this.productDeleteFileServer(contextMap);
				//ɾ����Ʒ��Ϣ
				boolean managerBoolean = this.deleteEntry(contextMap);
				if( managerBoolean ) {
					//ִ�гɹ�
				}else{
					//ִ��ʧ��
					returnMap.put(StaticVariable.MANAGER_RESULT, false);
					returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "��Ʒ��Ϣɾ��ʧ�ܣ������²�����");
				}
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "��Ʒ��Ϣ�����ڣ������²�����");
			}
		}catch(Exception ex) {
			logger.error("ɾ����Ʒ��Ϣʧ��,�쳣��Ϣ" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "��Ʒ��Ϣɾ��ʧ�ܣ������²�����");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public void saveEntry(Map<String, Object> contextMap) throws Exception {
		Product product = (Product)contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		fields.add("TITLE");
		fields.add("CONTENT");
		fields.add("STATE");
		fields.add("CREATE_TIME");
		fields.add("AUTHOR_NAME");
		fields.add("AUTHOR_ID");
		fields.add("PHOTO_ID");
		
		values.add(product.getId());
		values.add(product.getTitle());
		values.add(product.getContent());
		values.add(product.getState());
		values.add( new java.sql.Date( product.getCreate_time().getTime() ) );
		values.add(product.getAuthor_name());
		values.add(product.getAuthor_id());
		values.add(product.getPhoto_id());
		
		String insertSql = BuildSQLUtil.buildSaveSQL(StaticVariable.TABLE_NAME_PRODUCT, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		QueryHelper.updateSql(conn, insertSql, values.toArray()); 
	}

	
	
	public boolean updateEntry(Map<String, Object> contextMap) throws Exception {
		Product product = (Product)contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("TITLE");
		fields.add("CONTENT");
		fields.add("PHOTO_ID");
		
		whereFields.add("ID");
		
		values.add(product.getTitle());
		values.add(product.getContent());
		values.add(product.getPhoto_id());
		values.add(product.getId());
		
		String insertSql = BuildSQLUtil.buildUpdateWithConditionSQL(StaticVariable.TABLE_NAME_PRODUCT, fields.toArray(), whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, insertSql, values.toArray()); 
		if(i != 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean deleteEntry(Map<String, Object> contextMap) throws Exception {
		Product product = (Product)contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		whereFields.add("ID");
		values.add(product.getId());
		String deleteSQL = BuildSQLUtil.buildDeleteWithCondtionSQL(StaticVariable.TABLE_NAME_PRODUCT, whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, deleteSQL, values.toArray());
		if( i == 1 ) {
			return true;
		}else{
			return false;
		}
	}

	private Map<String,Object> selectEntry4Login( Map<String,Object> contextMap ) throws Exception {
		User loginUser = (User)contextMap.get(StaticVariable.MS_USER_OBJECT);
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		whereFields.add("USER_NAME");
		whereFields.add("USER_PASSWORD");
		
		values.add(loginUser.getUser_name());
		values.add(loginUser.getUser_password());
		
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_USER, whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public Map<String, Object> selectEntry4ID(Map<String, Object> contextMap) throws Exception{
		Product product = (Product)contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		values.add(product.getId());
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_PRODUCT, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public Map<String,Object> selectEntry4UserName( Map<String,Object> contextMap ) throws Exception{
		User user = (User)contextMap.get(StaticVariable.MS_USER_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("USER_NAME");
		values.add(user.getUser_name());
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_USER, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public List<Product> selectEntryList4Page(Map<String, Object> contextMap) {
		String sql = "select * from " + StaticVariable.TABLE_NAME_PRODUCT+ "";
		List<Product> productList = jdbcTemplate.query(sql , new ProductRowMapper());
		return productList;
	}
}