package com.wy.service;

import com.wy.dao.BookDao;
import com.wy.domain.Book;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by wy on 2017/3/23.
 */
@Slf4j
@Service("bookService")
public class BookServiceImpl implements BookService{

    @Resource
    private BookDao bookDao;

    @Override
    public Book getById(String id){
        return bookDao.getById(id);
    }
}
