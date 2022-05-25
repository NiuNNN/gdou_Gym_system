package com.gdou.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdou.api.CommonResult;
import com.gdou.user.domain.SuperUser;
import com.gdou.user.domain.User;
import com.gdou.user.service.ISuperUserService;
import com.gdou.user.service.IUserService;
import com.gdou.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/logins",produces="application/json")
public class LoginController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ISuperUserService superUserService;

    /**
     * 用户登录
     * @param user
     * @return JSONObject
     */
    @PostMapping("/user")
    public JSONObject userLogin(@RequestBody User user){
        JSONObject result = new JSONObject();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("code",user.getCode())
                .eq("password",user.getPassword());
        User admin = userService.getOne(queryWrapper);
        if(admin == null){
            result.put("state",400);
        }
        else {
            String token = TokenUtil.sign(admin.getName());
            result.put("userid",admin.getCode());
            result.put("name",admin.getName());
            result.put("token",token);
            result.put("state",200);
            result.put("userclass","user");
            System.out.println(token);
        }
        return result;
    }

    /**
     * 管理人员登录
     * @param superUser
     * @return JSONObject
     */
    @PostMapping("/superuser")
    public JSONObject superuserLogin(@RequestBody SuperUser superUser){
        JSONObject result = new JSONObject();
        QueryWrapper<SuperUser> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("code",superUser.getCode())
                .eq("password",superUser.getPassword());
        SuperUser admin = superUserService.getOne(queryWrapper);
        if(admin == null){
            result.put("state",400);
        }
        else {
            String token = TokenUtil.sign(admin.getName());
            result.put("userid",admin.getCode());
            result.put("name",admin.getName());
            result.put("token",token);
            result.put("state",200);
            result.put("userclass","superuser");
            System.out.println(token);
        }
        return result;
    }

    /**
     *修改用户密码
     * @param user
     * @return CommonResult
     */
    @PutMapping("/user")
    public CommonResult updateUserPwd(@RequestBody User user){
//        System.out.println(user);
        boolean flag = userService.updateUserPwd(user);
//        System.out.println(flag);
        if(flag) {
            return CommonResult.success();
        }
        else {
            return CommonResult.validateFailed("输入有误，请重新填写");
        }
    }

    /**
     *修改管理员密码
     * @param superUser
     * @return CommonResult
     */
    @PutMapping("/superuser")
    public CommonResult updateSuperUser(@RequestBody SuperUser superUser){
        UpdateWrapper<SuperUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("name",superUser.getName())
                .eq("phone",superUser.getPhone())
                .eq("code",superUser.getCode())
                .set("password",superUser.getPassword());
        boolean flag = superUserService.update(null,updateWrapper);
        if(flag) {
            return CommonResult.success();
        }
        else {
            return CommonResult.validateFailed("输入有误，请重新填写");
        }
    }

    /**
     * 新建用户
     * @param user
     * @return CommonResult
     */
    @PostMapping("/save")
    public CommonResult saveUser(@RequestBody User user){
        String code = userService.checkUser(user);
        if(code.equals("01")) return CommonResult.failed("存在该账号","code");
        else if(code.equals("02")) return CommonResult.failed("存在该手机号","phone");
        else if(code.equals("03")) return CommonResult.failed("存在该邮箱","email");
        else{
            userService.save(user);
            return CommonResult.success();
        }
    }
}
