package com.gdou.state.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.state.domain.StateAppointment;

import java.util.List;

public interface StateAppointmentService extends IService<StateAppointment> {
    List<StateAppointment> selectAll();

    boolean deleteById(Long id);

    boolean insertAppointment(StateAppointment stateAppointment);

    boolean updateAppointment(StateAppointment stateAppointment);

    boolean updateAppointmentSelect(Long id);

    StateAppointment selectById(Long id);

    List<StateAppointment> getRent(String usercode);

    List<StateAppointment> getPromise(String usercode);
}
