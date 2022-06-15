package com.gdou.state.controller;

import com.gdou.api.CommonResult;
import com.gdou.state.domain.Notice;
import com.gdou.state.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @PostMapping("/insertNotice")
    public CommonResult<String> insertNotice( Notice notice) {
        boolean success = noticeService.insertNotice(notice);
        return success ? CommonResult.success() : CommonResult.failed();
    }

    @GetMapping("/deleteById")
    public CommonResult<String> deleteById(Long id) {
        boolean success = noticeService.deleteById(id);
        return success ? CommonResult.success() : CommonResult.failed();
    }

    @PostMapping("/updateNotice")
    public CommonResult<Notice> updateNotice(@RequestBody Notice notice) {
        boolean success = noticeService.updateNotice(notice);
        return success ? CommonResult.success(notice) : CommonResult.failed();
    }

    @GetMapping("/selectById")
    public CommonResult<Notice> selectById(Long id) {
        Notice notice = noticeService.selectById(id);
        return notice != null ? CommonResult.success(notice) : CommonResult.failed();
    }

    @GetMapping("/selectAll")
    public CommonResult<List<Notice>> selectAll() {
        return CommonResult.success(noticeService.selectAll());
    }
}