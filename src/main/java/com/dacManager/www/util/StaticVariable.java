package com.dacManager.www.util;


/**
 * 静态公共常量管理类
 * @author Administrator
 */
public class StaticVariable {
	//公共区域
	/**
	 * 处理结果标识 true表示成功 false表示失败
	 */
	public static final String MANAGER_RESULT = "MANAGER_RESULT";
	/**
	 * 错误处理描述
	 */
	public static final String MANAGER_ERROR_MESSAGE = "MANAGER_ERROR_MESSAGE";
	/**
	 * 错误处理异常对象
	 */
	public static final String MANAGER_ERROR_EXCEPTION = "MANAGER_ERROR_EXCEPTION";
	//用户区域
	/**
	 * 相关表名
	 */
	public static final String TABLE_NAME_USER = "T_MS_USER";
	/**
	 * 交互参数标识，表示用户信息对象的KEY
	 */
	public static final String MS_USER_OBJECT = "OBJECT_USER";
	//公示公告区域
	/**
	 * 相关表名
	 */
	public static final String TABLE_NAME_BULLETIN = "T_WS_BULLETIN";
	/**
	 * 交互参数标识，表示公示公告信息对象KEY
	 */
	public static final String MS_BULLETIN_OBJECT = "OBJECT_BULLETIN";
	/**
	 * 公示公告分页对象标识
	 */
	public static final String PAGE_BULLETIN = "PAGE_BULLETIN";
	//农信要闻区域
	/**
	 * 相关表名
	 */
	public static final String TABLE_NAME_NEWS = "T_WS_NEWS";
	/**
	 *  交互参数标识，表示农信要闻信息对象KEY
	 */
	public static final String MS_NEWS_OBJECT = "OBJECT_NEWS";
	/**
	 * 农信要闻分页对象标识
	 */
	public static final String PAGE_NEWS = "PAGE_NEWS";
	//产品区域
	/**
	 * 相关表名
	 */
	public static final String TABLE_NAME_PRODUCT = "T_WS_PRODUCT";
	/**
	 * 交互参数标识，表示产品信息对象KEY
	 */
	public static final String MS_PRODUCT_OBJECT = "OBJECT_PRODUCT";
	/**
	 * 交互参数标识，表示产品照片对象
	 */
	public static final String MS_PRODUCT_FILE = "MS_PRODUCT_FILE";
	/**
	 * 产品分页对象标识
	 */
	public static final String PAGE_PRODUCT= "PAGE_PRODUCT";
	//业务区域
	/**
	 * 相关表名
	 */
	public static final String TABLE_NAME_SCHEME= "T_WS_SCHEME";
	/**
	 * 交互参数标识，表示业务信息对象KEY
	 */
	public static final String MS_SCHEME_OBJECT = "OBJECT_SCHEME";	
	/**
	 * 业务分页对象标识
	 */
	public static final String PAGE_SCHEME= "PAGE_SCHEME";
	//业务类型区域
	/**
	 * 相关表名
	 */
	public static final String TABLE_NAME_SCHEME_TYPE= "T_WS_SCHEME_TYPE";
	/**
	 * 交互参数标识，表示业务类型信息对象KEY
	 */
	public static final String MS_SCHEME_TYPE_OBJECT = "OBJECT_SCHEME_TYPE";
	/**
	 * 业务类型分页对象标识
	 */
	public static final String PAGE_SCHEME_TYPE= "PAGE_SCHEME_TYPE";
	//图片区域
	/**
	 * 相关表名
	 */
	public static final String TABLE_NAME_PHOTO= "T_PH_PHOTO";
	/**
	 * 交互参数标识，表示图片信息对象KEY 
	 */
	public static final String MS_PHOTO_OBJECT = "OBJECT_PHOTO";
	/**
	 * 交互参数标识，白哦之图片文件对象KEY
	 */
	public static final String MS_PHOTO_FILE_OBJECT = "OBJECT_PHOTO_FILE";
	/**
	 * 图片分页对象标识
	 */
	public static final String PAGE_PHOTO= "PAGE_PHOTO";
	
}
