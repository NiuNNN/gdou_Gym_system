package com.gdou.tools.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.tools.domain.TState;

import java.util.List;

public interface ITStateService extends IService<TState> {
    List<Integer> insert(TState tState, int num,List<Integer> toolsList);
}
