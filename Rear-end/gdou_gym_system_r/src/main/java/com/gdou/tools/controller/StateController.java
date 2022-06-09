package com.gdou.tools.controller;

import com.gdou.api.CommonResult;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.Tools;
import com.gdou.tools.service.ITStateService;
import com.gdou.tools.service.IToolsService;
import com.gdou.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/borrows",produces="application/json")
public class StateController {
    @Autowired
    private ITStateService itStateService;

    @Autowired
    private IToolsService iToolsService;

    @GetMapping("/rent/{token}/{usercode}/{num}/{date}/{time}")
    public CommonResult save( @PathVariable String token,@PathVariable String usercode,@PathVariable int num,@PathVariable String date,@PathVariable int time, Tools tools){
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
}
