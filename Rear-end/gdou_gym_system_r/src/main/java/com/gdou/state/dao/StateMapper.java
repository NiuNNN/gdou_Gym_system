package com.gdou.state.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdou.state.domain.State;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StateMapper extends BaseMapper<State> {
}
