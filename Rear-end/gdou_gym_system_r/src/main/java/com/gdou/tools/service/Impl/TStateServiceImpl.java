package com.gdou.tools.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.price.dao.Price_ToolsMapper;
import com.gdou.price.domain.Price_Tools;
import com.gdou.tools.dao.TStateMapper;
import com.gdou.tools.domain.TState;
import com.gdou.tools.service.ITStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<Integer> list = new ArrayList<>();

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

            list.add(tState.getId());
            list.add(tState.getToolscode());

            tState.setId(null);
        }

        //把租用信息存进price_tools表中
        price_tools.setUsercode(tState.getUsercode());
        price_tools.setToolslist(list.toString());
        System.out.println(list.toString());

        price_toolsMapper.insert(price_tools);

        list.add(price_tools.getId());
//        System.out.println(list);

        return list;
    }
}
