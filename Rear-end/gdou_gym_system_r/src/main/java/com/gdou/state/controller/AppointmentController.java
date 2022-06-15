package com.gdou.state.controller;

import com.gdou.api.CommonResult;
import com.gdou.state.domain.StateAppointment;
import com.gdou.state.service.StateAppointmentService;
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
    public CommonResult<String> updatePById(Long id) {
        StateAppointment select=stateAppointmentService.selectById(id);
        select.setPromise("是");
        boolean success =stateAppointmentService.updateAppointment(select);
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
}
