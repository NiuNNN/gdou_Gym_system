package com.gdou.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdou.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
