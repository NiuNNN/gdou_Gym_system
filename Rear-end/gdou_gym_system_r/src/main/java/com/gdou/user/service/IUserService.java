package com.gdou.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.user.domain.User;

public interface IUserService extends IService<User> {

    boolean addVatar(String avatar, User user);

    boolean updateUserPwd(User user);

    boolean updateUser(User user);

    //检查手机否存在账号、是否存在手机号、是否存在邮箱
    String checkUser(User user);

    boolean checkPhone(String phone);

    boolean checkEmail(String email);

    String getUserName(String usercode);

    IPage<User> getPage(int currentPage, int pageSize, User user);

    Boolean deleteUser(String usercode);
}
