package com.gdou.state.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Notice {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String content;
}
