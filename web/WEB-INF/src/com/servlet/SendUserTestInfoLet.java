package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class SendUserTestInfoLet extends HttpServlet{
	private static final long serialVersionUID = 369840050351775312L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ���տͻ�����Ϣ
        String username = request.getParameter("username");
        username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
        String course = request.getParameter("course");
        String time = request.getParameter("time");
        int hard = Integer.parseInt(request.getParameter("hard"));
        String scores = request.getParameter("scores");
        
        course = java.net.URLDecoder.decode(course,"UTF-8");
        time = java.net.URLDecoder.decode(time,"UTF-8");
        scores = java.net.URLDecoder.decode(scores,"UTF-8");
        System.out.println(username + "--" + course+ "--" + time+ "--" + hard+ "--" + scores);

        // �½��������
        Service serv = new Service();
        
     // ������Ϣ���ͻ���
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // ��֤����
        boolean flag = serv.SendUserTestInfo(username, course,time,hard,scores);
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
