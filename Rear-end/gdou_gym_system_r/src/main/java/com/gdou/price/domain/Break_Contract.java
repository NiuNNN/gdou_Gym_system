package com.gdou.price.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("break_contract")
public class Break_Contract {
    @TableId
    private Integer id;
    private String usercode;
    private String description;
    private String date;
}
