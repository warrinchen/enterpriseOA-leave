package com.exp.oa.dao;

import com.exp.oa.entity.Notice;

import java.util.List;
import java.util.Map;

public interface NoticeDao {
    void insert(Notice notice);

    List<Notice> selectByRecevierId(Long receiverId);
}
