package com.wy.util;

import com.alibaba.fastjson.JSON;
import com.wy.model.BookModel;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by wy on 2017/3/25.
 */
@Slf4j
public class HttpRequestUtil {

    //请求地址
    private static final String URL = "https://api.douban.com/v2/book/";

    public static String getBookInfo(String bookId) {
        StringBuilder entityStringBuilder = null;
        HttpGet get = new HttpGet(URL + bookId);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse httpResponse = httpClient.execute(get);) {
            HttpEntity entity = httpResponse.getEntity();
            entityStringBuilder = new StringBuilder();
            if (null != entity) {
                try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"), 8 * 1024)) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        entityStringBuilder.append(line + "\n");
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取图书信息失败:{}",e);
            e.printStackTrace();
        }
        return entityStringBuilder.toString();
    }

    public static void main(String[] args) {
        String line = getBookInfo("6082808");
        BookModel bookModel = JSON.parseObject(line, BookModel.class);
        System.out.println(bookModel);
    }
}
