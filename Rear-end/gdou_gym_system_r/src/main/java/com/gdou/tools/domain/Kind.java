package com.gdou.tools.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tools_kind")
public class Kind {
    private Integer id;
    private String kind;
}
