package com.dacManager.www.util;

/**
 * 配置文件applicationContext_config.xml对应的类
 * @author Administrator
 */
public class Config {
	/**
	 * 首页滚动图片存储路径
	 */
	public static String INDEX_PHOTO_PATH = "";
	/**
	 * 产品略缩图存储路径
	 */
	public static String PRODUCT_PHOTO_PATH = "";
	
	/**
	 * 产品略缩图默认展示图片的存储路径(临时)
	 */
	public static String PRODUCT_PHOTO_PATH_DEF  = "";
	
	public String getPRODUCT_PHOTO_PATH_DEF() {
		return PRODUCT_PHOTO_PATH_DEF;
	}
	public void setPRODUCT_PHOTO_PATH_DEF(String pRODUCT_PHOTO_PATH_DEF) {
		PRODUCT_PHOTO_PATH_DEF = pRODUCT_PHOTO_PATH_DEF;
	}
	public String getINDEX_PHOTO_PATH() {
		return INDEX_PHOTO_PATH;
	}
	public void setINDEX_PHOTO_PATH(String iNDEX_PHOTO_PATH) {
		INDEX_PHOTO_PATH = iNDEX_PHOTO_PATH;
	}
	public String getPRODUCT_PHOTO_PATH() {
		return PRODUCT_PHOTO_PATH;
	}
	public void setPRODUCT_PHOTO_PATH(String pRODUCT_PHOTO_PATH) {
		PRODUCT_PHOTO_PATH = pRODUCT_PHOTO_PATH;
	}
}
