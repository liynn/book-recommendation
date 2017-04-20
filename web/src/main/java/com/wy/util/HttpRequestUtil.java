package com.wy.util;

import com.alibaba.fastjson.JSON;
import com.wy.model.BookModel;
import com.wy.model.TagModel;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wy on 2017/3/25.
 */
public class HttpRequestUtil {

    //请求地址
    private static final String URL = "https://api.douban.com/v2/book/";

    /**
     * 发送请求获取豆瓣图书信息
     *
     * @param bookId 图书编号
     */
    public static String getBookInfo(String bookId) {
        StringBuilder entityStringBuilder = null;
        HttpGet get = new HttpGet(URL + bookId);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse httpResponse = httpClient.execute(get);) {
            HttpEntity entity = httpResponse.getEntity();
            entityStringBuilder = new StringBuilder();
            if (null != entity) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"), 8 * 1024)) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        entityStringBuilder.append(line + "\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityStringBuilder.toString();
    }

    public static void main(String[] args) {
        List<String> ids = FileUtil.read("/Users/wy/ids.dat");
        for (String data : ids) {
            String line = getBookInfo(data);
            BookModel bookModel = JSON.parseObject(line, BookModel.class);
            //作者数据处理
            String author = "";
            String[] authors = bookModel.getAuthor();
            List<String> authorList = Arrays.asList(authors);
            for (String value : authorList) {
                if (authorList.indexOf(value) != authorList.size() - 1) {
                    author = author + value + ",";
                } else {
                    author = author + value;
                }
            }
            //标签数据处理
            List<TagModel> tagList = bookModel.getTags();
            String tag = "";
            for (TagModel value : tagList) {
                if (tagList.indexOf(value) != tagList.size() - 1) {
                    tag = tag + value.getName() + "|";
                } else {
                    tag = tag + value.getName();
                }
            }
            String content = bookModel.getId() + "::" + bookModel.getTitle() + "::" + author + "::" + bookModel.getPublisher() + "::" + bookModel.getPrice() + "::" + bookModel.getImages().getLarge() + "::" + tag;
            System.out.println(content);
            FileUtil.write("/Users/wy/Documents/毕业设计/data/books.dat", content);
        }
    }
}
