package com.gdou.game.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("judge_state")

public class JudgeState {
    private  Integer id;
    private  Integer judgeid;
    private  String date;
    private  Integer time;


}
