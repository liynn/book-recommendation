package com.wy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wy on 2017/3/15.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String id; //图书编号
    private String name; //图书名称
    private String author;//图书作者
    private String press;//出版社
    private String tags;//图书标签
    private String imgUrl;//图片地址
}
