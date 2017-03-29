package com.wy.controller;

import com.wy.common.SparkTask;
import com.wy.domain.Book;
import com.wy.service.BookService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by wy on 2017/3/23.
 */
@Controller
public class BookController {

    @Resource
    private BookService bookService;

    @RequestMapping("/home")
    public String getHome(){
        return "home";
    }

    @RequestMapping("/spark")
    public String testSpark(){
        SparkTask.submit();
        return "home";
    }

    @ResponseBody
    @RequestMapping("/detail")
    public Book getById(@RequestParam(name = "id") String id){
        return bookService.getById(id);
    }
}
