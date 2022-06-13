package com.gdou.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdou.user.dao.UserMapper;
import com.gdou.user.domain.User;
import com.gdou.user.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class controllerTestCase {
    @Autowired
    private IUserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    void testLogin(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("username","大番薯")
                .eq("password","123");
        User one = userService.getOne(queryWrapper);
        System.out.println(one);
    }

    @Test
    void testGetUserClass(){
        String test = "201911701313";
        QueryWrapper<User> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq("code",test);
        User user = userMapper.selectOne(queryWrapper);
        if( user == null) System.out.println("123");
        System.out.println(user);

    }

    @Test
    void selectById(){
        User user = userMapper.selectById("201911701310");
        System.out.println(user);
    }
}
