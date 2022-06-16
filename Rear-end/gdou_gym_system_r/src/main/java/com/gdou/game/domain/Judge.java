package com.gdou.game.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("judge")
public class Judge {
    private Integer judgeid;
    private String name;
    private String age;
    private String details;
    private String avatar;
}
