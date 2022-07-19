package com.exp.oa.service;

import com.exp.oa.dao.NoticeDao;
import com.exp.oa.entity.Notice;
import com.exp.oa.utils.MyBatisUtils;

import java.util.List;
import java.util.Map;

public class NoticeService {
    public List<Notice> getNoticeList(Long receiverId) {
        return (List<Notice>) MyBatisUtils.executeQuery(sqlSession ->
                sqlSession.getMapper(NoticeDao.class).selectByRecevierId(receiverId));
    }
}
