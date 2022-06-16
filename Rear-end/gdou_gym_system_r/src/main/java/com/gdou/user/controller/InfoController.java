package com.gdou.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gdou.api.CommonResult;
import com.gdou.user.domain.User;
import com.gdou.user.service.IUserService;
import com.gdou.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping(value = "/users",produces="application/json")
public class InfoController {
    @Autowired
    private IUserService userService;


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

        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\avatar\\"+user.getCode()+"."+pType;

//        System.out.println(path);

        try{
            userService.addVatar("http://localhost:80/avatar/"+user.getCode()+"."+pType,user);
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

    /**
     * 修改密码
     * @param code
     * @param password
     * @param pass
     * @param token
     * @return
     */
    @PutMapping("/toUploadPwd/{token}/{code}/{password}/{pass}")
    public CommonResult updatePassword(@PathVariable String code, @PathVariable String password,@PathVariable String pass,@PathVariable String token){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else{
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("code",code);
            User user = userService.getOne(queryWrapper);
            if(!user.getPassword().equals(password)) return CommonResult.validateFailed();
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("code",code)
                    .set("password",pass);
            boolean update = userService.update(updateWrapper);
            if(update) return CommonResult.success();
            else return CommonResult.failed();
        }
    }

    /**
     * 管理员修改用户密码，用户账号是否存在
     * @param
     * @return
     */
    @PostMapping("/getuserid")
    public CommonResult getUserId(@RequestBody User user){
        System.out.println(user);
        String code = userService.checkUser(user);
        if(code.equals("01")) return CommonResult.success();
        else{
            return CommonResult.failed("该账号不存在","code");
        }
    }

    /**
     * 获取全部用户信息
     * @param currentPage 当前页码
     * @param pageSize 页面大小
     * @param user
     * @return
     */
    @GetMapping("getAll/{currentPage}/{pageSize}")
    public CommonResult getAll(@PathVariable int currentPage, @PathVariable int pageSize,User user){
        IPage<User> page = userService.getPage(currentPage,pageSize,user);
        //如果当前页码大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if(currentPage>page.getPages()){
            page = userService.getPage((int)page.getPages(),pageSize,user);
        }
        return CommonResult.success(page);
    }

    /**
     * 判断用户是否有预约 未付款的记录 没有进行删除
     * @param usercode
     * @return
     */
    @DeleteMapping("{usercode}")
    public CommonResult deleteuser(@PathVariable String usercode){
        Boolean flag = userService.deleteUser(usercode);
        return flag ? CommonResult.success() :CommonResult.failed();
    }
}
