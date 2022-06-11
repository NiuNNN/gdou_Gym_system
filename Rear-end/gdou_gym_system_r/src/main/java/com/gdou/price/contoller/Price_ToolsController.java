package com.gdou.price.contoller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gdou.api.CommonResult;
import com.gdou.price.domain.Price_Tools;
import com.gdou.price.service.IPrice_ToolsService;
import com.gdou.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/price",produces="application/json")
public class Price_ToolsController {
    @Autowired
    private IPrice_ToolsService iPrice_toolsService;

    /**
     * 对用户的租借信息进行查询
     * @param token 验证码
     * @param currentPage 当前页数
     * @param pageSize 页面大小
     * @param priceTools
     * @return page
     */
    @GetMapping("{token}/{currentPage}/{pageSize}")
    public CommonResult getOrder(@PathVariable String token, @PathVariable int currentPage, @PathVariable int pageSize,Price_Tools priceTools){
//        System.out.println(priceTools);
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else {
            IPage<Price_Tools> page = iPrice_toolsService.getPage(currentPage, pageSize, priceTools);
            if(currentPage>page.getPages()){
                page = iPrice_toolsService.getPage((int)page.getPages(), pageSize, priceTools);
            }
            return CommonResult.success(page);
        }
    }


    /**
     * 领取器材操作 判断是否在预约时间 然后再判断器材是否在仓库中
     * @param token 验证码
     * @param id 订单号
     * @return
     */
    @GetMapping("{token}/{id}")
    public CommonResult handleReceive(@PathVariable String token,@PathVariable Integer id){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else {
            //首先判断是否超时领取
            String code = iPrice_toolsService.overTime(id);
            if(code.equals("00")){
                List<Integer> list = iPrice_toolsService.handleReceive(id);
                if(list.size() > 0) return CommonResult.failed("03",list);
                else return CommonResult.success();
            }else if(code.equals("01")){
                return CommonResult.failed("01");
            }
            else{
                return CommonResult.failed("02");
            }
        }
    }
}
