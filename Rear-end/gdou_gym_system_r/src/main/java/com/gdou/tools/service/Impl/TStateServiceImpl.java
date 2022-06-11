package com.gdou.tools.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.price.dao.Price_ToolsMapper;
import com.gdou.price.domain.Price_Tools;
import com.gdou.tools.dao.TStateMapper;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.UserToolsVO;
import com.gdou.tools.service.ITStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class TStateServiceImpl extends ServiceImpl<TStateMapper, TState> implements ITStateService {
    @Autowired
    private TStateMapper tStateMapper;

    @Autowired
    private Price_ToolsMapper price_toolsMapper;

    @Override
    public List<Integer> insert(TState tState, int num,List<Integer> toolsList) {
        List<Integer> l1 = new ArrayList<>();//用户返回给用户器材id
        List<Integer> l2 = new ArrayList<>();//用于存进price_tools

//        生成随机数 然后从可选器材里中随机取出一个
        Random random = new Random();
        int number = 0;

//        System.out.println(toolsList);
        Price_Tools price_tools = new Price_Tools();

        for(int i=0;i<num;i++){
            number = random.nextInt(toolsList.size());
            tState.setToolscode(toolsList.get(number));
            tStateMapper.insert(tState);
            toolsList.remove(number);

            l1.add(tState.getToolscode());
            l2.add(tState.getId());

            tState.setId(null);
        }

        //把租用信息存进price_tools表中
        price_tools.setUsercode(tState.getUsercode());
        String str ="";
        for (int i = 0;i<l2.size();i++){
            str+=l2.get(i);
            if(i != l2.size()-1) str+=",";
        }
        price_tools.setToolslist(str);
        price_tools.setDate(tState.getDate());
        price_tools.setTime(tState.getTime());
        System.out.println(str);

        price_toolsMapper.insert(price_tools);

        l1.add(price_tools.getId());
//        System.out.println(list);

        return l1;
    }

    @Override
    public Page<UserToolsVO> getPage(int currentPage, int pageSize, String usercode) {
        Page<UserToolsVO> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        tStateMapper.page(page,usercode);
        return page;
    }

    @Override
    public boolean updateState(Integer id) {
        TState tState = tStateMapper.selectById(id);
        QueryWrapper<Price_Tools> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("usercode",tState.getUsercode())
                .eq("date",tState.getDate())
                .eq("time",tState.getTime());

        List<Price_Tools> price_tools = price_toolsMapper.selectList(queryWrapper);


        for (int i = 0;i<price_tools.size();i++){
            String[] str = price_tools.get(i).getToolslist().split(",");
            List<String> list1= Arrays.asList(str);
            List<String> arrList = new ArrayList<String>(list1);
            System.out.println(arrList);
            for(int j=0;j<arrList.size();j++) {
                if (arrList.get(j).equals(id.toString())) {
                    arrList.remove(id.toString());
                    String s = "";
                    for (int z = 0; z < arrList.size(); z++) {
                        s += arrList.get(z);
                        if (z != arrList.size() - 1) s += ",";
                    }
                    UpdateWrapper<Price_Tools> updateWrapper = new UpdateWrapper<>();
                    System.out.println(s);
                    if(s.equals("")){
                        price_toolsMapper.deleteById(price_tools.get(i).getId());
                    }
                    else{
                        updateWrapper.eq("id", price_tools.get(i).getId()).set("toolslist", s);
                        price_toolsMapper.update(null, updateWrapper);
                    }
                }
            }
        }

        return tStateMapper.deleteById(id) > 0;
    }

    @Override
    public String getToolscode(Integer id) {
        Price_Tools price_tools = price_toolsMapper.selectById(id);
        String[] str = price_tools.getToolslist().split(",");
        List<String> list1= Arrays.asList(str);
        List<String> arrList = new ArrayList<String>(list1);
        String toolscode = "";
        for(int i = 0;i<arrList.size();i++){
            TState tState = tStateMapper.selectById(arrList.get(i));
            toolscode += tState.getToolscode();
            if(i < arrList.size()-1 ) toolscode +=",";
        }
        return toolscode;
    }
}
