package com.exp.oa.controler;

import com.alibaba.fastjson.JSON;
import com.exp.oa.entity.LeaveForm;
import com.exp.oa.entity.User;
import com.exp.oa.service.LeaveFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//*通配符实现一个servlet多个功能
@WebServlet(name = "LeaveFormServlet", value = "/leave/*")
public class LeaveFormServlet extends HttpServlet {
    private LeaveFormService leaveFormService = new LeaveFormService();
    private Logger logger = LoggerFactory.getLogger(LeaveFormServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("test/html;charset=UTF-8");
        // http://localhost/leave/create
        String uri = request.getRequestURI();
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);
        System.out.println("in LeaveFormServlet: url is  "+request.getRequestURL().toString());
        if (methodName.equals("create")) {
            this.create(request, response);
        } else if (methodName.equals("list")) {
            this.getLeaveFormList(request, response);
        } else if (methodName.equals("audit")) {
            this.audit(request, response);
        }
    }

    /**
     * 创建请假单
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 接收各项请假单数据
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("login_user");
        String formType = request.getParameter("formType");
        String strStartTime = request.getParameter("startTime");
        String strEndTime = request.getParameter("endTime");
        String reason = request.getParameter("reason");

        Map result = new HashMap();
        LeaveForm leaveForm = new LeaveForm();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
        try {
            leaveForm.setEmployeeId(user.getEmployeeId());
            leaveForm.setStartTime(sdf.parse(strStartTime));
            leaveForm.setEndTime(sdf.parse(strEndTime));
            leaveForm.setFormType(Integer.parseInt(formType));
            leaveForm.setReason(reason);
            leaveForm.setCreateTime(new Date());
            //2 调用业务逻辑方法
            leaveFormService.createLeaveForm(leaveForm);
            result.put("code", "0");
            result.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("请假申请异常", e);
            result.put("code", e.getClass().getSimpleName());
            result.put("message", e.getMessage());
        }
        //3 组织响应数据
        String json = JSON.toJSONString(result);
        response.getWriter().println(json);
    }// end of create

    /**
     * 查询需要审核的请假单列表
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getLeaveFormList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("login_user");
        List<Map> formList = leaveFormService.getLeaveFormList("process", user.getEmployeeId());
//        System.out.println("in glf \n"+formList);
        Map result = new HashMap();
        result.put("code", "0");
        result.put("msg", "");
        result.put("count", formList.size());
        result.put("data", formList);
        String json = JSON.toJSONString(result);
        response.getWriter().println(json);
//        System.out.println(json);
    }// end of getLeaveFormList

    /**
     * 处理审批操作
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void audit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String formId = request.getParameter("formId");
        String result = request.getParameter("result");
        String reason = request.getParameter("reason");
        User user = (User) request.getSession().getAttribute("login_user");
        Map mpResult = new HashMap();
        try {
            leaveFormService.audit(Long.parseLong(formId), user.getEmployeeId(), result, reason);
            mpResult.put("code", "0");
            mpResult.put("message", "success");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            logger.error("请假单审核失败", e);
            mpResult.put("code", e.getClass().getSimpleName());
            mpResult.put("message", e.getMessage());
        }
        String json = JSON.toJSONString(mpResult);
        response.getWriter().println(json);

    }// end of audit
}
