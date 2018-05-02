package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class GetCourseLet extends HttpServlet{
	private static final long serialVersionUID = 369840050351775312L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ���տͻ�����Ϣ,��ʱ���ý�����Ϣ��ֻ��һ���γ̣�֮���ٱ��м�һ�����Կγ�������ʱ����γ���������Ӧ�Ŀγ̶���������
        
    	int hard = 1;
    	String s = request.getParameter("hard");
    	System.out.println(s);
    	hard = Integer.parseInt(request.getParameter("hard"));

        // �½��������
        Service serv = new Service();
        
     // ������Ϣ���ͻ���
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String course = serv.GetCourse(hard);
        out.print(course);
        
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
