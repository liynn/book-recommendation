package com.wy.dao;

import com.wy.domain.Book;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookDao {
    Book get(@Param("id") Integer id);

    Integer create(Book book);
}
