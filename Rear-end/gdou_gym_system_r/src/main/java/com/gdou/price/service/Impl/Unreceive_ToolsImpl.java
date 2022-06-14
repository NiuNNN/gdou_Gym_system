package com.gdou.price.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
     * @param id 订单号
     * @return 收费
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

    /**
     * 记录付款记录
     * @param id 订单号
     * @return
     */
    @Override
    public boolean cancelRegister(Integer id) {
        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();//获取当前时间

        String format = formatter.format(now.getTime());

        QueryWrapper<Unreceive_Tools> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("usercode",price_tools.getUsercode());
        List<Unreceive_Tools> unreceive_tools = unreceive_toolsMapper.selectList(queryWrapper);

        System.out.println(unreceive_tools);

        for(int i =0;i<arrList.size();i++){
            UpdateWrapper<TState> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",arrList.get(i)).set("actually",format).set("ractually",format).set("receive","已领取");
            tStateMapper.update(null,updateWrapper);
        }

        UpdateWrapper<Price_Tools> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id).set("price",unreceive_tools.get(0).getPrice()).set("state","是");

        unreceive_toolsMapper.deleteById(unreceive_tools.get(0).getId());

        return price_toolsMapper.update(null,updateWrapper) >0;
    }
}
