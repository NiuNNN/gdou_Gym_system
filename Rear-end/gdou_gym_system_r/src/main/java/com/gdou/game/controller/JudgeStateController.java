package com.gdou.game.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdou.game.domain.Judge;
import com.gdou.game.service.IJudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.gdou.api.CommonResult;

import java.util.ArrayList;
import java.util.List;
import com.gdou.game.domain.JudgeState;
import com.gdou.game.service.IJudgeStateService;

@RestController
@RequestMapping(value = "/judgestate", produces = "application/json")
public class JudgeStateController {
    @Autowired
    private IJudgeStateService judgeStateService;

    @Autowired
    private IJudgeService judgeService;


    @GetMapping("/saveorder/{date}/{time}")
    public  CommonResult saveorder(@PathVariable String date, @PathVariable int time){
        long id = judgeService.count();
        int randomCount =(int) (Math.random()*id);

        QueryWrapper<Judge> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("judgeid");
        wrapper.last("limit "+String.valueOf(randomCount)+", 1");
        List<Judge> msg = judgeService.list(wrapper);

        QueryWrapper<JudgeState> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("judgeid",msg.get(0).getJudgeid())
                .eq("date",date)
                .eq("time",time);
        JudgeState judgeState = judgeStateService.getOne(queryWrapper);
        JudgeState judgeState1  = new JudgeState();
        int num =0;
        for(int i=0;i<100;i++){
            if(judgeState==null){
                judgeState1.setJudgeid(msg.get(0).getJudgeid());
                judgeState1.setDate(date);
                judgeState1.setTime(time);
                judgeStateService.save(judgeState1);

                num=1;
                break;
            }else{
                continue;
            }
        }
        Integer appointid = judgeState1.getId();
        String message = Integer.toString(appointid);
        if(num==0){
            return CommonResult.failed();
    }else{
        return CommonResult.success(message);
        }
    }

    @GetMapping("/saveorder1/{date}/{time}")
    public  CommonResult saveorder1(@PathVariable String date, @PathVariable int time){

        List<Judge> judgeid = judgeStateService.getJudgeId();

        QueryWrapper<JudgeState> queryWrapper = new QueryWrapper<>();
        JudgeState judgeState1  = new JudgeState();
        List<Integer> check = new ArrayList<>();
        int num =0;
        for(int i=0;i<judgeid.size();i++){
            queryWrapper.eq("judgeid",judgeid.get(i).getJudgeid())
                    .eq("date",date)
                    .eq("time",time);
            System.out.println(judgeid.get(i).getJudgeid());
             check = judgeStateService.integerList(queryWrapper);
            System.out.println(check);
        }


        if(num==1){
            return CommonResult.failed("在该时间段，没有空闲裁判");
        }else{
            return CommonResult.success(check);
        }
    }

    @GetMapping("/saveorder2/{date}/{time}")
    private  CommonResult saveorder2(@PathVariable String date,@PathVariable int time){
        List<Judge> list = judgeService.list();

        List<Integer> list1 = judgeStateService.integerList(list, date,time);
        return CommonResult.success(list1);
    }

    @GetMapping("/rent/{date}/{time}/{usercode}")
    public  CommonResult rent(@PathVariable String date,@PathVariable int time,@PathVariable String usercode){
        Judge judge = new Judge();
        List<Integer> judgelist = judgeService.getJudges(judge,date,time);
        JudgeState judgeState = new JudgeState();
        judgeState.setDate(date);
        judgeState.setTime(time);
        List<Integer> msg = judgeStateService.insert(judgeState,judgelist,usercode);
        return CommonResult.success(msg);
    }

    @DeleteMapping ("/delete/{judgeorderid}")
    private CommonResult deleteorder(@PathVariable int judgeorderid){
        QueryWrapper<JudgeState> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",judgeorderid);
          judgeStateService.remove(queryWrapper);
          return CommonResult.success();
    }

    @GetMapping("/getjudgestateinfo/{judgeorderid}")
    private CommonResult getjudgestateinfo(@PathVariable int judgeorderid){
        QueryWrapper<JudgeState> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", judgeorderid);
        JudgeState judgeState = judgeStateService.getOne(queryWrapper);
        return CommonResult.success(judgeState);
    }
}