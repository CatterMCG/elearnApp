package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class PwdLet extends HttpServlet{
	private static final long serialVersionUID = 369840050351775312L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 接收客户端信息
    	request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
       /* username = new String(username.getBytes("ISO-8859-1"), "UTF-8");*/
        String password = request.getParameter("password");
        System.out.println("用户名：" + username + "密码：" + password);

        // 新建服务对象
        Service serv = new Service();
        
        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // 插入处理
        boolean flag = serv.newPwd(username, password);
        if (flag) {
            System.out.print("Succss");
            request.getSession().setAttribute("username", username);
          //插入成功的时候就返回 1
            out.print("1");
            // response.sendRedirect("welcome.jsp");
        } else {
            System.out.print("Failed");
            //插入失败的时候返回-1
            out.print("-1");
        }
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
