package com.wy.domain;

import lombok.Data;

/**
 * Created by wy on 2017/4/12.
 */
@Data
public class Rating {
    private Integer userId; //用户编号
    private Integer bookId; //图书编号
    private Double score; //分数
    private String timestamp;//评分时间戳
}
