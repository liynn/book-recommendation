package com.wy.util;

import com.google.common.collect.Maps;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

public class RatingDataUtil {

    private static final Integer userCount = 1000;

    /**
     * 构造评分数据
     *
     * @param fileName 评分数据保存路径
     */
    public static void getRatingsData(String fileName) {
        List<String> bookList = FileUtil.read(Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("web/target/classes/", "") + "data/books.dat");
        for (int i = 1; i <= userCount; i++) {
            int number = getNum(10, 50);
            Map<String, String> ratings = Maps.newHashMap();
            for (int j = 0; j < number; j++) {
                String key = i + "::" + getRandomBookId(bookList);
                if (ratings.get(key) == null) {
                    FileUtil.write(fileName, key + "::" + getNum(1, 5) + "::" + System.currentTimeMillis());
                    ratings.put(key, String.valueOf(i));
                }
            }
        }
    }

    /**
     * 构造个人评分数据
     *
     * @param fileName 个人评分数据保存路径
     */
    public static void getPersonalRatingsData(String fileName) {
        List<String> bookList = FileUtil.read(Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("web/target/classes/", "") + "data/books.dat");
        int number = getNum(10, 50);
        File file = new File(fileName);
        for (int i = 0; i < number; i++) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(0 + "::" + getRandomBookId(bookList) + "::" + getNum(1, 5) + "::" + System.currentTimeMillis());
                writer.newLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到随机图书编号
     *
     * @param books 所有图书信息
     */
    public static String getRandomBookId(List<String> books) {
        String id = books.get(getNum(0, books.size() - 1)).split("::")[0];
        id = StringUtils.isNotEmpty(id) ? id : getRandomBookId(books);
        System.out.println(id);
        return id;
    }

    /**
     * 得到随机数
     *
     * @param start 开始范围
     * @param end   结束范围
     */
    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static void main(String[] args) {
        getRatingsData("/Users/wy/Documents/毕业设计/data/ratings.dat");
    }
}
