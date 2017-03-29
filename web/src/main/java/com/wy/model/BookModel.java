package com.wy.model;

import java.util.List;

import lombok.Data;

/**
 * Created by wy on 2017/3/25.
 */
@Data
public class BookModel {
    private String id;//图书编号
    private String title;//图书名称
    private String[] author; //图书作者
    private List<TagModel> tags; //标签
    private ImagesModel images;//图片地址
    private String publisher;//出版社
    private String price;//价格
}
