package com.wy.dao;

import com.wy.domain.Book;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookDao {
    Book get(@Param("id") Integer id);

    Integer create(Book book);

    List<Book> paging(@Param("bookId") Integer bookId, @Param("name") String name, @Param("limit") Integer limit, @Param("offset") Integer offset);

    Integer count(@Param("bookId") Integer bookId, @Param("name") String name);
}
