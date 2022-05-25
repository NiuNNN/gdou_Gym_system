package com.gdou.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.user.dao.SuperUserMapper;
import com.gdou.user.domain.SuperUser;
import com.gdou.user.service.ISuperUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuperUserServiceImpl extends ServiceImpl<SuperUserMapper, SuperUser> implements ISuperUserService {
    @Autowired
    SuperUserMapper superUserMapper;

    @Override
    public boolean updateSuperPwd(SuperUser superUser) {
        UpdateWrapper<SuperUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("name",superUser.getName())
                .eq("phone",superUser.getPhone())
                .eq("email",superUser.getEmail())
                .eq("code",superUser.getCode())
                .set("password",superUser.getPassword());
        int flag = superUserMapper.update(null, updateWrapper);
        return flag>0;
    }
}
