package com.wy.service.impl;

import com.google.common.collect.Lists;

import com.wy.dao.BookDao;
import com.wy.dao.RatingDao;
import com.wy.dao.UserDao;
import com.wy.domain.Book;
import com.wy.domain.Rating;
import com.wy.domain.User;
import com.wy.service.BookService;
import com.wy.util.BeanCopyUtil;
import com.wy.vo.BookVO;
import com.wy.vo.PagingVO;
import com.wy.vo.UserDetailVO;
import com.wy.vo.UserVO;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("bookService")
public class BookServiceImpl implements BookService {

    @Resource
    private UserDao userDao;

    @Resource
    private BookDao bookDao;

    @Resource
    private RatingDao ratingDao;

    @Override
    public Boolean addUser(User user) {
        return userDao.create(user) == 1;
    }

    @Override
    public Boolean addBook(Book book) {
        return bookDao.create(book) == 1;
    }

    @Override
    public Boolean addRating(Rating rating) {
        return ratingDao.create(rating) == 1;
    }

    @Override
    public List<BookVO> listForIds(List<String> bookAllIds, Integer number) {
        if (CollectionUtils.isEmpty(bookAllIds)) {
            return Lists.newArrayList();
        }
        List<BookVO> bookVOList = Lists.newArrayList();
        for (int i = 0; i < bookAllIds.size(); i++) {
            if (i > number - 1) {
                break;
            }
            String[] data = bookAllIds.get(i).split(",");
            int id = Integer.parseInt(data[0].replace("(", ""));
            double score = new BigDecimal(Double.parseDouble(data[1].replace(")", "")))
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            BookVO bookVO = BeanCopyUtil.genBean(bookDao.get(id), BookVO.class);
            bookVO.setScore(score);
            bookVOList.add(bookVO);
        }
        return bookVOList;
    }

    @Override
    public UserDetailVO getUserDetail(Integer userId) {
        //查询用户基本信息
        UserDetailVO userDetailVO = new UserDetailVO();
        User dbUser = userDao.get(userId);
        userDetailVO.setUserId(dbUser.getId());
        userDetailVO.setName(dbUser.getName());
        userDetailVO.setSex(dbUser.getSex());
        userDetailVO.setAge(dbUser.getAge());
        userDetailVO.setPhone(dbUser.getPhone());
        userDetailVO.setEmail(dbUser.getEmail());
        //查询评分信息
        List<Rating> dbRatings = ratingDao.getByUserId(userId);
        if (CollectionUtils.isNotEmpty(dbRatings)) {
            List<BookVO> books = Lists.newArrayList();
            dbRatings.forEach(rating -> {
                //查询图书信息
                BookVO book = BeanCopyUtil.genBean(bookDao.get(rating.getBookId()), BookVO.class);
                book.setScore(rating.getScore());
                books.add(book);
            });
            userDetailVO.setBookList(books);
        }
        return userDetailVO;
    }

    @Override
    public Boolean isExist(Integer userId) {
        if (userId == null) {
            return Boolean.FALSE;
        }
        User dbUser = userDao.get(userId);
        if (dbUser == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public PagingVO<Book> paging(Integer bookId, String name, Integer pageNo, Integer pageSize) {
        PagingVO<Book> result = new PagingVO<>();
        int limit = (pageNo - 1) * pageSize;
        List<Book> paging = bookDao.paging(bookId, name, limit, pageSize);
        if (CollectionUtils.isNotEmpty(paging)) {
            result.setData(paging);
            Integer count = bookDao.count(bookId, name);
            result.setTotal(count);
            result.setPageNo(pageNo);
            result.setPageSize(pageSize);
            result.setPageNumber(count, pageSize);
        }
        return result;
    }

    @Override
    public UserVO login(String phone, String password) {
        User dbUser = userDao.login(phone, password);
        if (dbUser == null) {
            return null;
        }
        return BeanCopyUtil.genBean(dbUser, UserVO.class);
    }

    @Override
    public UserVO getUserByPhone(String phone) {
        User dbUser = userDao.getByPhone(phone);
        if (dbUser == null) {
            return null;
        }
        return BeanCopyUtil.genBean(dbUser, UserVO.class);
    }

    @Override
    public Rating getById(Integer userId, Integer bookId) {
        return ratingDao.getById(userId, bookId);
    }
}
