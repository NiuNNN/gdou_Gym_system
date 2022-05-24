package com.gdou.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdou.user.domain.User;
import com.gdou.user.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class controllerTestCase {
    @Autowired
    private IUserService userService;

    @Test
    void testlogin(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("username","大番薯")
                .eq("password","123");
        User one = userService.getOne(queryWrapper);
        System.out.println(one);
    }
}
