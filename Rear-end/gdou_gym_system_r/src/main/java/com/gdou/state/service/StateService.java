package com.gdou.state.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.state.domain.State;

import java.math.BigDecimal;
import java.util.List;

public interface StateService extends IService<State> {
    List<State> selectAll();

//    boolean deleteByName(String name);
boolean deleteById(Long Id,String time,String name);

    boolean insertState(State state);

    List<String> selectByName(String name);

    List<BigDecimal> selectByNametop(String name);

}
