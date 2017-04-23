package com.wy.test;

import com.google.common.collect.Lists;

import com.wy.domain.Book;
import com.wy.domain.Rating;
import com.wy.domain.User;
import com.wy.service.BookService;
import com.wy.util.FileUtil;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import javax.annotation.Resource;

public class BookTest extends BaseTest {

    @Resource
    private BookService bookService;

    /**
     * 添加用户信息
     */
    @Test
    public void testAddUser() {
        List<String> users = FileUtil.read("/Users/wy/Documents/毕业设计/data/users.dat");
        //转换
        List<User> userList = Lists.newArrayList();
        users.forEach(data -> {
            String[] result = data.split("::");
            User user = new User();
            user.setName(result[1]);
            user.setSex(result[2]);
            user.setAge(Integer.parseInt(result[3]));
            user.setPhone(result[4]);
            user.setEmail(result[5]);
            userList.add(user);
        });
        //存储
        int count = 0;
        for (User user : userList) {
            count += bookService.addUser(user);
        }
        Assert.assertEquals(userList.size(), count);
    }

    /**
     * 添加图书信息
     */
    @Test
    public void testAddBook() {
        List<String> books = FileUtil.read("/Users/wy/Documents/毕业设计/data/books.dat");
        //转换
        List<Book> bookList = Lists.newArrayList();
        books.forEach(data -> {
            String[] result = data.split("::");
            Book book = new Book();
            book.setId(Integer.parseInt(result[0]));
            book.setName(result[1]);
            book.setAuthor(result[2]);
            book.setPublisher(result[3]);
            book.setPrice(result[4]);
            book.setImage(result[5]);
            book.setTag(result[6]);
            bookList.add(book);
        });
        //存储
        int count = 0;
        for (Book book : bookList) {
            count += bookService.addBook(book);
        }
        Assert.assertEquals(bookList.size(), count);
    }

    /**
     * 添加评分信息
     */
    @Test
    public void testAddRating() {
        List<String> ratings = FileUtil.read("/Users/wy/Documents/毕业设计/data/ratings.dat");
        //转换
        List<Rating> ratingList = Lists.newArrayList();
        ratings.forEach(data -> {
            String[] result = data.split("::");
            Rating rating = new Rating();
            rating.setUserId(Integer.parseInt(result[0]));
            rating.setBookId(Integer.parseInt(result[1]));
            rating.setScore(Double.parseDouble(result[2]));
            rating.setTimestamp(result[3]);
            ratingList.add(rating);
        });
        //存储
        int count = 0;
        for (Rating rating : ratingList) {
            count += bookService.addRating(rating);
        }
        Assert.assertEquals(ratingList.size(), count);
    }
}
