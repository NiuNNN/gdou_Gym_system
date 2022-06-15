package com.gdou.tools.controller;

import com.gdou.api.CommonResult;
import com.gdou.tools.domain.Kind;
import com.gdou.tools.service.IKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/kinds",produces="application/json")
public class ToolsKindController {
    @Autowired
    private IKindService iKindService;

    /**
     * 获取器材种类以及价格
     * @return 器材种类表数据
     */
    @GetMapping
    public CommonResult getKind() {
        List<Kind> list = iKindService.list();
        if (list.size() != 0) return CommonResult.success(list);
        else return CommonResult.failed();
    }
}
