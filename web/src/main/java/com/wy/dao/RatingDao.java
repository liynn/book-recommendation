package com.wy.dao;

import com.wy.domain.Rating;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wy on 2017/4/12.
 */
@Mapper
public interface RatingDao {
    Integer create(Rating rating);

    List<Rating> getByUserId(@Param("userId") Integer userId);
}
