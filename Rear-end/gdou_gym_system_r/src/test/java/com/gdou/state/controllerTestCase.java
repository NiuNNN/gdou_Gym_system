package com.gdou.state;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdou.price.dao.Price_ToolsMapper;
import com.gdou.price.domain.Price_Tools;
import com.gdou.tools.dao.TStateMapper;
import com.gdou.tools.domain.TState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class controllerTestCase {
    @Autowired
    private TStateMapper tStateMapper;

    @Autowired
    private Price_ToolsMapper price_toolsMapper;
    @Test
    void testSave(){
        int num = 4;

        List<Integer> toolsList = new ArrayList<>();
        toolsList.add(1);
        toolsList.add(2);
        toolsList.add(3);
        toolsList.add(4);

        Random random = new Random();
        int number = 0;

//        把随机抽取出来的器材存进list中
        List<Integer> toolsCode = new ArrayList<>();
        for(int i=0;i<num;i++){
            number = random.nextInt(toolsList.size());
            toolsCode.add(toolsList.get(number));
            toolsList.remove(number);
        }

        System.out.println(toolsCode);
        TState tState = new TState();
        tState.setUsercode("201911701310");
        tState.setToolscode(3);
        tState.setDate("2022-06-07");
        tState.setTime(3);
        int insert = tStateMapper.insert(tState);
        System.out.println(tState);

    }


    @Test
    void testDelete(){
        TState tState = tStateMapper.selectById(4);
//        System.out.println(tState);
        QueryWrapper<Price_Tools> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("usercode",tState.getUsercode())
                .eq("date",tState.getDate())
                .eq("time",tState.getTime());
        List<Price_Tools> price_tools = price_toolsMapper.selectList(queryWrapper);
//        System.out.println(price_tools);

        for (int i = 0;i<price_tools.size();i++){
            String[] str = price_tools.get(i).getToolslist().split(",");
            List<String> list1= Arrays.asList(str);
            List<String> arrList = new ArrayList<String>(list1);
            System.out.println(arrList);
            for(int j=0;j<arrList.size();j++){
                if(arrList.get(j).equals("4")){
                    arrList.remove("4");
                    String s ="";
                    for (int z = 0;z<arrList.size();z++){
                        s+=arrList.get(z);
                        if(z != arrList.size()-1) s+=",";
                    }
                    UpdateWrapper<Price_Tools> updateWrapper = new UpdateWrapper<>();
                    System.out.println(s);
                    updateWrapper.eq("id",price_tools.get(i).getId()).set("toolslist",s);
                    price_toolsMapper.update(null,updateWrapper);
                }
            }
        }
    }
}
