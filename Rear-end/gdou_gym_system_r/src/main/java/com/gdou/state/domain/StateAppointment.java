package com.gdou.state.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StateAppointment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userId;
    private String name;
    private String date;
    private String time;
    private String promise;
    private BigDecimal price;
}
