package com.gdou.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdou.game.domain.Judge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JudgeMapper extends BaseMapper<Judge>{
    List<Judge> selectList();
}
