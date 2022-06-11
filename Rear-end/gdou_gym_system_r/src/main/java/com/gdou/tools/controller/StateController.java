package com.gdou.tools.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdou.api.CommonResult;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.Tools;
import com.gdou.tools.domain.UserToolsVO;
import com.gdou.tools.service.ITStateService;
import com.gdou.tools.service.IToolsService;
import com.gdou.user.service.IUserService;
import com.gdou.utils.TimeUtil;
import com.gdou.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/borrows",produces="application/json")
public class StateController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ITStateService itStateService;

    @Autowired
    private IToolsService iToolsService;

    /**
     * 用户预约器材
     * @param token 验证码
     * @param usercode 用户号
     * @param num 预约器材数量
     * @param date 预约日期
     * @param time 预约时间段
     * @param tools 预约器材
     * @return Array[器材号,...,租借单号]
     */
    @GetMapping("rent/{token}/{usercode}/{num}/{date}/{time}")
    public CommonResult rent( @PathVariable String token,@PathVariable String usercode,@PathVariable int num,@PathVariable String date,@PathVariable int time, Tools tools){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else {
            List<Integer> toolsList = iToolsService.getTools(tools, date, time);
            TState tState = new TState();
            tState.setUsercode(usercode);
            tState.setDate(date);
            tState.setTime(time);
            List<Integer> msg = itStateService.insert(tState, num, toolsList);
            return CommonResult.success(msg);//数组的最后一个数据为用户的租借订单
        }
    }

    @GetMapping("time/{date}")
    public CommonResult getTime(@PathVariable String date){
        List<String> list = TimeUtil.chooseTime(date);
        return CommonResult.success(list);
    }

    /**
     * 用户获取全部租借用具的信息
     * @param token 验证码
     * @param currentPage 当前页数
     * @param pageSize 页面大小
     * @param usercode 用户号
     * @return page 返回器材数据
     */
    @GetMapping("/getAll/{token}/{currentPage}/{pageSize}/{usercode}")
    public CommonResult getAll(@PathVariable String token,@PathVariable int currentPage, @PathVariable int pageSize,@PathVariable String usercode){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else{
            Page<UserToolsVO> page = itStateService.getPage(currentPage, pageSize, usercode);
            //如果当前页码大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
            if(currentPage>page.getPages()){
                page = itStateService.getPage((int)page.getPages(),pageSize,usercode);
            }
            return CommonResult.success(page);
        }
    }

    @GetMapping("detail/{token}/{id}/{usercode}")
    public JSONObject getDetail(@PathVariable String token,@PathVariable Integer id,@PathVariable String usercode){
        JSONObject result = new JSONObject();
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) result.put("code",401);
        else{
            //获取用户名称
            String userName = iUserService.getUserName(usercode);
            result.put("username",userName);
            //获取器材编号
            String toolscode = itStateService.getToolscode(id);
            result.put("toolscode",toolscode);
            if(!userName.equals("") && !toolscode.equals("")) result.put("code",200);
            else result.put("code",400);
        }
        return result;
    }

    /**
     * 取消预约
     * @param token 验证码
     * @param id 器材订单对应编号
     * @return CommonResult
     */
    @DeleteMapping("{token}/{id}")
    public CommonResult delete(@PathVariable String token,@PathVariable Integer id){
        boolean verify = TokenUtil.verify(token);//token是否超时如果超时前端就强制退出用户
        if(!verify) return CommonResult.unauthorized(null);
        else {
            if(itStateService.updateState(id)) return CommonResult.success();
            return CommonResult.failed();
        }
    }

}
