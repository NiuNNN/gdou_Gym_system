package com.gdou.price.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.price.dao.Price_ToolsMapper;
import com.gdou.price.domain.Price_Tools;
import com.gdou.price.service.IPrice_ToolsService;
import com.gdou.tools.dao.TStateMapper;
import com.gdou.tools.dao.ToolsMapper;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.Tools;
import com.gdou.utils.TimeUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Service
public class Price_ToolsImpl extends ServiceImpl<Price_ToolsMapper, Price_Tools> implements IPrice_ToolsService {
    @Autowired
    private Price_ToolsMapper price_toolsMapper;

    @Autowired
    private TStateMapper tStateMapper;

    @Autowired
    private ToolsMapper toolsMapper;

    @Override
    public IPage<Price_Tools> getPage(int currentPage, int pageSize,Price_Tools priceTools) {
        //进行条件查询
        LambdaQueryWrapper<Price_Tools> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(priceTools.getUsercode()),Price_Tools::getUsercode,priceTools.getUsercode());
        lambdaQueryWrapper.like(Strings.isNotEmpty(priceTools.getDate()),Price_Tools::getDate,priceTools.getDate());
        lambdaQueryWrapper.like(priceTools.getTime()>0,Price_Tools::getTime,priceTools.getTime());
        IPage<Price_Tools> page = new Page(currentPage,pageSize);
        price_toolsMapper.selectPage(page,lambdaQueryWrapper);
        return page;
    }

    @Override
    public List<Integer> handleReceive(Integer id) {
        //把toolslist转换成list
        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号
//        System.out.println(arrList);

        //遍历tools_state表获取器材号
        List<Integer> tList = new ArrayList<>();//用于存放器材号
        for (int i =0;i<arrList.size();i++){
            TState tState = tStateMapper.selectById(arrList.get(i));
            tList.add(tState.getToolscode());
        }

        //遍历tools表 根据exist项来确认器材是否存在
        List<Integer> list = new ArrayList<>();
        for(int i =0;i<tList.size();i++){
            Tools tools = toolsMapper.selectById(tList.get(i));
            if(tools.getExist().equals("否")) {
                list.add(tools.getId());
            }
        }

        if(list.size()>0){
            //有器材不在 返回不在的器材
            return list;
        }
        else{
            //器材都在
            // 对tools表进行更新
            //获取当前时间
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatterd = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
            String now = formatterd.format(calendar.getTime());

            for (int i = 0;i<tList.size();i++){
                UpdateWrapper<Tools> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id",tList.get(i)).set("exist","否");
                toolsMapper.update(null,updateWrapper);
            }
            //对tools_state表进行更新
            for (int i = 0;i<arrList.size();i++){
                UpdateWrapper<TState> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id",arrList.get(i)).set("receive","已领取").set("actually",now);
                tStateMapper.update(null,updateWrapper);
            }
            //对price_tools表进行更改
            UpdateWrapper<Price_Tools> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id).set("state","是");
            price_toolsMapper.update(null,updateWrapper);
            list.clear();
            return list;
        }
    }

    @Override
    public String overTime(Integer id) {
       //从数据库中获取预约日期以及预约时间

        Price_Tools price_tools = price_toolsMapper.selectById(id);
        System.out.println(price_tools);
        return TimeUtil.getTime(price_tools.getDate(), price_tools.getTime());
    }

    @Override
    public boolean cancelRent(Integer id) {
        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> stringList= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(stringList);//应用于存放预留器材订单编号
        int flag = 0;

        for(int i = 0;i<arrList.size();i++){
            flag = tStateMapper.deleteById(arrList.get(i));
        }
        flag = price_toolsMapper.deleteById(id);
        return flag>0;
    }

}
