package com.gdou.state.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.state.domain.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {
    boolean insertNotice(Notice notice);

    boolean deleteById(Long id);

    boolean updateNotice(Notice notice);

    Notice selectById(Long id);

    List<Notice> selectAll();
}
