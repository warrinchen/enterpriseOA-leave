package com.exp.oa.controler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * 跳转到对应的ftl文件
 */
@WebServlet(name = "ForwardServlet", value = "/forward/*")
public class ForwardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        /*
         * /forward/form
         * /forward/a/b/c/form
         */
        String subUri=uri.substring(1);
        String page = subUri.substring(subUri.indexOf("/"));
        request.getRequestDispatcher(page+".ftl").forward(request, response);
        System.out.println("in ForwardServlet: "+uri+"\nto "+page+".ftl");
    }

}
