package com.gdou.tools.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.UserToolsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface TStateMapper extends BaseMapper<TState> {

    String getAllSql = "select t.id,t.price,t.kind,s.date,s.time from gym_system.tools t,gym_system.tools_state s where t.id = s.toolscode and s.usercode = #{usercode}";

    @Select(getAllSql)
    Page<UserToolsVO> page(Page<UserToolsVO> page , @Param("usercode") String usercode);
}
