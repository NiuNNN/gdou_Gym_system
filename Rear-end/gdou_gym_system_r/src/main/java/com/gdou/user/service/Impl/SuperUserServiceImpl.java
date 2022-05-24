package com.gdou.user.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.user.dao.SuperUserMapper;
import com.gdou.user.domain.SuperUser;
import com.gdou.user.service.ISuperUserService;
import org.springframework.stereotype.Service;

@Service
public class SuperUserServiceImpl extends ServiceImpl<SuperUserMapper, SuperUser> implements ISuperUserService {
}
