package com.exp.oa.service;

import com.exp.oa.dao.EmployeeDao;
import com.exp.oa.dao.LeaveFormDao;
import com.exp.oa.dao.NoticeDao;
import com.exp.oa.dao.ProcessFlowDao;
import com.exp.oa.entity.Employee;
import com.exp.oa.entity.LeaveForm;
import com.exp.oa.entity.Notice;
import com.exp.oa.entity.ProcessFlow;
import com.exp.oa.service.exception.BusinessException;
import com.exp.oa.utils.MyBatisUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 请假单流程服务
 */
public class LeaveFormService {
    /**
     * 创建请假单
     * @param leaveForm 前端输入的请假单数据
     * @return 持久化后的请假单对象
     */
    public LeaveForm createLeaveForm(LeaveForm  leaveForm){
        return (LeaveForm) MyBatisUtils.executeUpdate(sqlSession -> {
            //1 持久化form表单数据, 8级一下表单状态为processing, 8级(总经理)状态为approved
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            Employee employee = employeeDao.selectById(leaveForm.getEmployeeId());
            if (employee.getLevel() == 8) {
                leaveForm.setState("approved");
            }else{
                leaveForm.setState("processing");
            }
            sqlSession.getMapper(LeaveFormDao.class).insert(leaveForm);

            //2 增加第一条流程数据, 说明表单已经提交, 状态为complete
            ProcessFlow processFlow1 = new ProcessFlow();
            processFlow1.setFormId(leaveForm.getFormId());
            processFlow1.setOperatorId(employee.getEmployeeId());
            processFlow1.setAction("apply");
            processFlow1.setCreateTime(new Date());
            processFlow1.setOrderNo(1);
            processFlow1.setState("complete");
            processFlow1.setIsLast(0);
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            processFlowDao.insert(processFlow1);
            //3 分情况创建其余流程数据
            // 3.1 7级以下员工, 生成部门经理审批任务, 请假时间大于36小时, 还需要生成总经理审批任务
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            if (employee.getLevel() < 7) {
                Employee dmanager = employeeDao.selectLeader(employee);
                ProcessFlow processFlow2 = new ProcessFlow();
                processFlow2.setFormId(leaveForm.getFormId());
                processFlow2.setOperatorId(dmanager.getEmployeeId());
                processFlow2.setAction("audit");
                processFlow2.setCreateTime(new Date());
                processFlow2.setOrderNo(2);
                processFlow2.setState("process");
                long diff = leaveForm.getEndTime().getTime() - leaveForm.getStartTime().getTime();
                float hours = diff / (1000 * 60 * 60) * 1f;
                if (hours >= BusinessConstants.MANAGER_AUDIT_HOURS) {
                    processFlow2.setIsLast(0);
                    processFlowDao.insert(processFlow2);

                    Employee manager = employeeDao.selectLeader(dmanager);
                    ProcessFlow processFlow3 = new ProcessFlow();
                    processFlow3.setFormId(leaveForm.getFormId());
                    processFlow3.setOperatorId(manager.getEmployeeId());
                    processFlow3.setAction("audit");
                    processFlow3.setCreateTime(new Date());
                    processFlow3.setOrderNo(2);
                    processFlow3.setState("ready");//ready:等待上一级流程结束
                    processFlow3.setIsLast(1);
                    processFlowDao.insert(processFlow3);
                }else{
                    processFlow2.setIsLast(1);
                    processFlowDao.insert(processFlow2);
                }
                //请假单已提交消息
                String noticeContent = String.format("您的请假申请[%s-%s]已提交, 请等待上级审批"
                , sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));
                //通知部门经理审批消息
                noticeContent = String.format("[%s-%s]提起请假申请[%s-%s], 请尽快审批",
                        employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));
                noticeDao.insert(new Notice(dmanager.getEmployeeId(), noticeContent));
            } else if (employee.getLevel() == 7) {
                // 3.2 7级员工, 生成总经理审批任务
                Employee manager = employeeDao.selectLeader(employee);
                ProcessFlow processFlow = new ProcessFlow();
                processFlow.setFormId(leaveForm.getFormId());
                processFlow.setOperatorId(manager.getEmployeeId());
                processFlow.setAction("audit");
                processFlow.setCreateTime(new Date());
                processFlow.setOrderNo(2);
                processFlow.setState("process");
                processFlow.setIsLast(1);
                processFlowDao.insert(processFlow);
                //请假单已提交消息
                String noticeContent = String.format("您的请假申请[%s-%s]已提交, 请等待上级审批"
                        , sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));
                //通知总经理审批消息
                noticeContent = String.format("[%s-%s]提起请假申请[%s-%s], 请尽快审批",
                        employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));
                noticeDao.insert(new Notice(manager.getEmployeeId(), noticeContent));
            } else if (employee.getLevel() == 8) {
                // 3.3 8级员工, 生成总经理审批任务, 系统自动通过
                ProcessFlow processFlow = new ProcessFlow();
                processFlow.setFormId(leaveForm.getFormId());
                processFlow.setOperatorId(employee.getEmployeeId());
                processFlow.setAction("audit");
                processFlow.setResult("approved");
                processFlow.setReason("自动通过");
                processFlow.setCreateTime(new Date());
                processFlow.setAuditTime(new Date());
                processFlow.setOrderNo(2);
                processFlow.setState("complete");
                processFlow.setIsLast(1);
                processFlowDao.insert(processFlow);
                //请假单已提交消息
                String noticeContent = String.format("您的请假申请[%s-%s]系统已经自动通过"
                        , sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));
            }
            return leaveForm;
        });
    }// end of createLeaveForm

    /**
     * 获取指定任务状态及指定经办人对应的请假单列表
     * @param pfState ProcessFlow任务状态
     * @param operatorId 经办人编号
     * @return 请假单及相关数据列表
     */
    public List<Map> getLeaveFormList(String pfState, Long operatorId) {
        return (List<Map>) MyBatisUtils.executeQuery(sqlSession ->
            (List<Map>) sqlSession.getMapper(LeaveFormDao.class)
                    .selectByParams(pfState, operatorId)
        );
    }

    public void audit(Long formId, Long operatorId, String result, String reason) {
        MyBatisUtils.executeUpdate(sqlSession -> {
            //1 无论同意/驳回, 当前任务状态变更为complete
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            List<ProcessFlow> flowList = processFlowDao.selectByFormId(formId);
            if (flowList.size() == 0) {
                throw new BusinessException("PF001", "无效的审批流程");
            }
            //获取当前任务ProcessFlow对象
            List<ProcessFlow> processFlowList = flowList.stream().filter(p -> p.getOperatorId() == operatorId && p.getState().equals("process")).collect(Collectors.toList());
            ProcessFlow processFlow = null;
            if (processFlowList.size() == 0) {
                throw new BusinessException("PF002", "未找到待处理任务");
            }else{
                processFlow = processFlowList.get(0);
                processFlow.setState("complete");
                processFlow.setResult(result);
                processFlow.setReason(reason);
                processFlow.setAuditTime(new Date());
                processFlowDao.update(processFlow);
            }

            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            LeaveForm leaveForm = leaveFormDao.selectById(formId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            Employee employee = employeeDao.selectById(leaveForm.getEmployeeId());//表单提交人消息
            Employee operator = employeeDao.selectById(operatorId);//经办人消息
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            //2 如果当前任务是最后一个节点, 更新请假单状态为对应的approved/refused
            if (processFlow.getIsLast() == 1) {
                leaveForm.setState(result);
                leaveFormDao.update(leaveForm);

                String strResult = null;
                if (result.equals("approved")) {
                    strResult = "批准";
                } else if (result.equals("refused")) {
                    strResult = "驳回";
                }
                String noticeContent = String.format("您的请假申请[%s-%s]%s%s已%s, 审批意见:%s, 审批流程结束",
                        sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()),
                        operator.getTitle(), operator.getName(),
                        strResult, reason);//发给表单提交人的信息
                noticeDao.insert(new Notice(leaveForm.getEmployeeId(), noticeContent));

                noticeContent = String.format("%s-%s提起请假申请[%s-%s], 您已%s, 审批意见:%s, 审批流程结束",
                        employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()),
                        strResult, reason);//发给审批人的信息
                noticeDao.insert(new Notice(operator.getEmployeeId(), noticeContent));
            } else{
                //readyList包含所有的后续任务节点
                List<ProcessFlow> readyList = flowList.stream().filter(p -> p.getState().equals("ready")).collect(Collectors.toList());
                //3 如果当前任务不是最后一个节点且审批通过, 下一个节点的状态从ready变为process
                if (result.equals("approved")) {
                    ProcessFlow readyProcess = readyList.get(0);
                    readyProcess.setState("process");
                    processFlowDao.update(readyProcess);
                    //消息1 通知表单提交人, 部门经理意见审批通过, 交由上级继续审批
                    String noticeContent1 = String.format("您的请假申请[%s-%s]%s%s已批准, 审批意见:%s, 交由上级继续审批",
                            sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()),
                            operator.getTitle(), operator.getName(), reason);//发给表单提交人的信息
                    noticeDao.insert(new Notice(leaveForm.getEmployeeId(), noticeContent1));
                    //消息2 通知总经理有新的审批任务
                    String noticeContent2 = String.format("%s-%s提起请假申请[%s-%s], 请尽快审批",
                            employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));//发给审批人的信息
                    noticeDao.insert(new Notice(readyProcess.getOperatorId(), noticeContent2));
                    //消息3 通知部门经理(当前经办人, 员工的申请单你已批准, 交由上级继续审批
                    String noticeContent3 = String.format("%s-%s提起请假申请[%s-%s], 您已批准, 交由上级继续审批",
                            employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));//发给审批人的信息
                    noticeDao.insert(new Notice(operator.getEmployeeId(), noticeContent3));
                } else if (result.equals("refused")) {
                //4 如果当前任务不是最后一个节点且审批驳回, 后序任务状态都变为cancel, 请假单状态变为refused
                    for (ProcessFlow p : readyList) {
                        p.setState("cancel");
                        processFlowDao.update(p);
                    }
                    leaveForm.setState("refused");
                    leaveFormDao.update(leaveForm);
                    //消息1 通知表单提交人, 申请已被驳回
                    String noticeContent1 = String.format("您的请假申请[%s-%s]已被%s%s驳回, 审批意见:%s, 交由上级继续审批",
                            sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()),
                            operator.getTitle(), operator.getName(), reason);//发给表单提交人的信息
                    noticeDao.insert(new Notice(leaveForm.getEmployeeId(), noticeContent1));
                    //消息2 通知经办人, "您已驳回表单"
                    String noticeContent2 = String.format("%s-%s提起请假申请[%s-%s], 您已驳回",
                            employee.getTitle(), employee.getName(), sdf.format(leaveForm.getStartTime()), sdf.format(leaveForm.getEndTime()));//发给审批人的信息
                    noticeDao.insert(new Notice(operator.getEmployeeId(), noticeContent2));
                }
            }
            return null;
        });
    }
}
