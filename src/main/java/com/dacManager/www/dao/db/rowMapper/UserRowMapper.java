package com.dacManager.www.dao.db.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dacManager.www.entry.User;

public class UserRowMapper implements RowMapper{
	public UserRowMapper() {
		// TODO Auto-generated constructor stub
	}
	
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId( rs.getString("ID") );
		user.setUser_name(rs.getString("USER_NAME"));
		user.setUser_password(rs.getString("USER_PASSWORD"));
		user.setUser_role(rs.getString("USER_ROLE"));
		user.setName(rs.getString("NAME"));
		user.setRemark(rs.getString("REMARK"));
		user.setAge(rs.getString("AGE"));
		user.setState(rs.getString("STATE"));
		user.setCreate_time(new java.util.Date( rs.getDate("CREATE_TIME").getTime() ) );
		return user;
	}
}
