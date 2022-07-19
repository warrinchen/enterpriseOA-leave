package com.exp.oa.service;

import com.exp.oa.entity.Node;
import com.exp.oa.entity.User;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

public class UserServiceTest extends TestCase {
    private UserService userService = new UserService();
    @Test
    public void testCheckLogin1() {
        userService.checkLogin("uu", "1234");
    }
    @Test
    public void testCheckLogin2() {
        userService.checkLogin("m8", "1234");
    }
    @Test
    public void testCheckLogin3() {
        User user = userService.checkLogin("m8", "test");
        System.out.println(user.getUserId());
    }

    @Test
    public void testSelectNode() {
        List<Node> list = userService.selectNodeByUserId(2L);
        System.out.println(list.size());
    }
}