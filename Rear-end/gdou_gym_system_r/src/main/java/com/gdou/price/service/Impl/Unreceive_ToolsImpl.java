package com.gdou.price.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.price.dao.Price_ToolsMapper;
import com.gdou.price.dao.Unreceive_ToolsMapper;
import com.gdou.price.domain.Price_Tools;
import com.gdou.price.domain.Unreceive_Tools;
import com.gdou.price.service.IUnreceive_Tools;
import com.gdou.tools.dao.TStateMapper;
import com.gdou.tools.dao.ToolsMapper;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.Tools;
import com.gdou.utils.PriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class Unreceive_ToolsImpl extends ServiceImpl<Unreceive_ToolsMapper, Unreceive_Tools> implements IUnreceive_Tools {
    @Autowired
    private Unreceive_ToolsMapper unreceive_toolsMapper;

    @Autowired
    private Price_ToolsMapper price_toolsMapper;

    @Autowired
    private TStateMapper tStateMapper;

    @Autowired
    private ToolsMapper toolsMapper;

    /**
     * 对用户超时未领取器材进行登记
     * @param id
     * @return
     */
    @Override
    public boolean register(Integer id) {
        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号

        int num = arrList.size();//获取选择器材的数量

        TState tState = tStateMapper.selectById(arrList.get(0));

        Tools tools = toolsMapper.selectById(tState.getToolscode());

        Float price = PriceUtil.strFormatDouble(tools.getPrice());//获取价格
        System.out.println(price);

        Float allPrice = num * price;

        Unreceive_Tools unreceive_tools = new Unreceive_Tools();

        unreceive_tools.setUsercode(price_tools.getUsercode());
        unreceive_tools.setPrice(allPrice);

        return unreceive_toolsMapper.insert(unreceive_tools)>0;
    }
}
