package com.gdou.game.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.gdou.game.domain.JudgeState;
import com.gdou.tools.domain.TState;
import com.gdou.tools.domain.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gdou.game.service.IJudgeService;
import com.gdou.game.domain.Judge;
import com.gdou.game.dao.JudgeMapper;

import com.gdou.game.dao.JudgeStateMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class JudgeServiceImpl extends ServiceImpl<JudgeMapper, Judge> implements  IJudgeService{
    @Autowired
    JudgeMapper judgeMapper;

    @Autowired
    JudgeStateMapper judgeStateMapper;

    @Override
    public List<Integer>  getJudges(Judge judge,String date, int time){
        QueryWrapper<Judge> queryWrapper = new QueryWrapper<>();


        List<Judge> l1 = judgeMapper.selectList();

        //2.创建一个list用于过滤出l1里的id 器材编号
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<l1.size();i++){
            list.add(l1.get(i).getJudgeid());
        }
//        System.out.println(list);

        //3、然后通过时间、时间段以及器材编号查找是否又符合的器材数据
//        System.out.println(date);
//        System.out.println(time);
        QueryWrapper<JudgeState> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("date",date)
                .eq("time",time)
                .in("judgeid",list);
        List<JudgeState> l2 = judgeStateMapper.selectList(queryWrapper1);
//        System.out.println(l2);

        //4、筛选出符合条件的器材 然后返回
        for (int i = 0;i<l2.size();i++){
            for (int j = 0;j<list.size();j++){
                if(list.get(j).equals(l2.get(i).getJudgeid())){
                    list.remove(j);
                }
            }
        }
//        System.out.println(list);
        return list;
    }



}
