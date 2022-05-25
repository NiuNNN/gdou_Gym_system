package com.gdou.user.domain;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
@Data
@TableName("superuser")
public class SuperUser {
    private String code;
    private String password;
    private String name;
    private String sex;
    private int age;
    private String phone;
    private String email;
    private String avatar;
    private String zip;
    private String address;
}
