package com.gdou.game.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("game_create")
public class Game {
    private String userid;
    private Integer gameid;
    private String gamename;
    private String gametype;
    private String gametime;
    private String gamelocation;
    private String gametool;
    private String gamenum;
    private String locationid;
    private String toolid;
    private String judgeorderid;
}
