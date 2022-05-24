package com.gdou.user.controller;

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
    public CommonResult updatAvatar(User user, MultipartFile file){
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
}
