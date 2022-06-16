package com.gdou.price.contoller;

import com.gdou.api.CommonResult;
import com.gdou.price.service.IBreak_ContractService;
import com.gdou.price.service.IUnreceive_Tools;
import com.gdou.state.domain.StateAppointment;
import com.gdou.state.service.StateAppointmentService;
import com.gdou.tools.domain.UserToolsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/break",produces="application/json")
public class Break_ContractController {
    @Autowired
    private IBreak_ContractService iBreak_contractService;

    @Autowired
    private IUnreceive_Tools iUnreceive_tools;

    @Autowired
    private StateAppointmentService stateAppointmentService;
    /**
     * 获取用户的违规信息 用于公告展示
     * @return
     */
    @GetMapping()
    public CommonResult getContract(){
        return CommonResult.success(iBreak_contractService.getDescription());
    }

    /**
     * 获取用户待缴费记录
     * @param usercode
     * @return
     */
    @GetMapping("{usercode}")
    public CommonResult getUnreceive(@PathVariable String usercode){
        return CommonResult.success(iUnreceive_tools.getUnreceive(usercode));
    }

    /**
     * 获取失约记录项
     * @param usercode
     * @return
     */
    @GetMapping("count/{usercode}")
    public CommonResult getCount(@PathVariable String usercode){
        List<UserToolsVO> unreceive = iUnreceive_tools.getUnreceive(usercode);
        List<StateAppointment> promise = stateAppointmentService.getPromise(usercode);
        return CommonResult.success(unreceive.size()+promise.size());
    }
}
