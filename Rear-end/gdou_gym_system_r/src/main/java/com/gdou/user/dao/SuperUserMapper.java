package com.gdou.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdou.user.domain.SuperUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SuperUserMapper extends BaseMapper<SuperUser> {
}
