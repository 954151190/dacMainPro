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
	 * 对外添加公示公告信息服务方法
	 * @param contextMap
	 */
	public Map<String,Object> saveEntryServer( Map<String,Object> contextMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//处理照片信息
			productSaveFileServer(contextMap);
			//处理业务信息
			productMessManagerServer(contextMap);
		}catch(Exception ex) {
			logger.error("产品信息保存失败，失败信息",ex );
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "产品信息保存失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	private void productUpdateFileServer( Map<String,Object> contextMap ) throws Exception {
		try{
			//获取产品图片信息
			File newFile = (File) contextMap.get(StaticVariable.MS_PRODUCT_FILE);
			if( null == newFile ) {
				logger.debug("更新时未提供新图片。图片不做任何修改。");
			}else {
				logger.debug("更新时提供新图片。删除原始图片，存储新图片");
				//删除原图片
				productDeleteFileServer( contextMap );
				//保存新图片
				//获取产品信息
				Product product  = (Product) contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
				//为产品略缩图定义唯一标识
	    		String photo_id = UUID.randomUUID().toString();
	    		product.setPhoto_id(photo_id+".jpg");
	    		saveFile( contextMap , product);
			}
		}catch(Exception ex) {
			logger.error("处理产品图片更新失败");
			throw new Exception("处理产品图片更新失败" , ex);
		}
	}
	
	/**
	 * 删除产品图片的工具方法
	 * @param contextMap
	 * @throws Exception
	 */
	private void productDeleteFileServer( Map<String,Object> contextMap ) throws Exception {
		try{
			//获取产品信息
			Product product =(Product) contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
			String photo_id = product.getPhoto_id();
			if( !ProductAction.photoPathDef.equals(photo_id) ) {
				//生成图片物理地址
				String photo_path = ProductAction.photoPath + photo_id;
				try{
					File deleteFile = new File( photo_path );
					deleteFile.delete();
				}catch(Exception ex) {
					logger.error("异常被捕获，未抛出，原因：删除产品图片失败，未找到图片信息。");
				}
			}else{
				logger.debug("默认图片不可删除。");
			}
			
		}catch(Exception ex) {
			logger.error("删除产品图片失败");
			throw new Exception("删除产品图片失败",ex);
		}
	}

	/**
	 * 产品推按处理服务
	 * @param contextMap
	 */
	private void productSaveFileServer( Map<String,Object> contextMap ) throws Exception {
		try{
			//获取产品信息
			Product product = (Product)contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
			//获取产品图片信息
			File newFile = (File) contextMap.get(StaticVariable.MS_PRODUCT_FILE);
			if( null != newFile ) {//图片存在执行存储图片操作
				//为产品略缩图定义唯一标识
	    		String photo_id = UUID.randomUUID().toString();
	    		product.setPhoto_id(photo_id+".jpg");
				//获取产品图片信息
				saveFile(contextMap, product);
			}else{
				//图片不存在，设定默认值
				product.setPhoto_id(ProductAction.photoPathDef);
			}
		}catch(Exception ex) {
			logger.error("处理产品图片信息失败" , ex);
			throw new Exception("处理产品图片信息失败" , ex);
		} 
	}

	/**
	 * 保存产品图片的工具方法
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
	 * 产品信息保存处理服务
	 * @param contextMap
	 * @throws Exception
	 */
	private void productMessManagerServer(Map<String, Object> contextMap) throws Exception {
		try{
			//获取业务信息
			Product product = (Product)contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
			//初始化ID
			product.setId(UUID.randomUUID().toString());
			//初始化创建时间
			product.setCreate_time( new Date() );
			this.saveEntry(contextMap);
		}catch(Exception ex) {
			logger.error("处理产品信息失败",ex);
			throw new Exception("处理产品信息失败",ex);
		}
		
	} 
	
	public Map<String,Object> updateEntryServer( Map<String,Object> contextMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取产品信息
			Product product = (Product)contextMap.get(StaticVariable.MS_PRODUCT_OBJECT);
			//判断产品信息是否存在
			Map<String,Object> tempProductMap = this.selectEntry4ID(contextMap);
			if( null != tempProductMap ) {
				//更新图片信息
				this.productUpdateFileServer( contextMap );
				//更新产品信息
				this.updateEntry(contextMap);
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "产品信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("更新公示公告信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "产品信息更新失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public Map<String,Object> deleteEntryServer(Map<String, Object> contextMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//判断产品信息是否存在
			Map<String,Object> tempProductMap = this.selectEntry4ID(contextMap);
			Product product = Product.Map2Product(tempProductMap);
			if( null != tempProductMap ) {
				contextMap.put(StaticVariable.MS_PRODUCT_OBJECT, product);
				//删除产品图片信息
				this.productDeleteFileServer(contextMap);
				//删除产品信息
				boolean managerBoolean = this.deleteEntry(contextMap);
				if( managerBoolean ) {
					//执行成功
				}else{
					//执行失败
					returnMap.put(StaticVariable.MANAGER_RESULT, false);
					returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "产品信息删除失败，请重新操作。");
				}
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "产品信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("删除产品信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "产品信息删除失败，请重新操作。");
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