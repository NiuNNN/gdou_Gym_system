package com.gdou.price.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("price_tools")
public class Price_Tools {
    private Integer id;//订单号
    private String usercode;//用户id
    private String toolslist;//器材登记
    private String price;//需付款金额
    private String state;//付款状态
    private String date;
    private Integer time;
}
