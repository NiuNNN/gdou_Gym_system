package com.gdou.state.controller;

import com.gdou.api.CommonResult;
import com.gdou.state.domain.State;
import com.gdou.state.service.StateService;
import com.gdou.state.service.StateAppointmentService;
import com.gdou.state.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/state")
public class StateController {
    @Autowired
    private StateService stateService;
    @Autowired
    private StateAppointmentService stateAppointmentService;
    @Autowired
    private NoticeService noticeService;

    //修改的第4点
    @GetMapping("/selectAll")
    public CommonResult<List<State>> selectAll() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("selected", "否");
        return CommonResult.success(stateService.listByMap(columnMap));
    }

    //保留之前的接口
    @GetMapping("/selectAllItem")
    public CommonResult<List<State>> selectAllItem() {
        return CommonResult.success(stateService.selectAll());
    }
//    @GetMapping("/deleteByName")
//    public CommonResult<String> deleteByName(String name) {
//        boolean success = stateService.deleteByName(name);
//        return success ? CommonResult.success() : CommonResult.failed();
//    }

    //修改的第1点
    @GetMapping("/deleteById")
    public CommonResult<String> deleteByName(Long id, String name, String time) {
        boolean success = stateService.deleteById(id, name, time);
        Map<String, Object> columnMap = new HashMap<>();
        //删除预约表内
        columnMap.put("time", time);
        columnMap.put("name", name);
        stateAppointmentService.removeByMap(columnMap);

        return success ? CommonResult.success() : CommonResult.failed();
    }


    @PostMapping("/insertState")
    public CommonResult<String> insertState(@RequestBody State state) {
        boolean success = stateService.insertState(state);

        return success ? CommonResult.success() : CommonResult.failed("场地名称已存在！");
    }

    //通过名字获取时间段
    @GetMapping("/selectByName")
    public CommonResult<List<String>> selectByName(String name) {
        Map<String, Object> columnMap = new HashMap<>();

        //筛选selected未否的名称
        columnMap.put("selected", "否");
        columnMap.put("name", name);
        List<String> stringArr = new ArrayList<>();
        List<State> stateList = stateService.listByMap(columnMap);
        stateList.forEach(state -> {
            stringArr.add(state.getTime());
        });
        return CommonResult.success(stringArr);
    }

    //通过名字获取价格
    @GetMapping("/selectByNametop")
    public CommonResult<List<BigDecimal>> selectByNametop(String name) {
        Map<String, Object> columnMap = new HashMap<>();
        //筛选selected未否的价格
        columnMap.put("selected", "否");
        columnMap.put("name", name);
        List<BigDecimal> priceArr = new ArrayList<>();
        List<State> stateList = stateService.listByMap(columnMap);
        stateList.forEach(state -> {
            priceArr.add(state.getPrice());
        });
        return CommonResult.success(priceArr);
    }
}
