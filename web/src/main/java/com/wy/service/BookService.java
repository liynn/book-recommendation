package com.wy.service;

import com.wy.domain.Book;
import com.wy.domain.Rating;
import com.wy.domain.User;
import com.wy.vo.BookVO;
import com.wy.vo.UserDetailVO;

import java.util.List;

public interface BookService {

    /**
     * 添加用户数据
     *
     * @param user 用户信息
     */
    Integer addUser(User user);

    /**
     * 添加图书数据
     *
     * @param book 图书信息
     */
    Integer addBook(Book book);

    /**
     * 添加评分数据
     *
     * @param rating 评分信息
     */
    Integer addRating(Rating rating);

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
}
