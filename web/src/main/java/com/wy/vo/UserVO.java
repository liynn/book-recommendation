package com.wy.vo;

import lombok.Data;

@Data
public class UserVO {
    private Integer id; //编号
    private String name;//姓名
    private String sex; //性别
    private Integer age; //年龄
    private String phone; //手机号
    private String email; //邮箱
}
