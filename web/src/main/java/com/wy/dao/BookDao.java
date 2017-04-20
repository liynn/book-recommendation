package com.wy.dao;

import com.wy.domain.Book;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by wy on 2017/3/23.
 */
@Mapper
public interface BookDao {
    Book get(@Param("id") Integer id);

    Integer create(Book book);
}
