package com.gdou.price.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("price_overtime")
public class Price_OverTime {
    @TableId
    private int id;
    private float multiple;
    private String description;
}
