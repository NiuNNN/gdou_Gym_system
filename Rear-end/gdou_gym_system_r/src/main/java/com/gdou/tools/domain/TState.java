package com.gdou.tools.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tools_state")
public class TState {
    @TableId
    private Integer id;
    private String usercode;
    private Integer toolscode;
    private String date;
    private Integer time;
    private String actually;
    private String ractually;
    private String receive;
}
