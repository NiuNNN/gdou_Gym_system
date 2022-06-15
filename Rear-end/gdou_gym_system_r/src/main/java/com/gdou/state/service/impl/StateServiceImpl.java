package com.gdou.state.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.state.dao.StateMapper;
import com.gdou.state.domain.State;
import com.gdou.state.domain.StateAppointment;
import com.gdou.state.service.StateAppointmentService;
import com.gdou.state.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StateServiceImpl extends ServiceImpl<StateMapper, State> implements StateService {
    private StateAppointmentService stateAppointmentService;

    @Autowired
    @Lazy
    public StateServiceImpl(StateAppointmentService stateAppointmentService) {
        this.stateAppointmentService = stateAppointmentService;
    }

    @Override
    public List<State> selectAll() {
        return list();
    }

//    @Override
//    public boolean deleteByName(String name) {
//        boolean success = remove(new QueryWrapper<State>().lambda().eq(State::getName, name));
//        LambdaQueryWrapper<StateAppointment> queryWrapper = new QueryWrapper<StateAppointment>().lambda().eq(StateAppointment::getName, name);
//        List<StateAppointment> stateAppointments = stateAppointmentService.list(queryWrapper);
//        if (!CollectionUtils.isEmpty(stateAppointments)) {
//            success = stateAppointmentService.remove(queryWrapper);
//        }
//        return success;
//    }

@Override
public boolean deleteById(Long id,String time,String name) {
    boolean success = remove(new QueryWrapper<State>().lambda().eq(State::getId, id));
    LambdaQueryWrapper<StateAppointment> queryWrapper = new QueryWrapper<StateAppointment>().lambda().eq(StateAppointment::getName, name).eq(StateAppointment::getTime,time );
    List<StateAppointment> stateAppointments = stateAppointmentService.list(queryWrapper);
    System.out.println(stateAppointments);
    if (!CollectionUtils.isEmpty(stateAppointments)) {
        success = stateAppointmentService.remove(queryWrapper);
    }
    return success;
}

    @Override
    public boolean insertState(State state) {
        List<State> states = list(new QueryWrapper<State>().lambda().eq(State::getName, state.getName()).eq(State::getTime, state.getTime()));
        if (!CollectionUtils.isEmpty(states)) {
            throw new RuntimeException("已存在");
        }
        return save(state);
    }

    @Override
    public List<String> selectByName(String name) {
        List<State> list = list(new QueryWrapper<State>().lambda().eq(State::getName, name));
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().map(State::getTime).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<BigDecimal> selectByNametop(String name) {
        List<State> list = list(new QueryWrapper<State>().lambda().eq(State::getName, name));
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().map(State::getPrice).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public boolean updateState(State state) {
        boolean success = false;
        try {
            success = updateById(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public State selectById(Long id) {
        return getById(id);
    }


}
