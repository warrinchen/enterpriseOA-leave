package com.exp.oa.dao;

import com.exp.oa.entity.Department;

public interface DepartmentDao {
    Department selectById(Long departmentId);
}
