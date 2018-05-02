package com.service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.*;

import com.db.DBManager;

public class Service {
	//�û���¼����,Ҫ�ֻ���+����
	 public Boolean login(String username, String password) {
	     String logSql = "select * from users where phone ='" + username
	                + "' and pwd ='" + password + "'";
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();

	     try {
	         ResultSet rs = sql.executeQuery(logSql);
	         if (rs.next()) {
	        	 System.out.print("��¼�ɹ�");
	             sql.closeDB();
	             return true;
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	     }
	     sql.closeDB();
	     return false;
	 }

	 //�û�ע�������Ҫ�ֻ���+����
	 public Boolean register(String username, String password) {
	     String regSql = "insert into users values('"+ username+ "','"+ password+ "') ";
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();

	     int ret = sql.executeUpdate(regSql);
	     if (ret != 0) {
	    	 
	         System.out.print("����ɹ�");
	         sql.closeDB();
	         return true;
	     }
	     sql.closeDB();
	        
	     return false;
	     
     }
	 
	 //�û������޸Ĳ�����Ҫ�ֻ���+����
	 public Boolean newPwd(String username, String password) {
	     String pwdSql = "update users set pwd = '" + password + "' where phone = '" + username + "'" ;
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();

	     int ret = sql.executeUpdate(pwdSql);
	     if (ret != 0) {
	         System.out.print("�޸ĳɹ�");
	         sql.closeDB();
	         return true;
	     }
	     sql.closeDB();
	     return false;
	 }
	 
	 //ͨ��phone�����û�
	 public Boolean FindUser(String username) {
	     String logSql = "select * from users where phone ='" + username
	                + "'";
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();

	        // ����DB����
	     try {
	         ResultSet rs = sql.executeQuery(logSql);
	         if (rs.next()) {
	        	 System.out.print("�����û��ɹ�");
	             sql.closeDB();
	             return true;
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	     }
	     sql.closeDB();
	     return false;
	 }
	 
	 //�����γ�
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
	 
	 //�����ص����ݿ���Ϣ �ĳ� json��ʽ
	 public String resultSetToJson(ResultSet rs) throws SQLException,JSONException  
	 {  
	    // json����  
	    JSONArray array = new JSONArray();  
	     
	    // ��ȡ����  
	    ResultSetMetaData metaData = rs.getMetaData();  
	    int columnCount = metaData.getColumnCount();  
	     
	    // ����ResultSet�е�ÿ������  
	     while (rs.next()) {  
	         JSONObject jsonObj = new JSONObject();  
	          
	         // ����ÿһ��  
	         for (int i = 1; i <= columnCount; i++) {  
	             String columnName =metaData.getColumnLabel(i);  
	             String value = rs.getString(columnName);  
	             jsonObj.put(columnName, value);  
	         }   
	         array.put(jsonObj);   
	     }  
	     
	    return array.toString();  
	 }  
	 
	 //�����û���ϸ��Ϣ
	 public boolean UserInfo(String username, String nickname,String introduce,String major,String email){
		 
		  // ��ȡSql�������
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
	         System.out.print("����ɹ�");
	         sql.closeDB();
	         return true;
	     }
	     sql.closeDB();
	        
	     return false;
	 }
	 
	//�����û�������Ϣ
		 public boolean SendUserTestInfo(String username, String course,String time,int hard,String scores){
			 
			  // ��ȡSql�������
		     String regSql = "insert into user_test_info  values('"+ username+ "','"+ course+ "','" + time + "','" + hard + "','" + scores + "') ";
		     DBManager sql = DBManager.createInstance();
		     sql.connectDB();

		     int ret = sql.executeUpdate(regSql);
		     if (ret != 0) {
		         System.out.print("����ɹ�");
		         sql.closeDB();
		         return true;
		     }
		     sql.closeDB();
		        
		     return false;
		 }
		 
		 //��ȡ�û�������Ϣ
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
		 
	 
	 //��ȡ�û���ע�Ŀγ�
	 public String GetUserCourseInfo(String username){
		 String regSql = "select * from user_course where phone = '" + username +"'";
		 String json = null;
	     // ��ȡDB����
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();
	  // ����DB����
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
	 
	//�����û���ϸ��Ϣ
		 public boolean FeedBack(String username,String suggestion){
			  // ��ȡSql�������
		     String regSql = "insert into user_suggestion  values('"+ username+ "','"+ suggestion+ "')";
		     DBManager sql = DBManager.createInstance();
		     sql.connectDB();

		     int ret = sql.executeUpdate(regSql);
		     if (ret != 0) {
		         System.out.print("����ɹ�");
		         sql.closeDB();
		         return true;
		     }
		     sql.closeDB();
		        
		     return false;
		 }
	 
	 
	 //�����û���ע�Ŀγ�
	 public boolean SendUserCourseInfo(String username,String jsonstr) throws JSONException{
		  // ��ȡSql�������
	     String Sql = "delete from user_course where phone = '" + username +"'";
	        // ��ȡDB����
	     DBManager sql = DBManager.createInstance();
	     sql.connectDB();
	     //��ɾ�����е���Ϣ
	     sql.executeUpdate(Sql);
	     //����µ���Ϣ
	     JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(jsonstr);
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
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
	         System.out.print("����ɹ�");
	         sql.closeDB();
	         return true;
	     }
	     sql.closeDB();
	        
	     return false;
	 }
	 	    
}