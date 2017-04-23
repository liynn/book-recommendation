package com.wy.dao;

import com.wy.domain.Rating;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RatingDao {
    Integer create(Rating rating);

    List<Rating> getByUserId(@Param("userId") Integer userId);
}
