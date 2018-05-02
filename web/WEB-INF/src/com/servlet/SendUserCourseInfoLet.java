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

        // ���տͻ�����Ϣ
        String jsonstr = request.getParameter("jsonstr");
        System.out.println(jsonstr);
        /*jsonstr = new String(jsonstr.getBytes("ISO-8859-1"), "UTF-8");*/
        jsonstr = java.net.URLDecoder.decode(jsonstr,"UTF-8"); 
        System.out.println(jsonstr);
        String username = request.getParameter("username");

        // �½��������
        Service serv = new Service();
        
     // ������Ϣ���ͻ���
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // �����ֻ����ж��û��Ƿ����
        boolean flag = false;
		try {
			flag = serv.SendUserCourseInfo(username,jsonstr);
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
        if (flag) {
            System.out.print("Succss");
            request.getSession().setAttribute("username", jsonstr);
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
