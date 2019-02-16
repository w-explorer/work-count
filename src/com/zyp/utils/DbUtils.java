package com.zyp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtils {					
	static String dirverClass = "com.mysql.jdbc.Driver";
	static String url = "jdbc:mysql://localhost:3306/demo01";
	static String user ="root" ;
	static String password = "";
	//创建链接数据库
	public static Connection getConnection() throws Exception{
		//加载驱动器
		Class.forName(dirverClass);
		//链接数据库
		Connection conn = DriverManager.getConnection(url, user, password);
		
		return conn;
		
	}
	//关流
	public static void closeAll(ResultSet rs,Statement stmt,Connection conn){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs=null;
		}
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt=null;
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn=null;
		}
	}
//	public static void main(String[] args) {
//		Connection conn= null;
//		try {
//			 conn =getConnection();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(conn);
//	}
}
