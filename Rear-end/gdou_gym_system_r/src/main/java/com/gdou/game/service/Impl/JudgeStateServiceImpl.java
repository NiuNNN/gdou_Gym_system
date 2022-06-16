package com.gdou.game.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.game.dao.JudgeMapper;
import com.gdou.game.domain.Judge;
import com.gdou.price.dao.Price_ToolsMapper;
import com.gdou.price.domain.Price_Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import  com.gdou.game.service.IJudgeStateService;
import  com.gdou.game.domain.JudgeState;
import com.gdou.game.dao.JudgeStateMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class JudgeStateServiceImpl extends ServiceImpl<JudgeStateMapper, JudgeState> implements IJudgeStateService{
    @Autowired
    JudgeStateMapper judgeStateMapper;
    @Autowired
    private JudgeMapper judgeMapper;

    @Autowired
    private Price_ToolsMapper price_toolsMapper;

    @Override
    public List<Integer> insert(JudgeState judgeState, List<Integer> judgeList,String usercode){
        List<Integer> l1 = new ArrayList<>();//用户返回给用户器材id
        List<Integer> l2 = new ArrayList<>();
        Random random = new Random();
        int number = 0;
        int num = 1;
        Price_Tools price_tools = new Price_Tools();
        for(int i=0;i<num;i++){
            number = random.nextInt(judgeList.size());
            judgeState.setJudgeid(judgeList.get(number));
            judgeStateMapper.insert(judgeState);
            judgeList.remove(number);

            l1.add(judgeState.getJudgeid());
            l1.add(judgeState.getId());

            l2.add(judgeState.getId());

            judgeState.setId(null);
        }
        price_tools.setUsercode(usercode);
        String str ="";
        for (int i = 0;i<l2.size();i++){
            str+=l2.get(i);
            if(i != l2.size()-1) str+=",";
        }
        price_tools.setToolslist(str);
        price_tools.setDate(judgeState.getDate());
        price_tools.setTime(judgeState.getTime());
        price_toolsMapper.insert(price_tools);
        QueryWrapper<Price_Tools> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",price_tools.getId());

        price_toolsMapper.delete(queryWrapper);
        return l1;
    }

    @Override
    public List<Judge> getJudgeId() {
        List<Judge> judgeList = judgeMapper.selectList();
        return judgeList;
    }

    @Override
    public  List<JudgeState> judgeStateList(QueryWrapper queryWrapper){
        List<JudgeState> list = judgeStateMapper.selectList(queryWrapper);
        if(list.size()==0){
            return null;
        }else{
            return list;
        }
    }

    @Override
    public List<Integer> integerList(QueryWrapper queryWrapper){
        List<Integer> list = judgeStateMapper.selectList(queryWrapper);
        if(list.size()==0){
            return null;
        }else{
            return  list;
        }
    }

    @Override
    public List<Integer> integerList(List<Judge> list, String date, Integer time){
        List<Integer> list1 =new ArrayList<>();

        JudgeState judgeState = new JudgeState();
        for(int i =0 ; i < list.size();i++){
            QueryWrapper<JudgeState> queryWrapper =new QueryWrapper<>();
            queryWrapper.eq("judgeid",list.get(i).getJudgeid())
                    .eq("date",date)
                    .eq("time",time);
            List<JudgeState> judgeStateList = judgeStateMapper.selectList(queryWrapper);
            if(judgeStateList.size()==0) {
                judgeState.setJudgeid(list.get(i).getJudgeid());
                judgeState.setTime(time);
                judgeState.setDate(date);
                judgeStateMapper.insert(judgeState);
                list1.add(judgeStateList.get(i).getId());
                break;
            }else{
                continue;
            }
        }
        return list1;
    }


}
