package com.gdou.api;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectExceptionAdvice{
    //拦截所有的异常信息
    @ExceptionHandler(Exception.class)
    public CommonResult doException(Exception ex){
        //记录日志
        //通知运维
        //通知开发
        ex.printStackTrace();
        return CommonResult.failed("服务器故障，请稍后再试");
    }
}
