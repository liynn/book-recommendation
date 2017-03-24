package com.wy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wy on 2017/3/23.
 */
@Controller
public class BookController {

    @RequestMapping("/home")
    public String getHome(){
        return "home";
    }
}
