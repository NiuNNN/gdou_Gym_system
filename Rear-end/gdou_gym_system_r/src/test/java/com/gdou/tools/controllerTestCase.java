package com.gdou.tools;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdou.tools.dao.TStateMapper;
import com.gdou.tools.dao.ToolsMapper;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.Tools;
import com.gdou.tools.service.IToolsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class controllerTestCase {
    @Autowired
    private IToolsService iToolsService;

    @Autowired
    private ToolsMapper toolsMapper;

    @Autowired
    private TStateMapper tStateMapper;


    @Test
    void testGetPrice(){
        String kind = "篮球";
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("distinct kind,price").eq("kind",kind);
        List list = toolsMapper.selectList(queryWrapper);
        System.out.println(list);
    }

    @Test
    void testCheckTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String start ="2022-06-07";
        Date date = new Date();
        String end ="2022-06-07";
        QueryWrapper<Tools> queryWrapper = new QueryWrapper<Tools>();
        queryWrapper.eq("kind","篮球")
                .eq("price","￥1.00/小时")
                .eq("safe","否");
        List<Tools> l1 = toolsMapper.selectList(queryWrapper);
        System.out.println(l1);
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<l1.size();i++){
            list.add(l1.get(i).getId());
        }
        System.out.println(list);
        QueryWrapper<TState> queryWrapper1 = new QueryWrapper<TState>();
        queryWrapper1.eq("date",start)
                     .eq("time",1)
                     .in("toolscode",list);
        List<TState>  l2= tStateMapper.selectList(queryWrapper1);
        System.out.println(l2);
        for (int i = 0;i<l2.size();i++){
            for (int j = 0;j<list.size();j++){
                if(list.get(j).equals(l2.get(i).getToolscode())){
                    list.remove(j);
                }
            }
        }
        System.out.println(list);
    }

    @Test
    void testList_toString(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(1);
        String str ="";
        for (int i = 0;i<list.size();i++){
            str+=list.get(i);
            if(i != list.size()-1) str+=",";
        }
        System.out.println(str);
    }

    @Test
    void testPrice(){
        String str = "￥3.00/次";
        String substring = str.substring(1);
        System.out.println(substring);
        String[] split = substring.split("/");
        System.out.println(split[0]);
        System.out.println(Float.valueOf(split[0]).toString()+"0");

    }

}
