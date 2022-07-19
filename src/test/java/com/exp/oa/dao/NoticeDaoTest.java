package com.exp.oa.dao;

import com.exp.oa.entity.Notice;
import com.exp.oa.utils.MyBatisUtils;
import org.junit.Test;

import java.util.Date;

public class NoticeDaoTest {

    @Test
    public void testInsert() {
        MyBatisUtils.executeUpdate(sqlSession -> {
            Notice notice = new Notice();
            notice.setReceiverId(2L);
            notice.setContent("测试消息");
            notice.setCreateTime(new Date());
            sqlSession.getMapper(NoticeDao.class).insert(notice);
            return null;
        });
    }
}