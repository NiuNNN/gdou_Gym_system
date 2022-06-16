package com.gdou.state.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdou.api.CommonResult;
import com.gdou.state.domain.State;
import com.gdou.state.domain.StateAppointment;
import com.gdou.state.service.StateAppointmentService;
import com.gdou.state.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private StateAppointmentService stateAppointmentService;

    @Autowired
    private StateService stateService;

    @PostMapping("/insertAppointment")
    public CommonResult<String> insertNotice(@RequestBody StateAppointment stateAppointment) {

        boolean success = stateAppointmentService.insertAppointment(stateAppointment);
        return success ? CommonResult.success() : CommonResult.failed();
    }

    @GetMapping("/deleteById")
    public CommonResult<String> deleteById(Long id) {
        boolean success = stateAppointmentService.deleteById(id);
        return success ? CommonResult.success() : CommonResult.failed();
    }
    //加的第2点
    @GetMapping("/updatePById")
    public CommonResult<String> updatePById(Long id, String name, String time) {
        StateAppointment select=stateAppointmentService.selectById(id);
        select.setPromise("true");
        boolean success =stateAppointmentService.updateAppointment(select);

        //更新state表
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("time", time);
        columnMap.put("name", name);
        List<State> stateList=stateService.listByMap(columnMap);
        stateList.forEach((state)->{
            state.setSelected("否");
            stateService.updateById(state);
        });
        return success ? CommonResult.success() : CommonResult.failed();
    }
//开始使用
    @GetMapping("/updateUById")
    public CommonResult<String> updateUById(Long id, String name, String time) {
        StateAppointment select=stateAppointmentService.selectById(id);
        select.setUsec("true");
        boolean success =stateAppointmentService.updateAppointment(select);

        //更新state表
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("time", time);
        columnMap.put("name", name);
        List<State> stateList=stateService.listByMap(columnMap);
        stateList.forEach((state)->{
            state.setTuse("true");
            stateService.updateById(state);
        });
        return success ? CommonResult.success() : CommonResult.failed();
    }

    //结束使用
    @GetMapping("/updateUfById")
    public CommonResult<String> updateUfById(Long id, String name, String time) {
        StateAppointment select=stateAppointmentService.selectById(id);
        select.setUsec("false");
        boolean success =stateAppointmentService.updateAppointment(select) &&  stateAppointmentService.deleteById(id);

        //更新state表
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("time", time);
        columnMap.put("name", name);
        List<State> stateList=stateService.listByMap(columnMap);
        stateList.forEach((state)->{
            state.setTuse("false");
            state.setSelected("否");
            stateService.updateById(state);
        });
        return success ? CommonResult.success() : CommonResult.failed();
    }

    //加的第3点
    @GetMapping("/getInfoByUserId")
    public CommonResult<List<StateAppointment>> getInfoByUserId(String userId) {
        Map<String, Object> columnMap = new HashMap<>();
        //通过userId获取数据
        columnMap.put("user_id",userId);
        List<StateAppointment> select = stateAppointmentService.listByMap(columnMap);
        return CommonResult.success(select);
    }


    @PostMapping("/updateNotice")
    public CommonResult<StateAppointment> updateNotice(@RequestBody StateAppointment stateAppointment) {
        boolean success = stateAppointmentService.updateAppointment(stateAppointment);
        return success ? CommonResult.success(stateAppointment) : CommonResult.failed();
    }

    @GetMapping("/selectById")
    public CommonResult<StateAppointment> selectById(Long id) {
        StateAppointment stateAppointment = stateAppointmentService.selectById(id);
        return stateAppointment != null ? CommonResult.success(stateAppointment) : CommonResult.failed();
    }


    @GetMapping("/selectAll")
    public CommonResult<List<StateAppointment>> selectAll() {
        return CommonResult.success(stateAppointmentService.selectAll());
    }

    /**
     * 获取用户的失约待付款记录
     * @param usercode 用户id
     * @return
     */
    @GetMapping("getPromise/{usercode}")
    public CommonResult getPromise(@PathVariable String usercode){
        return CommonResult.success(stateAppointmentService.getPromise(usercode));
    }


    /**
     * 获取用户的预约信息
     * @param usercode 用户id
     * @return
     */
    @GetMapping("rent/{usercode}")
    public CommonResult getRent(@PathVariable String usercode){
        return CommonResult.success(stateAppointmentService.getRent(usercode));
    }

    @PostMapping("/gametodelete/{id}")
    private  CommonResult gametodelete(@PathVariable int id){
        QueryWrapper<StateAppointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        stateAppointmentService.remove(queryWrapper);
        return CommonResult.success();
    }

    @PostMapping("/rent")
    public CommonResult<String> insertid(@RequestBody StateAppointment stateAppointment) {
        System.out.println(stateAppointment.getName());
        System.out.println(stateAppointment.getUserId());
        stateAppointmentService.save(stateAppointment);

        Long msg = stateAppointment.getId();
        String id = Long.toString(msg);
        return CommonResult.success(id);
    }
}
