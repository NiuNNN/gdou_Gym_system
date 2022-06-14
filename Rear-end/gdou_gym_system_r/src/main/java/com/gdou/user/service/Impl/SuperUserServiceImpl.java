package com.gdou.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    /**
     * 添加头像
     * @param avatar
     * @param superUser
     * @return
     */
    @Override
    public boolean addVatar(String avatar, SuperUser superUser) {
        UpdateWrapper<SuperUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("code", superUser.getCode())
                .set("avatar", avatar);
        return superUserMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 更新管理员个人信息
     * @param superUser
     * @return
     */

    @Override
    public boolean updateSuperUser(SuperUser superUser) {

        UpdateWrapper<SuperUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("code",superUser.getCode())
                .set("name",superUser.getName())
                .set("sex",superUser.getSex())
                .set("age",superUser.getAge())
                .set("zip",superUser.getZip())
                .set("phone",superUser.getPhone())
                .set("email",superUser.getEmail())
                .set("address",superUser.getAddress());
        int flag = superUserMapper.update(null, updateWrapper);
        return flag>0;

    }
    /**
     * 检查手机号 邮箱 是否存在
     * @param superUser
     * @return
     */
    @Override
    public String checkUser(SuperUser superUser) {

        QueryWrapper<SuperUser> Tcode = new QueryWrapper<>();
        Tcode.eq("code",superUser.getCode());//查找有无相同学生号
        long c = superUserMapper.selectCount(Tcode);
        if(c>0) return "01";
//        System.out.println(c);
        QueryWrapper<SuperUser> Tphone = new QueryWrapper<>();
        Tphone.eq("phone",superUser.getPhone());//查找有无相同的电话号
        long p = superUserMapper.selectCount(Tphone);
        if(p>0) return "02";
//        System.out.println(p);
        QueryWrapper<SuperUser> Temail = new QueryWrapper<>();
        Temail.eq("email",superUser.getEmail());//查找有无相同的邮箱
        long e = superUserMapper.selectCount(Temail);
        if(e>0) return "03";
        return "00";
    }

    /**
     * 检查是否存在手机号
     * @param phone
     * @return
     */
    @Override
    public boolean checkPhone(String phone) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone",phone);
        Long p = superUserMapper.selectCount(queryWrapper);
        return p>0;
    }

    /**
     * 检查是否存在邮箱
     * @param email
     * @return
     */
    @Override
    public boolean checkEmail(String email) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email",email);
        Long e = superUserMapper.selectCount(queryWrapper);
        return e>0;
    }
}
