package com.gdou.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.gdou.api.CommonResult;
import com.gdou.game.domain.Judge;
import com.gdou.game.service.IJudgeService;
import java.util.List;

@RestController
@RequestMapping(value = "/judge", produces = "application/json")
public class JudgeInfoController {
    @Autowired
    private  IJudgeService judgeService;

    /**
     * 添加裁判
     * @param judge
     * @return
     */
    @PostMapping("/save")
    public CommonResult saveJudge(@RequestBody Judge judge) {
        judgeService.save(judge);
        return CommonResult.success();
    }

    /**
     * 获得裁判的id
     * @return
     */
    @GetMapping("/getjudgeid")
    public CommonResult getJudge(){
        List<Judge> list = judgeService.list();
        if(list.size() !=0){
            return CommonResult.success(list);
        }else{
            return  CommonResult.failed();
        }
    }

    @PostMapping("/deletejudge/{judgeid}")
    public  CommonResult deletejudge(@PathVariable int judgeid){
        QueryWrapper<Judge> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("judgeid",judgeid);
        judgeService.remove(queryWrapper);
        return CommonResult.success();
    }
}
