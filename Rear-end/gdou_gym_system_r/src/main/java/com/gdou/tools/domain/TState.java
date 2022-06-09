package com.gdou.tools.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tools_state")
public class TState {
    private Integer id;
    private String usercode;
    private Integer toolscode;
    private String date;
    private Integer time;
    private String actually;
    private String receive;
    private String exist;
}
