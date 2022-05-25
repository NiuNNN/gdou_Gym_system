package com.gdou.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdou.api.CommonResult;
import com.gdou.user.domain.User;
import com.gdou.user.service.ISuperUserService;
import com.gdou.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping(value = "/users",produces="application/json")
public class InfoController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ISuperUserService superUserService;

    /**
     * 返回用户信息
     * @param code
     * @return
     */
    @GetMapping("/toGetUserInfo/{code}")
    public CommonResult getUserInfo(@PathVariable String code){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code",code);
        User user = userService.getOne(queryWrapper);
        if(user.equals("")) return CommonResult.failed();
        return CommonResult.success(user);
    }


    /**
     * 获得照片
     * @param code
     * @return
     */
    @GetMapping ("/toGetUserAvatar/{code}")
    public CommonResult getAvatar(@PathVariable String code){
        String avatar = userService.getAvatar(code);
        if(!avatar.equals("")){
            return CommonResult.success(avatar);
        }
        else return CommonResult.validateFailed();
    }


    /**
     * 上传照片
     * @param user
     * @param file
     * @return
     */
    @PostMapping("/toUploadUserAvatar")
    public CommonResult updateAvatar(User user, MultipartFile file){
        //判断文件类型
        String pType = file.getContentType();
        pType = pType.substring(pType.indexOf("/")+1);

        if("jpeg".equals(pType)){
            pType="jpg";
        }
        long time=System.currentTimeMillis();

        String path="C:/Users/牛/Desktop/gdou_Gym_System/Rear-end/gdou_gym_system_r/src/main/resources/static/images/avatar/"+user.getCode()+"."+pType;

        try{
            userService.addVatar("http://localhost:80/"+path.substring(path.indexOf("images/")),user);
            file.transferTo(new File(path));
            //文件路径保存到数据库中从而读取
            System.out.println(path);
            return CommonResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed();
        }
    }

    /**
     * 修改资料
     * @param user
     * @return
     */
    @PutMapping("/toUploadUser")
    public CommonResult updateUser(@RequestBody User user){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code",user.getCode());
        User user1 = userService.getOne(queryWrapper);
        //1.判断是否修改了邮箱和手机号
        //2.判断是否存在相同的邮箱和手机号
        if(!user1.getPhone().equals(user.getPhone())) {
            if (userService.checkPhone(user.getPhone())) {
                return CommonResult.failed("存在该手机号", "phone");
            }
        }
        if(!user1.getEmail().equals(user.getEmail())) {
            if (userService.checkEmail(user.getEmail())) {
                return CommonResult.failed("存在该邮箱", "email");
            }
        }
        boolean flag = userService.updateUser(user);
        if(flag) return CommonResult.success();
        return CommonResult.failed();
    }
}
