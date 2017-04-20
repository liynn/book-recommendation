package com.wy.util;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by wy on 2017/3/26.
 */
public class FileUtil {
    /**
     * 追加写入数据列表
     *
     * @param destName 写入地址
     * @param contents 数据列表
     */
    public static void write(String destName, List<String> contents) {
        for (String content : contents) {
            write(destName, content);
        }
    }

    /**
     * 追加写入数据
     *
     * @param destName 写入地址
     * @param content  写入内容
     */
    public static void write(String destName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destName, true))) {
            writer.write(content + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件数据
     *
     * @param srcName 文件地址
     */
    public static List<String> read(String srcName) {
        List<String> list = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(srcName))) {
            list = Lists.newArrayList();
            String data;
            while ((data = reader.readLine()) != null) {
                if (!list.contains(data)) {
                    list.add(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
