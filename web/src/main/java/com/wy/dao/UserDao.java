package com.wy.dao;

import com.wy.domain.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by wy on 2017/4/12.
 */
@Mapper
public interface UserDao {

    User get(@Param("id") Integer id);

    Integer create(User user);
}
