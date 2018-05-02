package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class FeedBackLet extends HttpServlet{
	private static final long serialVersionUID = 369840050351775312L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ���տͻ�����Ϣ
        String username = request.getParameter("username");
        /*username = new String(username.getBytes("ISO-8859-1"), "UTF-8");*/
        String suggestion = request.getParameter("suggestion");
        suggestion = java.net.URLDecoder.decode(suggestion,"UTF-8");
        
        System.out.println(username + "--" + suggestion);

        // �½��������
        Service serv = new Service();
        
     // ������Ϣ���ͻ���
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // ��֤����
        boolean flag = serv.FeedBack(username, suggestion);
        if (flag) {
            System.out.print("Succss");
            //�ɹ�����1
            out.print("1");
        } else {
        	//ʧ�ܷ���-1
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
