package com.dacManager.www.util;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库查询助手 (需要到 Common-DBUtils 包)
 * 
 * @author CaoJia
 */

public class QueryHelper {
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(QueryHelper.class);

	// jdbc支持 getPreparedStatementMetaData 时使用 如果不支持如oracle 构造函数加true参数
	private final static QueryRunner qr = new QueryRunner();
	// 查出记录放入list
	private final static MapListHandler mapListHandler = new MapListHandler();
	// 查出一个Object(count用)
	private static final ScalarHandler scalarHandler = new ScalarHandler();
	// 查出一条记录
	private static final MapHandler mapHandler = new MapHandler();

	/**
	 * 没有where条件,查出一条记录
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> selectSqlForMap(Connection conn,
			String sql, Object[] params) throws Exception {
		return qr.query(conn, sql, mapHandler, params);
	}

	/**
	 * 有where条件,查出一条记录
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> selectSqlForMap(Connection conn,
			String sql) throws Exception {
		return qr.query(conn, sql, mapHandler);
	}

	/**
	 * 无where条件,查出一个Object,统计用
	 * @param conn
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static Object selectSqlForObject(Connection conn, String sql)
			throws Exception {
		return qr.query(conn, sql, scalarHandler);
	}

	/**
	 * 有wherec条件,查出一个Object,统计用
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static Object selectSqlForObject(Connection conn, String sql,
			Object[] params) throws Exception {
		return qr.query(conn, sql, scalarHandler, params);
	}

	/**
	 * 无where条件查询
	 * @param conn
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> selectSql(Connection conn,
			String sql) throws Exception {
		return qr.query(conn, sql, mapListHandler);
	}

	/**
	 * 有where条件查询
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> selectSql(Connection conn,
			String sql, Object[] params) throws Exception {
		return qr.query(conn, sql, mapListHandler, params);
	}

	/**
	 * 更新全部
	 * @param conn
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static int updateSql(Connection conn, String sql) throws Exception {
		int ret = 0;
		ret = qr.update(conn, sql);
		return ret;
	}

	/**
	 * 增、删、改均用此方法(DBUtil用update做增删改操作)
	 * @param conn
	 * @param sql
	 * @param params preparedstatement中的预定义参数的值
	 * @return
	 * @throws Exception
	 */
	public static int updateSql(Connection conn, String sql, Object[] params)
			throws Exception {
		int ret = 0;
		ret = qr.update(conn, sql, params);
		return ret;
	}
	
	
	public static int[] saveSqlBatch(Connection conn, String sql, Object[][] params)
			throws Exception {
		int[] ret = new int[params.length];
		ret = qr.batch(conn, sql, params);
		return ret;
	}

}
