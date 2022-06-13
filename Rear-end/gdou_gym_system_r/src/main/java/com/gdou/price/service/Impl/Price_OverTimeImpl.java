package com.gdou.price.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.price.dao.Price_OverTimeMapper;
import com.gdou.price.domain.Price_OverTime;
import com.gdou.price.service.IPrice_OverTimeService;
import org.springframework.stereotype.Service;

@Service
public class Price_OverTimeImpl extends ServiceImpl<Price_OverTimeMapper, Price_OverTime> implements IPrice_OverTimeService {
}
