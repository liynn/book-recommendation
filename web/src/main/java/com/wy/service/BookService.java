package com.wy.service;

import com.wy.domain.Book;

/**
 * Created by wy on 2017/3/23.
 */
public interface BookService {
    /**
     *  根据图书编号查询图书信息
     *  @param id 图书编号
     */
    Book getById(String id);
}
