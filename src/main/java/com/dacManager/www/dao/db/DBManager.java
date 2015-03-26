package com.dacManager.www.dao.db;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 数据库管理类
 */
public class DBManager {

	private final static Logger log = LoggerFactory.getLogger(DBManager.class);
	private final static ThreadLocal<Connection> conns = new ThreadLocal<Connection>();
	private static DataSource dataSource;
	private static boolean show_sql = true;
	
	/**
	 * 断开连接池
	 */
	public final static void closeDataSource(){
		try {
			dataSource.getClass().getMethod("close").invoke(dataSource);
		} catch (NoSuchMethodException e){ 
		} catch (Exception e) {
			log.error("Unabled to destroy DataSource!!! ", e);
		}
	}

	public final static Connection getConnection() throws SQLException {
		Connection conn = conns.get();
		if(conn ==null || conn.isClosed()){
			conn = dataSource.getConnection();
			conns.set(conn);
		}
		return (show_sql && !Proxy.isProxyClass(conn.getClass()))?
                      new _DebugConnection(conn).getConnection():conn;
	}
	
	/**
	 * 关闭连接
	 */
	public final static void closeConnection() {
		Connection conn = conns.get();
		try {
			if(conn != null && !conn.isClosed()){
				conn.setAutoCommit(true);
				conn.close();
			}
		} catch (SQLException e) {
			log.error("Unabled to close connection!!! ", e);
		}
		conns.set(null);
	}

	/**
	 * 用于跟踪执行的SQL语句
	 * 
	 */
	static class _DebugConnection implements InvocationHandler {
		
		private final static Logger log = LoggerFactory.getLogger(_DebugConnection.class);
		
		private Connection conn = null;

		public _DebugConnection(Connection conn) {
			this.conn = conn;
		}

		/**
		 * Returns the conn.
		 * @return Connection
		 */
		public Connection getConnection() {
			return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), 
                             conn.getClass().getInterfaces(), this);
		}
		
		public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
			try {
				String method = m.getName();
				if("prepareStatement".equals(method) || "createStatement".equals(method))
					log.info("[SQL] >>> " + args[0]);				
				return m.invoke(conn, args);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}
	}

	public static DataSource getDataSource() {
		return dataSource;
	}

	public static void setDataSource(DataSource dataSource) {
		DBManager.dataSource = dataSource;
	}
}
