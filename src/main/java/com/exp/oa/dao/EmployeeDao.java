package com.exp.oa.dao;

import com.exp.oa.entity.Employee;
import org.apache.ibatis.annotations.Param;

public interface EmployeeDao {
    Employee selectById(Long employeeId);

    /**
     * 根据传入员工对象获取上级主管对象
     * @param employee 员工对象
     * @return 上级主管对象
     */
    Employee selectLeader(@Param("emp") Employee employee);//注解表示 在xml中employee的名称为emp
}
