package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.service.Service;

public class SendUserCourseInfoLet extends HttpServlet{
	private static final long serialVersionUID = 369840050351775312L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 接收客户端信息
        String jsonstr = request.getParameter("jsonstr");
        System.out.println(jsonstr);
        /*jsonstr = new String(jsonstr.getBytes("ISO-8859-1"), "UTF-8");*/
        jsonstr = java.net.URLDecoder.decode(jsonstr,"UTF-8"); 
        System.out.println(jsonstr);
        String username = request.getParameter("username");

        // 新建服务对象
        Service serv = new Service();
        
     // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // 根据手机号判断用户是否存在
        boolean flag = false;
		try {
			flag = serv.SendUserCourseInfo(username,jsonstr);
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
        if (flag) {
            System.out.print("Succss");
            request.getSession().setAttribute("username", jsonstr);
          //成功的时候就返回 1
            out.print("1");
            // response.sendRedirect("welcome.jsp");
        } else {
            System.out.print("Failed");
            //失败的时候返回-1
            out.print("-1");
        }
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
