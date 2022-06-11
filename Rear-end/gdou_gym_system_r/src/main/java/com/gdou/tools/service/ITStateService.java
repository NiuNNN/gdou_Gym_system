package com.gdou.tools.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.UserToolsVO;

import java.util.List;

public interface ITStateService extends IService<TState> {
    List<Integer> insert(TState tState, int num,List<Integer> toolsList);
    Page<UserToolsVO> getPage(int currentPage, int pageSize, String usercode);
    boolean updateState(Integer id);
    String getToolscode(Integer id);
}
