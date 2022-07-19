package com.exp.oa.service;

import com.exp.oa.entity.LeaveForm;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class LeaveFormServiceTest {
    LeaveFormService leaveFormService = new LeaveFormService();
    /**
     * 市场部员工请假单(72小时)以上测试用例
     * @throws ParseException
     */
    @Test
    public void testCreateLeaveForm1() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        LeaveForm leaveForm = new LeaveForm();
        leaveForm.setEmployeeId(8L);
        leaveForm.setStartTime(sdf.parse("2020032608"));
        leaveForm.setEndTime(sdf.parse("2020040118"));
        leaveForm.setFormType(1);
        leaveForm.setReason("市场部请假单(72h以上)");
        leaveForm.setCreateTime(new Date());
        LeaveForm savedForm = leaveFormService.createLeaveForm(leaveForm);
        System.out.println(savedForm.getFormId());
    }

    /**
     * 市场部员工请假单(72小时)以内测试用例
     * @throws ParseException
     */
    @Test
    public void testCreateLeaveForm2() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        LeaveForm leaveForm = new LeaveForm();
        leaveForm.setEmployeeId(8L);
        leaveForm.setStartTime(sdf.parse("2020032608"));
        leaveForm.setEndTime(sdf.parse("2020032718"));
        leaveForm.setFormType(1);
        leaveForm.setReason("市场部请假单(72h以内)");
        leaveForm.setCreateTime(new Date());
        LeaveForm savedForm = leaveFormService.createLeaveForm(leaveForm);
        System.out.println(savedForm.getFormId());
    }

    /**
     * 研发部经理请假单测试用例
     * @throws ParseException
     */
    @Test
    public void testCreateLeaveForm3() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        LeaveForm leaveForm = new LeaveForm();
        leaveForm.setEmployeeId(2L);
        leaveForm.setStartTime(sdf.parse("2020032608"));
        leaveForm.setEndTime(sdf.parse("2020040118"));
        leaveForm.setFormType(1);
        leaveForm.setReason("研发部部门经理请假单)");
        leaveForm.setCreateTime(new Date());
        LeaveForm savedForm = leaveFormService.createLeaveForm(leaveForm);
        System.out.println(savedForm.getFormId());
    }

    /**
     * 总经理请假单测试用例
     * @throws ParseException
     */
    @Test
    public void testCreateLeaveForm4() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        LeaveForm leaveForm = new LeaveForm();
        leaveForm.setEmployeeId(1L);
        leaveForm.setStartTime(sdf.parse("2020032608"));
        leaveForm.setEndTime(sdf.parse("2020040118"));
        leaveForm.setFormType(1);
        leaveForm.setReason("总经理请假单)");
        leaveForm.setCreateTime(new Date());
        LeaveForm savedForm = leaveFormService.createLeaveForm(leaveForm);
        System.out.println(savedForm.getFormId());
    }

    @Test
    public void testAudit1(){
        leaveFormService.audit(31L, 2L, "approved", "祝愿早日康复");
    }
    @Test
    public void testAudit2(){
        leaveFormService.audit(32L, 2L, "refused", "不同意");
    }
    @Test
    public void testAudit3(){
        leaveFormService.audit(33L, 1L, "approved", "同意");
    }
}