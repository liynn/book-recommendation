package com.wy.param;

import java.util.List;

import lombok.Data;

/**
 * Created by wy on 2017/4/27.
 */
@Data
public class BookRatingParam {
    private Integer userId; //用户编号
    List<RatingParam> ratings; //用户评分图书集合
}
