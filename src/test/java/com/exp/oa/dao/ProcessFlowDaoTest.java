package com.exp.oa.dao;

import com.exp.oa.entity.ProcessFlow;
import com.exp.oa.utils.MyBatisUtils;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;


public class ProcessFlowDaoTest {

    @Test
    public void testInsert() {
        MyBatisUtils.executeUpdate(sqlSession -> {
            ProcessFlow processFlow = new ProcessFlow();
            processFlow.setFormId(3L);
            processFlow.setOperatorId(2L);
            processFlow.setAction("audit");
            processFlow.setResult("approved");
            processFlow.setReason("同意");
            processFlow.setCreateTime(new Date());
            processFlow.setAuditTime(new Date());
            processFlow.setOrderNo(1);
            processFlow.setState("ready");
            processFlow.setIsLast(1);
            sqlSession.getMapper(ProcessFlowDao.class).insert(processFlow);
            return null;
        });
    }
}