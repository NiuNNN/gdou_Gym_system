package com.gdou.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.game.domain.Judge;

import java.util.List;

public interface IJudgeService extends IService<Judge>{
    List<Integer> getJudges(Judge judge, String date, int time);
}
