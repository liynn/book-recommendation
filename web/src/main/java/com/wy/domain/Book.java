package com.wy.domain;

import lombok.Data;

@Data
public class Book {
    private Integer id; //编号
    private String name; //名称
    private String author; //作者
    private String publisher; //出版社
    private String price; //价格
    private String image; //图片地址
    private String tag; //标签
}
