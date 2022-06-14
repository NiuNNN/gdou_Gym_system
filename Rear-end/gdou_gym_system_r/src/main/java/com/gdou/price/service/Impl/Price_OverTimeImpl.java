package com.gdou.price.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.price.dao.Price_OverTimeMapper;
import com.gdou.price.domain.Price_OverTime;
import com.gdou.price.service.IPrice_OverTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Price_OverTimeImpl extends ServiceImpl<Price_OverTimeMapper, Price_OverTime> implements IPrice_OverTimeService {
    @Autowired
    private Price_OverTimeMapper price_overTimeMapper;

    @Override
    public String getFine() {
        Price_OverTime price_overTime1 = price_overTimeMapper.selectById(1);
        Price_OverTime price_overTime2 = price_overTimeMapper.selectById(2);
        return "1、预约范围时间内归还，按原来预定价格付款"+"\n"+"2、"+price_overTime1.getDescription()+"\n"+"3、"+price_overTime2.getDescription();
    }
}
