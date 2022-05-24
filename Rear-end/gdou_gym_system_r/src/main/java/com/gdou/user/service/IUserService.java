package com.gdou.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.user.domain.User;

public interface IUserService extends IService<User> {

    boolean addVatar(String avatar, User user);

    String getAvatar(String avatar);
}
