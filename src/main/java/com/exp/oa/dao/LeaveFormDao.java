package com.exp.oa.dao;

import com.exp.oa.entity.LeaveForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LeaveFormDao {
    public void insert(LeaveForm leaveForm);

    List<Map> selectByParams(@Param("pf_state") String pfState, @Param("pf_operator_id") Long operatorId);

    LeaveForm selectById(Long formId);

    void update(LeaveForm form);
}
