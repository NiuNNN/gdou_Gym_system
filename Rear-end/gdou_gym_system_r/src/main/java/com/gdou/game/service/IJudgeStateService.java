package com.gdou.game.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.game.domain.Judge;
import com.gdou.game.domain.JudgeState;


import java.util.List;

public interface IJudgeStateService extends IService<JudgeState>{


    List<Integer> insert(JudgeState judgeState, List<Integer> judgeList,String usercode);

    List<Judge> getJudgeId();

    List<JudgeState> judgeStateList(QueryWrapper queryWrapper);

    List<Integer> integerList(QueryWrapper queryWrapper);

    List<Integer> integerList(List<Judge> list, String date, Integer time);
}
