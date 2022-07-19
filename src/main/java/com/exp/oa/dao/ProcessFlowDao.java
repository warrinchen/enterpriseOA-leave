package com.exp.oa.dao;

import com.exp.oa.entity.ProcessFlow;

import java.util.List;

public interface ProcessFlowDao {
    void insert(ProcessFlow processFlow);

    void update(ProcessFlow processFlow);

    List<ProcessFlow> selectByFormId(Long formId);
}
