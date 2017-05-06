package com.wy.param;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * Created by wy on 2017/4/27.
 */
@Data
public class RatingParam {
    private Integer bookId;
    @NotBlank(message = "评分不能为空")
    private Double score;
}
