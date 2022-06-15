package com.gdou.state.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.state.dao.NoticeMapper;
import com.gdou.state.domain.Notice;
import com.gdou.state.service.NoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
    @Override
    public boolean insertNotice(Notice notice) {
        return save(notice);
    }

    @Override
    public boolean deleteById(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateNotice(Notice notice) {
        return updateById(notice);
    }

    @Override
    public Notice selectById(Long id) {
        return getById(id);
    }

    @Override
    public List<Notice> selectAll() {
        return list();
    }
}