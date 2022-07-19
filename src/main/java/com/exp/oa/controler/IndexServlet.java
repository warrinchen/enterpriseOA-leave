package com.exp.oa.controler;

import com.exp.oa.entity.Department;
import com.exp.oa.entity.Employee;
import com.exp.oa.entity.Node;
import com.exp.oa.entity.User;
import com.exp.oa.service.DepartmentService;
import com.exp.oa.service.EmployeeService;
import com.exp.oa.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "IndexServlet", value = "/index")
public class IndexServlet extends HttpServlet {
    private UserService userService = new UserService();
    private EmployeeService employeeService = new EmployeeService();
    private DepartmentService departmentService = new DepartmentService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("login_user");
        Employee employee = employeeService.selectById(user.getEmployeeId());
        Department department = departmentService.selectById(employee.getDepartmentId());
        List<Node> nodeList = userService.selectNodeByUserId(user.getUserId());
        request.setAttribute("node_list", nodeList);

        session.setAttribute("current_employee", employee);
        session.setAttribute("current_department", department);
        request.getRequestDispatcher("/index.ftl").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
