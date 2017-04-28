package com.wy.dao;

import com.wy.domain.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {

    User get(@Param("id") Integer id);

    Integer create(User user);

    User login(@Param("phone") String phone, @Param("password") String password);

    User getByPhone(@Param("phone") String phone);
}
