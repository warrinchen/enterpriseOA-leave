package com.exp.oa.dao;

import com.exp.oa.entity.Node;
import com.exp.oa.entity.User;
import com.exp.oa.utils.MyBatisUtils;

import java.util.List;

public class RbacDao {
    public List<Node> selectNodeByUserId(Long userId){
        return (List<Node>) MyBatisUtils.executeQuery(sqlSession -> sqlSession.selectList("rbacmapper.selectNodeByUserId", userId));
    }
}
