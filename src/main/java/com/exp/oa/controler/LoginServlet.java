package com.exp.oa.controler;

import com.alibaba.fastjson.JSON;
import com.exp.oa.entity.User;
import com.exp.oa.service.UserService;
import com.exp.oa.service.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = "/check_login")
public class LoginServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private UserService userService = new UserService();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //1 接收用户输入
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Map<String, Object> result = new HashMap<>();
        try {
            //2 调用业务逻辑
            User user = userService.checkLogin(username, password);
            HttpSession session = request.getSession();
            //向session存入登陆用户信息, 属性名:login_user
            session.setAttribute("login_user", user);
            result.put("code", "0");
            result.put("message", "success login");
            result.put("redirect_url", "/index");
        } catch (BusinessException be) {
            logger.error(be.getMessage(), be);
            result.put("code", be.getCode());
            result.put("message", be.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("code", e.getClass());
            result.put("message", e.getMessage());
        }
        //3 返回对应结果
        String json = JSON.toJSONString(result);
        response.getWriter().println(json);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
    }
}
