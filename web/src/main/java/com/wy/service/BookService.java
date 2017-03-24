package com.wy.service;

import com.wy.domain.Book;

import java.util.List;

/**
 * Created by wy on 2017/3/23.
 */
public interface BookService {
    /**
     *  根据图书编号或名称查询图书信息
     *  @param id 图书编号
     *  @param name 图书名称
     */
    List<Book> getAllBook(String id, String name);
}
