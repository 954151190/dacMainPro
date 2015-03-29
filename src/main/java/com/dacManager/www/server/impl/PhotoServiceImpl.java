package com.dacManager.www.server.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dacManager.www.action.PhotoAction;
import com.dacManager.www.dao.db.rowMapper.PhotoRowMapper;
import com.dacManager.www.dao.db.rowMapper.SchemeRowMapper;
import com.dacManager.www.entry.Page;
import com.dacManager.www.entry.Photo;
import com.dacManager.www.entry.Scheme;
import com.dacManager.www.entry.SchemeType;
import com.dacManager.www.server.IPhotoServer;
import com.dacManager.www.util.BuildSQLUtil;
import com.dacManager.www.util.QueryHelper;
import com.dacManager.www.util.StaticVariable;

public class PhotoServiceImpl implements IPhotoServer{
	private static final Logger logger = LoggerFactory.getLogger(PhotoServiceImpl.class);
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return this .jdbcTemplate;
	}
	
	public void setJdbcTemplate( JdbcTemplate jdbcTemplate ) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * 对外添加图片信息服务方法
	 * @param contextMap
	 */
	public Map<String,Object> saveEntryServer( Map<String,Object> contextMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取居民信息
			Photo photo = (Photo)contextMap.get(StaticVariable.MS_PHOTO_OBJECT);
			//初始化创建时间
			photo.setCreate_time( new Date() );
			this.saveEntry(contextMap);
		}catch(Exception ex) {
			logger.error("图片信息保存失败，失败信息",ex );
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "图片信息保存失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}

	/**
	 * 对外添加图片对象服务方法
	 * @param contextMap
	 * @return
	 */
	public Map<String, Object> saveFileServer(Map<String, Object> contextMap) {
		Map<String,Object> retMap = new HashMap<String,Object>();
		try {
			Photo photo = (Photo)contextMap.get(StaticVariable.MS_PHOTO_OBJECT);
			File inFile = (File)contextMap.get(StaticVariable.MS_PHOTO_FILE_OBJECT);
			File outFile = new File( PhotoAction.photoPuth + photo.getId() + ".jpg" );
			InputStream input = new FileInputStream(inFile);
			OutputStream out = new FileOutputStream(outFile);
			int temp = 0;
			while( ( temp = input.read() ) != -1 ) {
				out.write(temp);
			}
			out.close();
			retMap.put(StaticVariable.MANAGER_RESULT, true);
		} catch (Exception ex) {
			logger.error("图片文件存储失败,",ex);
			retMap.put(StaticVariable.MANAGER_RESULT, false);
			retMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION,ex);
			retMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "图片文件存储失败");
		} 
		return retMap;
	}
	
	 public static byte[] readInputStream(InputStream inStream) throws Exception{  
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	        //创建一个Buffer字符串  
	        byte[] buffer = new byte[1024];  
	        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
	        int len = 0;  
	        //使用一个输入流从buffer里把数据读取出来  
	        while( (len=inStream.read(buffer)) != -1 ){  
	            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
	            outStream.write(buffer, 0, len);  
	        }  
	        //关闭输入流  
	        inStream.close();  
	        //把outStream里的数据写入内存  
	        return outStream.toByteArray();  
	    }  
	
	public Map<String,Object> updateEntryServer( Map<String,Object> contextMap ) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取业务信息
			SchemeType schemeType = (SchemeType)contextMap.get(StaticVariable.MS_SCHEME_TYPE_OBJECT);
			//判断业务信息是否存在
			Map<String,Object> tempEntryMap = this.selectEntry4ID(contextMap);
			if( null != tempEntryMap ) {
				//更新业务信息
				this.updateEntry(contextMap);
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务类型信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("更新业务类型信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "业务类型信息更新失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public Map<String,Object> deleteEntryServer(Map<String, Object> contextMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(StaticVariable.MANAGER_RESULT, true);
		try{
			//获取图片信息
			Photo photo = (Photo)contextMap.get(StaticVariable.MS_PHOTO_OBJECT);
			//判断图片是否存在
			Map<String,Object> tempPhotoMap = this.selectEntry4ID(contextMap);
			if( null != tempPhotoMap ) {
				//删除业务类型信息
				boolean managerBoolean = this.deleteEntry(contextMap);
				if( managerBoolean ) {
					//执行成功
				}else{
					//执行失败
					returnMap.put(StaticVariable.MANAGER_RESULT, false);
					returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "图片信息删除失败，请重新操作。");
				}
			}else{
				returnMap.put(StaticVariable.MANAGER_RESULT, false);
				returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "图片信息不存在，请重新操作。");
			}
		}catch(Exception ex) {
			logger.error("删除图片信息失败,异常信息" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "图片信息删除失败，请重新操作。");
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
		}
		return returnMap;
	}
	
	public Map<String, Object> deleteFileServer(Map<String, Object> contextMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try{
			Photo photo = (Photo)contextMap.get(StaticVariable.MS_PHOTO_OBJECT);
			//获取文件地址
			String filePath = PhotoAction.photoPuth + photo.getId() + ".jpg";
			File deleteFile = new File( filePath );
			if( deleteFile.isFile() && deleteFile.exists() ) {
				deleteFile.delete();
			}
			returnMap.put(StaticVariable.MANAGER_RESULT, true);
		}catch(Exception ex) {
			logger.error("删除图片文件失败。" , ex);
			returnMap.put(StaticVariable.MANAGER_RESULT, false);
			returnMap.put(StaticVariable.MANAGER_ERROR_EXCEPTION, ex);
			returnMap.put(StaticVariable.MANAGER_ERROR_MESSAGE, "删除图片文件失败。");
		}
		return returnMap;
	}
	
	public void saveEntry(Map<String, Object> contextMap) throws Exception {
		Photo photo = (Photo)contextMap.get(StaticVariable.MS_PHOTO_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		fields.add("TITLE");
		fields.add("STATE");
		fields.add("CREATE_TIME");
		fields.add("AUTHOR_NAME");
		fields.add("AUTHOR_ID");
		fields.add("PATH");
		
		values.add(photo.getId());
		values.add(photo.getTitle());
		values.add(photo.getState());
		values.add( new java.sql.Date( photo.getCreate_time().getTime() ) );
		values.add(photo.getAuthor_name());
		values.add(photo.getAuthor_id());
		values.add(photo.getPath());
		
		String insertSql = BuildSQLUtil.buildSaveSQL(StaticVariable.TABLE_NAME_PHOTO, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		QueryHelper.updateSql(conn, insertSql, values.toArray()); 
	}

	
	
	public boolean updateEntry(Map<String, Object> contextMap) throws Exception {
		SchemeType schemeType = (SchemeType)contextMap.get(StaticVariable.MS_SCHEME_TYPE_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("TITLE");
		fields.add("CONTENT");
		
		whereFields.add("ID");
		
		values.add(schemeType.getTitle());
		values.add(schemeType.getContent());
		values.add(schemeType.getId());
		
		String insertSql = BuildSQLUtil.buildUpdateWithConditionSQL(StaticVariable.TABLE_NAME_SCHEME_TYPE, fields.toArray(), whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, insertSql, values.toArray()); 
		if(i != 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean deleteEntry(Map<String, Object> contextMap) throws Exception {
		Photo photo = (Photo)contextMap.get(StaticVariable.MS_PHOTO_OBJECT);
		List<Object> whereFields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		whereFields.add("ID");
		values.add(photo.getId());
		String deleteSQL = BuildSQLUtil.buildDeleteWithCondtionSQL(StaticVariable.TABLE_NAME_PHOTO, whereFields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		int i = QueryHelper.updateSql(conn, deleteSQL, values.toArray());
		if( i == 1 ) {
			return true;
		}else{
			return false;
		}
	}

	public Map<String, Object> selectEntry4ID(Map<String, Object> contextMap) throws Exception{
		Photo photo = (Photo)contextMap.get(StaticVariable.MS_PHOTO_OBJECT);
		List<Object> fields = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		fields.add("ID");
		values.add(photo.getId());
		String selectSQL = BuildSQLUtil.buildSelectAllFieldsWithConditionSQL(StaticVariable.TABLE_NAME_PHOTO, fields.toArray());
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Map<String,Object> tempMap = QueryHelper.selectSqlForMap(conn, selectSQL,values.toArray());
		return tempMap;
	}
	
	public List<Photo> selectEntryList4Page(Map<String, Object> contextMap) {
		Page page = (Page)contextMap.get(StaticVariable.PAGE_SCHEME);
		//计算最大序号
		int numberMax = page.getCount() * page.getNumber();
		//计算最小序号
		int numberMin = (page.getNumber()-1) * page.getCount();
		String sql = "SELECT * FROM ( SELECT A.*, ROWNUM RN FROM ( select * from " + StaticVariable.TABLE_NAME_PHOTO + " ) A WHERE ROWNUM <= "+numberMax+" ) WHERE RN > "+numberMin+"";
		List<Photo> photoList = jdbcTemplate.query(sql , new PhotoRowMapper());
		return photoList;
	}
	
	/**
	 * 查询信息个数
	 * @param contextMap
	 */
	public Long countEntry( Map<String,Object> contextMap ){
		String countStr = BuildSQLUtil.buildCountAllSQL( StaticVariable.TABLE_NAME_PHOTO );
		Long l = jdbcTemplate.queryForLong(countStr);
		return l;
	}
}