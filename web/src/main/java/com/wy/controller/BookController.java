package com.wy.controller;

import com.wy.common.SparkTask;
import com.wy.service.BookService;
import com.wy.util.ApplicationUtil;
import com.wy.vo.BookVO;
import com.wy.vo.UserDetailVO;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.annotation.Resource;

/**
 * Created by wy on 2017/3/23.
 */
@Controller
public class BookController {

    @Resource
    private SparkTask sparkTask;

    @Resource
    private BookService bookService;

    /**
     * 跳转首页
     *
     * @return 首页地址
     */
    @RequestMapping(value = "/home")
    public String getHome() {
        return "home";
    }

    /**
     * 获取图书推荐模型
     *
     * @param address  图书评分地址
     * @param iterator 迭代次数
     */
    @ResponseBody
    @RequestMapping(value = "/book/model", method = RequestMethod.POST)
    public String getBookRecommendModel(@RequestParam(value = "address") String address,
                                        @RequestParam(value = "rank") String rank,
                                        @RequestParam(value = "lambda") String lambda,
                                        @RequestParam(value = "iterator") String iterator) {
        return sparkTask.buildRecommendModel(address, rank, lambda, iterator);
    }

    /**
     * 获取推荐模型进度
     *
     * @param appId 任务应用编号
     * @return 进度数
     */
    @ResponseBody
    @RequestMapping(value = "/model/progress", method = RequestMethod.GET)
    public String getModelProgress(@RequestParam(value = "appId") String appId) {
        return ApplicationUtil.getApplicationStatus(appId);
    }

    /**
     * 得到用户基本信息和评分图书信息
     *
     * @param userId 用户编号
     */
    @ResponseBody
    @RequestMapping(value = "/user/detail", method = RequestMethod.GET)
    public UserDetailVO getUserDetail(@RequestParam("userId") Integer userId) {
        return bookService.getUserDetail(userId);
    }

    /**
     * 进行图书推荐
     *
     * @param userId 需要推荐的图书用户编号
     * @param number 推荐数量
     */
    @ResponseBody
    @RequestMapping(value = "/book/recommend", method = RequestMethod.GET)
    public List<BookVO> recommendBook(@RequestParam("userId") String userId, @RequestParam("number") Integer number) {
        List<String> bookAllIds = sparkTask.getRecommendResult(userId);
        return bookService.listForIds(bookAllIds, number);
    }
}
