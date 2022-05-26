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

    /**
     * 添加头像
     * @param avatar
     * @param user
     * @return
     */
    @Override
    public boolean addVatar(String avatar, User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("code", user.getCode())
                .set("avatar", avatar);
        return userMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 修改用户密码
     * @param user
     * @return
     */
    @Override
    public boolean updateUserPwd(User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("name",user.getName())
                .eq("phone",user.getPhone())
                .eq("email",user.getEmail())
                .eq("code",user.getCode())
                .set("password",user.getPassword());
        int flag = userMapper.update(null, updateWrapper);
        return flag>0;
    }

    /**
     * 更新用户个人信息
     * @param user
     * @return
     */
    @Override
    public boolean updateUser(User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("code",user.getCode())
                .set("name",user.getName())
                .set("sex",user.getSex())
                .set("age",user.getAge())
                .set("zip",user.getZip())
                .set("phone",user.getPhone())
                .set("email",user.getEmail())
                .set("address",user.getAddress());
        int flag = userMapper.update(null, updateWrapper);
        return flag>0;
    }

    /**
     * 检查手机号 邮箱 是否存在
     * @param user
     * @return
     */
    @Override
    public String checkUser(User user) {
        QueryWrapper<User> Tcode = new QueryWrapper<>();
        Tcode.eq("code",user.getCode());//查找有无相同学生号
        long c = userMapper.selectCount(Tcode);
        if(c>0) return "01";
//        System.out.println(c);
        QueryWrapper<User> Tphone = new QueryWrapper<>();
        Tphone.eq("phone",user.getPhone());//查找有无相同的电话号
        long p = userMapper.selectCount(Tphone);
        if(p>0) return "02";
//        System.out.println(p);
        QueryWrapper<User> Temail = new QueryWrapper<>();
        Temail.eq("email",user.getEmail());//查找有无相同的邮箱
        long e = userMapper.selectCount(Temail);
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
        Long p = userMapper.selectCount(queryWrapper);
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
        Long e = userMapper.selectCount(queryWrapper);
        return e>0;
    }
}
