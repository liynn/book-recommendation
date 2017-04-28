package com.wy.service;

import com.wy.domain.Book;
import com.wy.domain.Rating;
import com.wy.domain.User;
import com.wy.vo.BookVO;
import com.wy.vo.PagingVO;
import com.wy.vo.UserDetailVO;
import com.wy.vo.UserVO;

import java.util.List;

public interface BookService {

    /**
     * 添加用户数据
     *
     * @param user 用户信息
     */
    Boolean addUser(User user);

    /**
     * 添加图书数据
     *
     * @param book 图书信息
     */
    Boolean addBook(Book book);

    /**
     * 添加评分数据
     *
     * @param rating 评分信息
     */
    Boolean addRating(Rating rating);

    /**
     * 根据图书编号查询所有的图书信息
     *
     * @param bookAllIds 图书编号list
     * @param number     推荐数目
     */
    List<BookVO> listForIds(List<String> bookAllIds, Integer number);

    /**
     * 根据用户编号查询用户基本信息和评过分的图书信息和分数
     *
     * @param userId 用户编号
     */
    UserDetailVO getUserDetail(Integer userId);

    /**
     * 查询判断用户是否存在
     *
     * @param userId 用户编号
     */
    Boolean isExist(Integer userId);

    /**
     * 分页查询所有图书信息
     *
     * @param bookId   按编号搜索
     * @param name     按名称搜索
     * @param pageNo   页码
     * @param pageSize 页码大小
     */
    PagingVO<Book> paging(Integer bookId, String name, Integer pageNo, Integer pageSize);

    /**
     * 用户登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return 用户基本信息
     */
    UserVO login(String phone, String password);

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     */
    UserVO getUserByPhone(String phone);

    /**
     * 根据用户编号和图书编号查询评分记录
     *
     * @param userId 用户编号
     * @param bookId 图书编号
     */
    Rating getById(Integer userId, Integer bookId);
}

