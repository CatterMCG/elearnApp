package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class FindUserLet extends HttpServlet{
	 private static final long serialVersionUID = 369840050351775312L;

	    public void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        // ���տͻ�����Ϣ
	        String username = request.getParameter("username");
	        /*username = new String(username.getBytes("ISO-8859-1"), "UTF-8");*/
	        //String password = request.getParameter("password");
	        //System.out.println(username + "--" + password);

	        // �½��������
	        Service serv = new Service();
	        
	     // ������Ϣ���ͻ���
	        response.setCharacterEncoding("UTF-8");
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();

	        // �����ֻ����ж��û��Ƿ����
	        boolean flag = serv.FindUser(username);
	        if (flag) {
	            System.out.print("Succss");
	            request.getSession().setAttribute("username", username);
	          //�ɹ���ʱ��ͷ��� 1
	            out.print("1");
	            // response.sendRedirect("welcome.jsp");
	        } else {
	            System.out.print("Failed");
	            //ʧ�ܵ�ʱ�򷵻�-1
	            out.print("-1");
	        }
	        out.flush();
	        out.close();
	    }

	    public void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	    }
}
