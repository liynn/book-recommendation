package com.wy.controller;

import com.wy.common.SparkTask;
import com.wy.service.BookService;
import com.wy.util.ApplicationUtil;
import com.wy.util.Response;
import com.wy.vo.BookVO;
import com.wy.vo.UserDetailVO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.annotation.Resource;

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
     * @param rank     隐藏因子数
     * @param lambda   正则化参数
     * @param iterator 迭代次数
     */
    @ResponseBody
    @RequestMapping(value = "/book/model", method = RequestMethod.POST)
    public String getBookRecommendModel(@RequestParam(value = "address", defaultValue = "/data/ratings.dat", required = false) String address,
                                        @RequestParam(value = "rank", defaultValue = "10", required = false) String rank,
                                        @RequestParam(value = "lambda", defaultValue = "0.1", required = false) String lambda,
                                        @RequestParam(value = "iterator", defaultValue = "10", required = false) String iterator) {
        return sparkTask.buildRecommendModel(address, rank, lambda, iterator);
    }

    /**
     * 获取推荐模型进度
     *
     * @param appId 任务编号
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
    public Response<UserDetailVO> getUserDetail(@RequestParam(value = "userId") Integer userId) {
        if (null == userId || !bookService.isExist(userId)) {
            return Response.fail("用户不存在");
        }
        UserDetailVO userDetail = bookService.getUserDetail(userId);
        if (CollectionUtils.isEmpty(userDetail.getBookList())) {
            return Response.fail("未找到用户阅读记录");
        }
        return Response.ok(userDetail);
    }

    /**
     * 进行图书推荐
     *
     * @param userId 需要推荐的图书用户编号
     * @param number 推荐数量
     */
    @ResponseBody
    @RequestMapping(value = "/book/recommend", method = RequestMethod.GET)
    public Response<List<BookVO>> recommendBook(@RequestParam(value = "userId") String userId,
                                                @RequestParam(value = "number", defaultValue = "9", required = false) Integer number) {
        if (StringUtils.isEmpty(userId) || !bookService.isExist(Integer.parseInt(userId))) {
            return Response.fail("用户不存在");
        }
        List<String> bookAllIds = sparkTask.getRecommendResult(userId);
        if (CollectionUtils.isEmpty(bookAllIds)) {
            return Response.fail("图书模型未建立");
        }
        return Response.ok(bookService.listForIds(bookAllIds, number));
    }
}

