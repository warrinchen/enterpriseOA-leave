package com.exp.oa.service;

import com.exp.oa.dao.DepartmentDao;
import com.exp.oa.entity.Department;
import com.exp.oa.utils.MyBatisUtils;

public class DepartmentService {
    public Department selectById(Long departmentId) {
        return (Department) MyBatisUtils.executeQuery(sqlSession -> sqlSession.getMapper(DepartmentDao.class).selectById(departmentId));
    }
}
