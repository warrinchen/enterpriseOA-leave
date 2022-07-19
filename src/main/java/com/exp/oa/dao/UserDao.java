package com.exp.oa.dao;

import com.exp.oa.entity.User;
import com.exp.oa.utils.MyBatisUtils;

/**
 * 用户表DAO
 */
public class UserDao {
    /**
     * 按用户名查询用户表
     * @param username 用户名
     * @return User对象包含对应的用户信息, null则代表对象不存在
     */
    public User selectByUsername(String username){
        return  (User) MyBatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("usermapper.selectByUsername", username));
    }
}
