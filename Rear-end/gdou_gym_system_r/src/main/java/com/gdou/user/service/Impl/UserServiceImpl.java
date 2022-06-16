package com.gdou.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.price.dao.Price_ToolsMapper;
import com.gdou.price.domain.Price_Tools;
import com.gdou.state.dao.AppointmentMapper;
import com.gdou.state.domain.StateAppointment;
import com.gdou.user.dao.SuperUserMapper;
import com.gdou.user.dao.UserMapper;
import com.gdou.user.domain.SuperUser;
import com.gdou.user.domain.User;
import com.gdou.user.service.IUserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SuperUserMapper superUserMapper;

    @Autowired
    private Price_ToolsMapper price_toolsMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

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

    /**
     * 获取用户名
     * @param usercode
     * @return
     */
    @Override
    public String getUserName(String usercode) {
        QueryWrapper<User> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq("code",usercode);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            QueryWrapper<SuperUser> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("code",usercode);
            SuperUser superUser = superUserMapper.selectOne(queryWrapper1);
            return superUser.getName();
        }
        else {
            return user.getName();
        }
    }


    /**
     * 获取全部用户信息
     * @param currentPage 当前页码
     * @param pageSize 页面大小
     * @param user
     * @return
     */
    @Override
    public IPage<User> getPage(int currentPage, int pageSize, User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(user.getCode()),User::getCode,user.getCode());
        IPage page = new Page(currentPage,pageSize);
        userMapper.selectPage(page,lambdaQueryWrapper);
        return page;
    }

    /**
     * 删除用户
     * @param usercode
     * @return
     */
    @Override
    public Boolean deleteUser(String usercode) {
        //查询器材、场地的订单表有无欠费、预约
        QueryWrapper<Price_Tools> price_toolsQueryWrapper = new QueryWrapper<>();
        price_toolsQueryWrapper.eq("usercode",usercode).eq("price","");

        if(price_toolsMapper.selectCount(price_toolsQueryWrapper)>0) return false;

        QueryWrapper<StateAppointment> stateAppointmentQueryWrapper = new QueryWrapper<>();
        stateAppointmentQueryWrapper.eq("user_id",usercode).eq("usec","false");

        if(appointmentMapper.selectCount(stateAppointmentQueryWrapper)>0) return false;

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("code",usercode);
        return userMapper.delete(userQueryWrapper)>0;
    }
}
