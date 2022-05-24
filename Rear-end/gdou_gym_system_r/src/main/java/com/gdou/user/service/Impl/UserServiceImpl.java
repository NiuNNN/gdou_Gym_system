package com.gdou.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.user.dao.UserMapper;
import com.gdou.user.domain.User;
import com.gdou.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public boolean addVatar(String avatar, User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("code",user.getCode())
                .set("avatar",avatar);
        return userMapper.update(null, updateWrapper)>0;
    }

    @Override
    public String getAvatar(String avatar) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",avatar);
        User user = userMapper.selectOne(queryWrapper);
        return user.getAvatar();
    }
}
