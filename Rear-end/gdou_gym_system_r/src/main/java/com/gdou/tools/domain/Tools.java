package com.gdou.tools.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tools")
public class Tools {
    @TableId
    private Integer id;
    private String kind;
    private String price;
    private String safe;
    private String exist;
}
