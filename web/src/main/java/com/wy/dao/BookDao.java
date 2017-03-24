package com.wy.dao;

import com.wy.domain.Book;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/3/23.
 */
@Mapper
public interface BookDao {
    List<Book> queryAll(Map<String,Object> critera);
}
