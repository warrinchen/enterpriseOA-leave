package com.exp.oa.dao;

import com.exp.oa.entity.LeaveForm;
import com.exp.oa.utils.MyBatisUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LeaveFormDaoTest {

    @Test
    public void testInsert() {
        MyBatisUtils.executeUpdate(sqlSession -> {
            LeaveForm leaveForm = new LeaveForm();
            leaveForm.setEmployeeId(4L);
            leaveForm.setFormType(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = sdf.parse("2020-02-25 08:00:00");
                endTime = sdf.parse("2020-02-25 08:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            leaveForm.setStartTime(startTime);
            leaveForm.setEndTime(endTime);
            leaveForm.setReason("回家探亲");
            leaveForm.setCreateTime(new Date());
            leaveForm.setState("processing");
            sqlSession.getMapper(LeaveFormDao.class).insert(leaveForm);
            return null;
        });
    }

    @Test
    public void testSelectByParams() {
        MyBatisUtils.executeQuery(sqlSession -> {
            List<Map> list = sqlSession.getMapper(LeaveFormDao.class)
                    .selectByParams("process", 2L);
            System.out.println(list);
            return list;
        });
    }
}