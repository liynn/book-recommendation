package com.wy.param;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;

/**
 * Created by wy on 2017/4/27.
 */
@Data
public class BookRatingParam {
    @NotBlank(message = "用户未登录")
    private Integer userId; //用户编号
    @Valid
    List<RatingParam> ratings; //用户评分图书集合
}
