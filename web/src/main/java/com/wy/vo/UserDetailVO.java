package com.wy.vo;

import java.util.List;

import lombok.Data;

@Data
public class UserDetailVO {
    private Integer userId; //编号
    private String name;//姓名
    private String sex; //性别
    private Integer age; //年龄
    private String phone; //手机号
    private String email; //邮箱
    List<BookVO> bookList;//用户评过分的图书
}
