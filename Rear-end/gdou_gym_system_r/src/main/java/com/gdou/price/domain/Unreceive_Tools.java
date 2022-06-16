package com.gdou.price.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("unreceive")
public class Unreceive_Tools {
    @TableId
    private Integer id;
    private String usercode;
    private float price;
    private Integer priceid;
}
