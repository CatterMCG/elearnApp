package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class VersionLet extends HttpServlet{
	private static final long serialVersionUID = 369840050351775312L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
     // 返回版本信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.print("1.0");
        
        out.flush();
        out.close();

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
