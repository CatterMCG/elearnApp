package com.service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.*;

import com.db.DBManager;

public class Service {
	//用户登录操作,要手机号+密码
	 public Boolean login(String username, String password) {
	     String logSql = "select * from users where phone ='" + username
	                + "' and pwd ='" + password + "'";
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();

	     try {
	         ResultSet rs = sql.executeQuery(logSql);
	         if (rs.next()) {
	        	 System.out.print("登录成功");
	             sql.closeDB();
	             return true;
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	     }
	     sql.closeDB();
	     return false;
	 }

	 //用户注册操作，要手机号+密码
	 public Boolean register(String username, String password) {
	     String regSql = "insert into users values('"+ username+ "','"+ password+ "') ";
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();

	     int ret = sql.executeUpdate(regSql);
	     if (ret != 0) {
	    	 
	         System.out.print("插入成功");
	         sql.closeDB();
	         return true;
	     }
	     sql.closeDB();
	        
	     return false;
	     
     }
	 
	 //用户密码修改操作，要手机号+密码
	 public Boolean newPwd(String username, String password) {
	     String pwdSql = "update users set pwd = '" + password + "' where phone = '" + username + "'" ;
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();

	     int ret = sql.executeUpdate(pwdSql);
	     if (ret != 0) {
	         System.out.print("修改成功");
	         sql.closeDB();
	         return true;
	     }
	     sql.closeDB();
	     return false;
	 }
	 
	 //通过phone查找用户
	 public Boolean FindUser(String username) {
	     String logSql = "select * from users where phone ='" + username
	                + "'";
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();

	        // 操作DB对象
	     try {
	         ResultSet rs = sql.executeQuery(logSql);
	         if (rs.next()) {
	        	 System.out.print("查找用户成功");
	             sql.closeDB();
	             return true;
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	     }
	     sql.closeDB();
	     return false;
	 }
	 
	 //搜索课程
	 public String GetCourse(int hard) {
		 String json = null;
	     String logSql = "select * from course_datastructure where hard = " + hard;

	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();
	     try {
	         ResultSet rs = sql.executeQuery(logSql);
	         json = resultSetToJson(rs);
	     } catch (SQLException | JSONException e) {
	         e.printStackTrace();
	     }
	     sql.closeDB();
		return json;
	 }
	 
	 //将返回的数据库信息 改成 json格式
	 public String resultSetToJson(ResultSet rs) throws SQLException,JSONException  
	 {  
	    // json数组  
	    JSONArray array = new JSONArray();  
	     
	    // 获取列数  
	    ResultSetMetaData metaData = rs.getMetaData();  
	    int columnCount = metaData.getColumnCount();  
	     
	    // 遍历ResultSet中的每条数据  
	     while (rs.next()) {  
	         JSONObject jsonObj = new JSONObject();  
	          
	         // 遍历每一列  
	         for (int i = 1; i <= columnCount; i++) {  
	             String columnName =metaData.getColumnLabel(i);  
	             String value = rs.getString(columnName);  
	             jsonObj.put(columnName, value);  
	         }   
	         array.put(jsonObj);   
	     }  
	     
	    return array.toString();  
	 }  
	 
	 //插入用户详细信息
	 public boolean UserInfo(String username, String nickname,String introduce,String major,String email){
		 
		  // 获取Sql插入语句
	     String regSql = "insert into user_info  values('"+ username+ "','"+ nickname+ "','" + introduce + "','" + major + "','" + email + "') ";
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();
	     
	     String finduser = "select * from user_info where phone = '" + username +"'";
	     ResultSet rs = sql.executeQuery(finduser);
	     if(rs != null){
	    	 sql.executeUpdate("delete from user_info where phone = '" + username +"'");
	     }

	     int ret = sql.executeUpdate(regSql);
	     if (ret != 0) {
	         System.out.print("插入成功");
	         sql.closeDB();
	         return true;
	     }
	     sql.closeDB();
	        
	     return false;
	 }
	 
	//插入用户测试信息
		 public boolean SendUserTestInfo(String username, String course,String time,int hard,String scores){
			 
			  // 获取Sql插入语句
		     String regSql = "insert into user_test_info  values('"+ username+ "','"+ course+ "','" + time + "','" + hard + "','" + scores + "') ";
		     DBManager sql = DBManager.createInstance();
		     sql.connectDB();

		     int ret = sql.executeUpdate(regSql);
		     if (ret != 0) {
		         System.out.print("插入成功");
		         sql.closeDB();
		         return true;
		     }
		     sql.closeDB();
		        
		     return false;
		 }
		 
		 //获取用户测试信息
		 public String GetUserTestInfo(String username ){
			 System.out.print("meiyourenhe jiegou ");
			 String json = null;
		     String logSql = "select * from user_test_info where phone = '" + username +"'";

		     DBManager sql = DBManager.createInstance();
		     sql.connectDB();
		     try {
		         ResultSet rs = sql.executeQuery(logSql);
		         System.out.println(rs);
		         json = resultSetToJson(rs);
		         System.out.print(json+"meiyourenhe jiegou ");
		     } catch (SQLException | JSONException e) {
		         e.printStackTrace();
		     }
		     sql.closeDB();
			return json;
		 }
		 
	 
	 //获取用户关注的课程
	 public String GetUserCourseInfo(String username){
		 String regSql = "select * from user_course where phone = '" + username +"'";
		 String json = null;
	     // 获取DB对象
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();
	  // 操作DB对象
	     try {
	         ResultSet rs = sql.executeQuery(regSql);
	         if(rs != null){
	        	 json = resultSetToJson(rs);
	         }
	     } catch (SQLException | JSONException e) {
	         e.printStackTrace();
	     }
	     sql.closeDB();
		return json;
	 }
	 
	//插入用户详细信息
		 public boolean FeedBack(String username,String suggestion){
			  // 获取Sql插入语句
		     String regSql = "insert into user_suggestion  values('"+ username+ "','"+ suggestion+ "')";
		     DBManager sql = DBManager.createInstance();
		     sql.connectDB();

		     int ret = sql.executeUpdate(regSql);
		     if (ret != 0) {
		         System.out.print("插入成功");
		         sql.closeDB();
		         return true;
		     }
		     sql.closeDB();
		        
		     return false;
		 }
	 
	 
	 //更新用户关注的课程
	 public boolean SendUserCourseInfo(String username,String jsonstr) throws JSONException{
		  // 获取Sql插入语句
	     String Sql = "delete from user_course where phone = '" + username +"'";
	        // 获取DB对象
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();
	     //先删除所有的信息
	     sql.executeUpdate(Sql);
	     //添加新的信息
	     JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(jsonstr);
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	     int size = jsonArray.length();
	     String regSql;
	     int ret = 0;
	     for(int i=0;i<size;i++){
	    	 JSONObject jsonObj = jsonArray.getJSONObject(i);
	    	 regSql="insert into user_course  values('"+ jsonObj.getString("username")+ "','"+ jsonObj.getString("course_name") + "') ";
	    	 ret = sql.executeUpdate(regSql);
	     }

	     if (ret != 0) {
	         System.out.print("插入成功");
	         sql.closeDB();
	         return true;
	     }
	     sql.closeDB();
	        
	     return false;
	 }
	 	    
}