package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class UserInfoLet extends HttpServlet{
	private static final long serialVersionUID = 369840050351775312L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 接收客户端信息
        String username = request.getParameter("username");
       /* username = new String(username.getBytes("ISO-8859-1"), "UTF-8");*/
        String nickname = request.getParameter("nickname");
        String introduce = request.getParameter("introduce");
        String major = request.getParameter("major");
        String email = request.getParameter("email");
        nickname = java.net.URLDecoder.decode(nickname,"UTF-8");
        introduce = java.net.URLDecoder.decode(introduce,"UTF-8");
        major = java.net.URLDecoder.decode(major,"UTF-8");
        email = java.net.URLDecoder.decode(email,"UTF-8");
        System.out.println(username + "--" + nickname+ "--" + introduce+ "--" + major+ "--" + email);

        // 新建服务对象
        Service serv = new Service();
        
     // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // 验证处理
        boolean flag = serv.UserInfo(username, nickname,introduce,major,email);
        if (flag) {
            System.out.print("Succss");
            //成功返回1
            out.print("1");
        } else {
        	//失败返回-1
        	out.print("-1");
            System.out.print("Failed");
        }
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
