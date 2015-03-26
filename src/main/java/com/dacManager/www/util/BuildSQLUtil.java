package com.dacManager.www.util;

import java.util.Map;

public final class BuildSQLUtil {

	private static final String SPACE = " ";
	private static final String EQ = "=";
	private static final String NOTEQ = "<>";
	private static final String QUESTION_MARK = "?";
	private static final String AND = "and";
	private static final String OR = "or";
	private static final String SEPERATOR = ",";
	private static final String ORDER_BY = "order by";
	private static final String ASC = "asc";
	private static final String DESC = "desc";

	/**
	 * build count all sql without where condition
	 * 
	 * @param tableName
	 * @return
	 */
	public static String buildCountAllSQL(String tableName) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) ");
		sql.append("from ");
		sql.append(tableName);
		return sql.toString();
	}

	/**
	 * build count all sql with where condition
	 * 
	 * @param tableName
	 * @param params
	 * @return
	 */
	public static String buildCountSQL(String tableName, Object[] whereCondition) {
		StringBuffer sql = new StringBuffer(buildCountAllSQL(tableName));
		sql.append(SPACE).append("where ");
		andWhereConditon(sql, whereCondition);
		return sql.toString();
	}

	/**
	 * link where condition with and
	 * 
	 * @param whereCondition
	 */
	private static void andWhereConditon(StringBuffer sql,
			Object[] whereCondition) {
		int length = whereCondition.length;
		for (int i = 0; i < length; i++) {
			sql.append(whereCondition[i]).append(SPACE).append(EQ)
					.append(SPACE).append(QUESTION_MARK).append(SPACE);
			if (i != length - 1)
				sql.append(AND).append(SPACE);
		}
	}

	/**
	 * query all fields without where condition
	 * 
	 * @param tableName
	 * @return
	 */
	public static String buildSelectAllFieldsWithoutConditionSQL(
			String tableName) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * ");
		sql.append("from ");
		sql.append(tableName);
		return sql.toString();
	}

	/**
	 * build query all fields with where condition
	 * 
	 * @param tableName
	 * @param whereCondition
	 * @return
	 */
	public static String buildSelectAllFieldsWithConditionSQL(String tableName,
			Object[] whereCondition) {
		StringBuffer sql = new StringBuffer(
				buildSelectAllFieldsWithoutConditionSQL(tableName));
		sql.append(SPACE).append("where ");
		andWhereConditon(sql, whereCondition);
		return sql.toString();
	}

	/**
	 * build special fields query sql without wherecondition
	 * 
	 * @param tableName
	 * @param fields
	 * @return
	 */
	public static String buildSelectFieldsWithoutConditionSQL(String tableName,
			Object[] fields) {
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		linkFields(sql, fields);
		sql.append(SPACE).append("from ");
		sql.append(tableName);
		return sql.toString();
	}

	/**
	 * build special fields query sql with wherecondition
	 * 
	 * @param tableName
	 * @param fields
	 * @param whereCondition
	 * @return
	 */
	public static String buildSelectFieldsWithConditionSQL(String tableName,
			Object[] fields, Object[] whereCondition) {
		StringBuffer sql = new StringBuffer(
				buildSelectFieldsWithoutConditionSQL(tableName, fields));
		sql.append(SPACE).append("where ");
		andWhereConditon(sql, whereCondition);
		return sql.toString();
	}

	/**
	 * link field with ',' symbol
	 * 
	 * @param sql
	 * @param fields
	 */
	private static void linkFields(StringBuffer sql, Object[] fields) {
		int length = fields.length;
		for (int i = 0; i < length; i++) {
			sql.append(fields[i]).append(SPACE);
			if (i != length - 1)
				sql.append(",").append(SPACE);
		}
	}

	/**
	 * build insert sql
	 * 
	 * @param tableName
	 * @param fields
	 * @return
	 */
	public static String buildSaveSQL(String tableName, Object[] fields) {
		StringBuffer sql = new StringBuffer();
		StringBuffer values = new StringBuffer();
		sql.append("insert into ");
		sql.append(tableName).append("(");
		linkFields(sql, fields);
		sql.append(") ");
		sql.append("values(");
		for (int i = 0; i < fields.length; i++) {
			values.append(QUESTION_MARK);
			if (i != fields.length - 1)
				values.append(",");
		}
		sql.append(values);
		sql.append(")");
		return sql.toString();
	}

	/**
	 * build update sql without wherecondition
	 * 
	 * @param tableName
	 * @param fields
	 * @return
	 */
	public static String buildUpdateWithoutConditionSQL(String tableName,
			Object[] fields) {
		StringBuffer sql = new StringBuffer();
		sql.append("update ");
		sql.append(tableName).append(SPACE);
		sql.append("set ");
		for (int i = 0; i < fields.length; i++) {
			sql.append(fields[i]).append(EQ).append(QUESTION_MARK);
			if (i != fields.length - 1)
				sql.append(",");
		}
		return sql.toString();
	}

	/**
	 * build update sql with where condition
	 * 
	 * @param tableName
	 * @param fields
	 * @param whereCondition
	 * @return
	 */
	public static String buildUpdateWithConditionSQL(String tableName,
			Object[] fields, Object[] whereCondition) {
		StringBuffer sql = new StringBuffer(buildUpdateWithoutConditionSQL(
				tableName, fields));
		sql.append(SPACE).append("where ");
		andWhereConditon(sql, whereCondition);
		return sql.toString();
	}

	/**
	 * build delete sql without where condition
	 * 
	 * @param tableName
	 * @return
	 */
	public static String buildDeleteWithoutConditionSQL(String tableName) {
		StringBuffer sql = new StringBuffer();
		sql.append("delete ");
		sql.append("from ").append(tableName).append(SPACE);
		return sql.toString();
	}

	/**
	 * build delete sql with where condition
	 * 
	 * @param tableName
	 * @param whereCondition
	 * @return
	 */
	public static String buildDeleteWithCondtionSQL(String tableName,
			Object[] whereCondition) {
		StringBuffer sql = new StringBuffer(
				buildDeleteWithoutConditionSQL(tableName));
		sql.append(SPACE).append("where ");
		andWhereConditon(sql, whereCondition);
		return sql.toString();
	}
	
	/**
	 * 在sql语句后面加上order by条件
	 * @param sql
	 * @param order key字段名 value 升降序标志
	 * @return
	 */
	public static String addOrderByInTheSelectEnd(String sql, Map<String, String> order) {
		StringBuffer sqltemp = new StringBuffer(sql);
		for(String field : order.keySet()){
			sqltemp.append(SPACE).append(ORDER_BY).append(SPACE).append(field).append(SPACE).append(order.get(field)).append(SPACE);
		}
		return sqltemp.toString();
	}
}
