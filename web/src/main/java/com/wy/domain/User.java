package com.wy.domain;

import lombok.Data;

/**
 * Created by wy on 2017/4/12.
 */
@Data
public class User {
    private Integer id; //编号
    private String name; //姓名
    private String sex; //性别
    private Integer age; //年龄
    private String phone; //手机号
    private String email; //邮箱
}
