package com.gdou.tools.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gdou.api.CommonResult;
import com.gdou.tools.domain.Tools;
import com.gdou.tools.service.IToolsService;
import com.gdou.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tools",produces="application/json")
public class ToolsController {
    @Autowired
    private IToolsService iToolsService;

    /*@GetMapping
    public CommonResult getAll(){
        return CommonResult.success(iToolsService.list());
    }*/

    /**
     * 获取全部器材
     * @param currentPage 当前页数
     * @param pageSize 页面大小
     * @param tools 器材
     * @return page 返回器材数据
     */
    @GetMapping("getAll/{token}/{currentPage}/{pageSize}")
    public CommonResult getTools(@PathVariable String token,@PathVariable int currentPage, @PathVariable int pageSize, Tools tools){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else{
            IPage<Tools> page = iToolsService.getPage(currentPage,pageSize,tools);
            //如果当前页码大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
            if(currentPage>page.getPages()){
                page = iToolsService.getPage((int)page.getPages(),pageSize,tools);
            }
            return CommonResult.success(page);
        }

    }

    /**
     *
     * @param token
     * @param currentPage
     * @param pageSize
     * @param tools
     * @return
     */
    @GetMapping("getExistAll/{token}/{currentPage}/{pageSize}")
    public CommonResult getExistAll(@PathVariable String token,@PathVariable int currentPage, @PathVariable int pageSize, Tools tools){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else{
            IPage<Tools> page = iToolsService.getExistPage(currentPage,pageSize,tools);
            //如果当前页码大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
            if(currentPage>page.getPages()){
                page = iToolsService.getExistPage((int)page.getPages(),pageSize,tools);
            }
            return CommonResult.success(page);
        }
    }

    /**
     * 添加新器材
     * @param tools 器材
     * @return CommonResult
     */
    @PostMapping()
    public  CommonResult save(@RequestBody Tools tools) {
        System.out.println(tools);
            boolean flag = iToolsService.save(tools);
            if (flag) return CommonResult.success();
            else {
                return CommonResult.failed();
            }

    }

    /**
     * 用户根据器材种类 获取对应价格
     * @param kind 器材种类
     * @return price
     */
    @GetMapping("/getprice")
    public CommonResult getPrice(String kind){
//        System.out.println("controller---"+kind);
        List<Tools> price = iToolsService.getPrice(kind);
        if(price.size()!=0) return CommonResult.success(price);
        return CommonResult.failed();
    }

    /**
     * 通过时间 器材种类 时间段 进行筛选 查询器材有无剩余
     * @param token 验证码
     * @param date 时间
     * @param time 时间段
     * @param tools 器材种类
     * @return 返回符合条件器材数量
     */
    @GetMapping("search/{token}/{date}/{time}")
    public CommonResult getCount(@PathVariable String token,@PathVariable String date,@PathVariable int time,Tools tools){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else{
            List<Integer> toolsCode = iToolsService.getTools(tools, date, time);
            return CommonResult.success(toolsCode.size());
        }
    }


    /**
     * 对器材进行维修
     * @param id 器材id
     * @return
     */
    @GetMapping("fix/{id}")
    public CommonResult fixTools(@PathVariable Integer id){
        boolean flag = iToolsService.fixTools(id);
        if(flag) return CommonResult.success();
        return CommonResult.failed();
    }

    /**
     * 对器材进行上架
     * @param id 器材id
     * @return
     */
    @GetMapping("up/{id}")
    public CommonResult upTools(@PathVariable Integer id){
        boolean flag = iToolsService.upTools(id);
        if(flag) return CommonResult.success();
        return CommonResult.failed();
    }

    /**
     * 删除器材
     * @param id 器材id
     * @return
     */
    @DeleteMapping("{id}")
    public CommonResult deleteTools(@PathVariable Integer id){
        boolean flag = iToolsService.deleteTools(id);
        if(flag) return CommonResult.success();
        return CommonResult.failed();
    }
}
