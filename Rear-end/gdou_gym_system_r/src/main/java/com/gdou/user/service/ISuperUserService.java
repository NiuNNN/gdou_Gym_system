package com.gdou.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.user.domain.SuperUser;

public interface ISuperUserService extends IService<SuperUser> {
    boolean addVatar(String avatar, SuperUser superUser);

    boolean updateSuperPwd(SuperUser superUser);

    boolean updateSuperUser(SuperUser superUser);

    //检查手机否存在账号、是否存在手机号、是否存在邮箱
    String checkUser(SuperUser superUser);

    boolean checkPhone(String phone);

    boolean checkEmail(String email);
}
