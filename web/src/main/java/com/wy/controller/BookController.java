package com.wy.controller;

import com.wy.common.SparkTask;
import com.wy.domain.Book;
import com.wy.domain.Rating;
import com.wy.domain.User;
import com.wy.param.BookRatingParam;
import com.wy.param.RatingParam;
import com.wy.param.UserParam;
import com.wy.service.BookService;
import com.wy.util.ApplicationUtil;
import com.wy.util.BeanCopyUtil;
import com.wy.util.HadoopUtil;
import com.wy.util.PropertyUtil;
import com.wy.util.Response;
import com.wy.vo.BookVO;
import com.wy.vo.PagingVO;
import com.wy.vo.UserDetailVO;
import com.wy.vo.UserVO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
     * 用户登录
     *
     * @param phone    手机号
     * @param password 密码
     */
    @ResponseBody
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public Response<UserVO> login(@RequestParam(value = "phone") String phone,
                                  @RequestParam(value = "password") String password, HttpSession session) {
        UserVO userVO = bookService.login(phone, password);
        if (userVO == null) {
            return Response.fail("登录失败，请检查手机号码或密码是否正确!");
        }
        session.setAttribute("userId", userVO.getId());
        session.setAttribute("name", userVO.getName());
        return Response.ok(userVO);
    }

    /**
     * 用户登出
     */
    @ResponseBody
    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    public Response<String> logout(HttpSession session) {
        session.removeAttribute("userId");
        session.removeAttribute("name");
        return Response.ok();
    }

    /**
     * 用户注册
     *
     * @param param 用户对象
     */
    @ResponseBody
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public Response<String> register(@Valid @RequestBody UserParam param, BindingResult result) {
        //参数校验
        if (result.hasErrors()) {
            return Response.fail(result.getAllErrors().get(0).getDefaultMessage());
        }
        //校验密码是否一致
        if (!param.getPassword().equals(param.getConfirmPassword())) {
            return Response.fail("输入密码不一致");
        }
        //校验手机号是否已经被注册
        if (bookService.getUserByPhone(param.getPhone()) != null) {
            return Response.fail("该手机号已经被注册");
        }
        //添加新用户
        User user = BeanCopyUtil.genBean(param, User.class);
        if (!bookService.addUser(user)) {
            return Response.fail("添加用户失败");
        }
        //写入HDFS文件中
        String content = "\r\n" + user.getId() + "::" + user.getName() + "::" + user.getSex() + "::" + user.getAge() + "::" + user.getPhone() + "::" + user.getEmail();
        HadoopUtil.write(PropertyUtil.getProperty("user.base.dir"), content);
        return Response.ok();
    }

    /**
     * 分页获取所有图书信息
     *
     * @param bookId   图书编号
     * @param name     图书名称
     * @param pageNo   页码
     * @param pageSize 页面大小
     */
    @ResponseBody
    @RequestMapping(value = "/book/paging", method = RequestMethod.GET)
    public Response<PagingVO<Book>> paging(@RequestParam(value = "bookId", required = false) Integer bookId,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "pageNo") Integer pageNo,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return Response.ok(bookService.paging(bookId, name, pageNo, pageSize));
    }

    /**
     * 添加新的评分记录
     *
     * @param param 评分信息对象
     */
    @ResponseBody
    @RequestMapping(value = "book/addRatings", method = RequestMethod.POST)
    public Response<String> addRatings(@Valid @RequestBody BookRatingParam param, BindingResult result) {
        //参数校验
        if (result.hasErrors()) {
            return Response.fail(result.getAllErrors().get(0).getDefaultMessage());
        }
        List<RatingParam> ratingParams = param.getRatings();
        String timestamp = String.valueOf(System.currentTimeMillis());
        if (CollectionUtils.isNotEmpty(ratingParams)) {
            ratingParams.forEach(ratingParam -> {
                Rating rating = new Rating();
                rating.setUserId(param.getUserId());
                rating.setBookId(ratingParam.getBookId());
                rating.setScore(ratingParam.getScore());
                rating.setTimestamp(timestamp);
                if (bookService.getById(param.getUserId(), ratingParam.getBookId()) == null) {
                    if (bookService.addRating(rating)) {
                        //将评分数据添加到HDFS中
                        HadoopUtil.write(PropertyUtil.getProperty("book.ratings.dir"), "\r\n" + param.getUserId() + "::" + ratingParam.getBookId() + "::" + ratingParam.getScore().intValue() + "::" + timestamp);
                    }
                }
            });
            //若模型存在，则删除重新建立新模型
            if (HadoopUtil.isExist(PropertyUtil.getProperty("model.save.dir"))) {
                HadoopUtil.delete(PropertyUtil.getProperty("model.save.dir"));
            }
        }
        return Response.ok();
    }

    /**
     * 获取图书推荐模型
     *
     * @param rank     隐藏因子数
     * @param lambda   正则化参数
     * @param iterator 迭代次数
     */
    @ResponseBody
    @RequestMapping(value = "/book/model", method = RequestMethod.POST)
    public String getBookRecommendModel(@RequestParam(value = "rank", defaultValue = "10", required = false) String rank,
                                        @RequestParam(value = "lambda", defaultValue = "0.01", required = false) String lambda,
                                        @RequestParam(value = "iterator", defaultValue = "10", required = false) String iterator) {
        return sparkTask.buildRecommendModel(rank, lambda, iterator);
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

