package com.gdou.state.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.state.dao.AppointmentMapper;
import com.gdou.state.domain.State;
import com.gdou.state.domain.StateAppointment;
import com.gdou.state.service.StateAppointmentService;
import com.gdou.state.service.StateService;
import com.gdou.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateAppointmentServiceImpl extends ServiceImpl<AppointmentMapper, StateAppointment> implements StateAppointmentService {
    private StateService stateService;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    @Lazy
    public StateAppointmentServiceImpl(StateService stateService) {
        this.stateService = stateService;
    }

    @Override
    public List<StateAppointment> selectAll() {
        return list();
    }

    @Override
    public boolean deleteById(Long id) {
        StateAppointment stateAppointment = selectById(id);
        String name = stateAppointment.getName();
        String time = stateAppointment.getTime();
        LambdaQueryWrapper<State> wrapper = new QueryWrapper<State>().lambda().eq(State::getName, name).eq(State::getTime, time);
        State state = stateService.getOne(wrapper);
        state.setSelected("否");
        stateService.update(state, wrapper);
        return removeById(id);
    }

    @Override
    public boolean insertAppointment(StateAppointment stateAppointment) {
        String name = stateAppointment.getName();
        String time = stateAppointment.getTime();
        LambdaQueryWrapper<State> wrapper = new QueryWrapper<State>().lambda().eq(State::getName, name).eq(State::getTime, time);
        State state = stateService.getOne(wrapper);
        state.setSelected("是");
        stateService.update(state, wrapper);
        return save(stateAppointment);
    }

    @Override
    public boolean updateAppointmentSelect(Long id) {
    return true;
    }


    public boolean updateAppointment(StateAppointment stateAppointment) {
        return updateById(stateAppointment);
    }

    @Override
    public StateAppointment selectById(Long id) {
        return getById(id);
    }


    /**
     * 获取用户预约记录
     * @param usercode
     * @return
     */
    @Override
    public List<StateAppointment> getRent(String usercode) {
        QueryWrapper<StateAppointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",usercode).eq("usec","false");
        List<StateAppointment> stateAppointments = appointmentMapper.selectList(queryWrapper);
        List<String> time = TimeUtil.time();
        for(int i = 0;i<stateAppointments.size();i++){
            stateAppointments.get(i).setTime(time.get(Integer.parseInt(stateAppointments.get(i).getTime())));
        }
        return stateAppointments;
    }

    /**
     * 获取失约记录
     * @param usercode
     * @return
     */
    @Override
    public List<StateAppointment> getPromise(String usercode) {
        QueryWrapper<StateAppointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",usercode).eq("promise","true");
        List<StateAppointment> stateAppointments = appointmentMapper.selectList(queryWrapper);
        List<String> time = TimeUtil.time();
        for(int i = 0;i<stateAppointments.size();i++){
            stateAppointments.get(i).setTime(time.get(Integer.parseInt(stateAppointments.get(i).getTime())));
        }
        return stateAppointments;
    }
}
