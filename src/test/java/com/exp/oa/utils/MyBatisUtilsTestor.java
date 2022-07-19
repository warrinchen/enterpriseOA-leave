package com.exp.oa.utils;

import org.junit.Test;

public class MyBatisUtilsTestor {
    @Test
    public void testcase1(){
        String result = (String) MyBatisUtils.executeQuery(sqlSession -> sqlSession.<String>selectOne("test.sample"));
        System.out.println(result);
    }
}
