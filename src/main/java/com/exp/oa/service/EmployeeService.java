package com.exp.oa.service;

import com.exp.oa.dao.EmployeeDao;
import com.exp.oa.entity.Employee;
import com.exp.oa.utils.MyBatisUtils;

public class EmployeeService {
    public Employee selectById(Long employeeId) {
        return (Employee) MyBatisUtils.executeQuery(sqlSession -> {
            //按照传入的接口自动实现类
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            return employeeDao.selectById(employeeId);
        });
    }
}
