package com.gdou.price.contoller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gdou.api.CommonResult;
import com.gdou.price.domain.Price_Tools;
import com.gdou.price.service.IBreak_ContractService;
import com.gdou.price.service.IPrice_ToolsService;
import com.gdou.price.service.IUnreceive_Tools;
import com.gdou.tools.service.ITStateService;
import com.gdou.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/price",produces="application/json")
public class Price_ToolsController {
    @Autowired
    private IPrice_ToolsService iPrice_toolsService;

    @Autowired
    private ITStateService itStateService;

    @Autowired
    private IBreak_ContractService iBreak_contractService;

    @Autowired
    private IUnreceive_Tools iUnreceive_tools;

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
     * 领取器材操作 判断是否在预约时间 然后再判断器材是否在仓库中 条件都满足给予租借 不满足返回信息
     * @param token 验证码
     * @param id 订单号
     * @return
     */
    @GetMapping("{token}/{id}")
    public CommonResult handleRent(@PathVariable String token,@PathVariable Integer id){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else {
            //首先判断是否超时领取
            String code = iPrice_toolsService.overTime(id);
            if(code.equals("00")){
                List<Integer> list = iPrice_toolsService.handleRent(id);
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

    /**
     * 取消预约
     * @param token 验证码
     * @param id 订单号
     * @return
     */
    @DeleteMapping("cancel/{token}/{id}")
    public CommonResult cancelRent(@PathVariable String token,@PathVariable Integer id){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else{
            String code = iPrice_toolsService.overTime(id);
            if (code.equals("02")){
                return CommonResult.failed("02");//超过预定时间不给予取消
            }
            else{
                boolean flag = iPrice_toolsService.cancelRent(id);
                if(flag) return CommonResult.success();
                return CommonResult.failed();
            }
        }
    }

    /**
     * 查询有无相同条件器材进行替换 有就直接替换然后进行租借 返回成功租借信息 以及更换后器材的信息 然后没有则返回没有的信息
     * @param token
     * @param id
     * @return
     */
    @GetMapping("change/{token}/{id}")
    public JSONObject changeRent(@PathVariable String token, @PathVariable Integer id){
        JSONObject result = new JSONObject();
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) {
            result.put("code",401);
        }
        else{
            String s = iPrice_toolsService.changeRent(id);
//            System.out.println(s);
            if(s.equals("01")){//没有多余的器材给予更换
                result.put("code",500);
            }
            else{//有多余的器材给予更换 然后直接进行器材的领取操作 返回给前端替换后的器材编号 以及成功领取的状态码
                List<Integer> list = iPrice_toolsService.handleRent(id);
//                System.out.println(list);
                result.put("toolslist",s);
                result.put("code",200);
            }
        }
        return result;
    }

    /**
     * 对不存在的器材去除 然后进行租借 返回消息
     * @param token 验证码
     * @param id 订单号
     * @return
     */
    @GetMapping("continue/{token}/{id}")
    public CommonResult continueRent(@PathVariable String token,@PathVariable Integer id){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else{
            String s = iPrice_toolsService.continueRent(id);
            if(!s.equals("")){
                iPrice_toolsService.handleRent(id);
                return CommonResult.success(s);
            }
            else{
                return CommonResult.failed();
            }
        }
    }

    /**
     * 返回用户租借器材的出库信息
     * @param token
     * @param id
     * @return
     */
    @GetMapping("receive/{token}/{id}")
    public JSONObject receive(@PathVariable String token,@PathVariable Integer id){
        JSONObject result = new JSONObject();
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) result.put("code",401);
        else{
            result.put("code",200);
            result.put("toolscode",itStateService.getToolscode(id));
            result.put("actually",itStateService.getDateTime(id).get(1));
        }
        return result;
    }

    /**
     * 计算收账 如果用户超过6小时返还器材 进行违约处理
     * @param token 验证码
     * @param id
     * @return
     */
    @GetMapping("getPrice/{token}/{id}")
    public JSONObject getPrice(@PathVariable String token,@PathVariable Integer id){
        JSONObject result = new JSONObject();
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) result.put("code",401);
        else{
            List<String> list = iPrice_toolsService.getPrice(id);
            if(list.get(3).equals("01")){
                //超时 对用户的违约行为记录 此时是超时归还
                iBreak_contractService.takeNotes(id);
            }
            result.put("overtime",list.get(0));
            result.put("price",list.get(1));
            result.put("ractually",list.get(2));
            result.put("code",200);
        }
        return result;
    }

    /**
     * 确认收款把收款信息写入订单
     * @param token
     * @param id
     * @param price
     * @return
     */
    @GetMapping("handlePay/{token}/{id}/{price}")
    public CommonResult handlePay(@PathVariable String token,@PathVariable Integer id,@PathVariable String price){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else{
            if (iPrice_toolsService.handlePay(id,price)) return CommonResult.success();
            return CommonResult.failed();
        }
    }

    /**
     * 对用户超时未领取器材进行登记
     * @param id
     * @return
     */
    @GetMapping("register/{id}")
    public CommonResult register(@PathVariable Integer id){
        iUnreceive_tools.register(id);
        return CommonResult.success();
    }

}
