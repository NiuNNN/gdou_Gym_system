package com.gdou.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gdou.api.CommonResult;
import com.gdou.user.domain.SuperUser;
import com.gdou.user.service.ISuperUserService;
import com.gdou.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping(value = "/superusers",produces="application/json")
public class SuperInfoController {
    @Autowired
    private ISuperUserService superUserService;

    /**
     * 返回管理员信息
     * @param code
     * @return
     */
    @GetMapping("/toGetSuperUserInfo/{code}")
    public CommonResult getUserInfo(@PathVariable String code){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code",code);
        SuperUser superUser = superUserService.getOne(queryWrapper);
        if(superUser.equals("")) return CommonResult.failed();
        return CommonResult.success(superUser);
    }
    /**
     * 上传照片
     * @param superUser
     * @param file
     * @return
     */
    @PostMapping("/toUploadUserAvatar")
    public CommonResult updateAvatar(SuperUser superUser, MultipartFile file){
        //判断文件类型
        String pType = file.getContentType();
        pType = pType.substring(pType.indexOf("/")+1);

        if("jpeg".equals(pType)){
            pType="jpg";
        }
        long time=System.currentTimeMillis();

        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\avatar\\"+superUser.getCode()+"."+pType;

//        System.out.println(path);

        try{
            superUserService.addVatar("http://localhost:80/avatar/"+superUser.getCode()+"."+pType,superUser);
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
     * @param superUser
     * @return
     */
    @PutMapping("/toUploadUser")
    public CommonResult updateUser(@RequestBody SuperUser superUser){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code",superUser.getCode());
        SuperUser superUser1 = superUserService.getOne(queryWrapper);
        //1.判断是否修改了邮箱和手机号
        //2.判断是否存在相同的邮箱和手机号
        if(!superUser1.getPhone().equals(superUser.getPhone())) {
            if (superUserService.checkPhone(superUser.getPhone())) {
                return CommonResult.failed("存在该手机号", "phone");
            }
        }
        if(!superUser1.getEmail().equals(superUser.getEmail())) {
            if (superUserService.checkEmail(superUser.getEmail())) {
                return CommonResult.failed("存在该邮箱", "email");
            }
        }
        boolean flag = superUserService.updateSuperUser(superUser);
        if(flag) return CommonResult.success();
        return CommonResult.failed();
    }

    /**
     * 修改密码
     * @param
     * @return
     */
    @PutMapping("/toUploadPwd/{token}/{code}/{password}/{pass}")
    public CommonResult updatePassword(@PathVariable String code, @PathVariable String password,@PathVariable String pass,@PathVariable String token){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else{
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("code",code);
            SuperUser superUser = superUserService.getOne(queryWrapper);
            String code1 = superUserService.checkUser(superUser);
            if(!superUser.getPassword().equals(password)) return CommonResult.validateFailed();
            UpdateWrapper<SuperUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("code",code)
                    .set("password",pass);
            boolean update = superUserService.update(updateWrapper);
            if(update) return CommonResult.success();
            else return CommonResult.failed();
        }
    }

    /**
     * 新建用户
     * @param superUser
     * @return CommonResult
     */
    @PostMapping("/save")
    public CommonResult savesuperUser(@RequestBody SuperUser superUser){
        String code = superUserService.checkUser(superUser);
        if(code.equals("01")) return CommonResult.failed("存在该账号","code");
        else if(code.equals("02")) return CommonResult.failed("存在该手机号","phone");
        else if(code.equals("03")) return CommonResult.failed("存在该邮箱","email");
        else{
            superUserService.save(superUser);
            return CommonResult.success();
        }
    }

    /**
     * 获得管理员密码来验证
     * @param usercode
     * @return
     */
    @GetMapping("/getuserpwd/{usercode}")
    public  CommonResult getUserpwd(@PathVariable String usercode) {
        QueryWrapper<SuperUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",usercode);
        SuperUser superUser = superUserService.getOne(queryWrapper);
        return CommonResult.success(superUser);
    }

    /**
     * 获取全部管理员信息
     * @param currentPage 当前页码
     * @param pageSize 页面大小
     * @param superUser
     * @return
     */
    @GetMapping("getAll/{currentPage}/{pageSize}")
    public CommonResult getAll(@PathVariable int currentPage, @PathVariable int pageSize, SuperUser superUser){
        IPage<SuperUser> page = superUserService.getPage(currentPage,pageSize,superUser);
        //如果当前页码大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if(currentPage>page.getPages()){
            page = superUserService.getPage((int)page.getPages(),pageSize,superUser);
        }
        return CommonResult.success(page);
    }

    /**
     * 删除管理员
     * @param usercode
     * @return
     */
    @DeleteMapping("{usercode}")
    public CommonResult deleteuser(@PathVariable String usercode){
        Boolean flag = superUserService.deleteUser(usercode);
        return flag ? CommonResult.success() :CommonResult.failed();
    }

}
