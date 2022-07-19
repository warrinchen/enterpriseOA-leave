package com.exp.oa.service;

import com.exp.oa.entity.Notice;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class NoticeServiceTest {

    @Test
    public void testGetNoticeList() {
        NoticeService noticeService = new NoticeService();
        List<Notice> noticeList = noticeService.getNoticeList(1L);
//        noticeList.forEach(notice -> System.out.println(notice.getContent()));
    }
}