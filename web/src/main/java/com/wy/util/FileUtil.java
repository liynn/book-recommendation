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

    public static void write(String destName, List<String> contents) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destName, true))) {
            for (String content : contents){
                writer.write(content+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(String destName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destName, true))) {
            writer.write(content+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static List<String> read(String srcName){
        List<String> list = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(srcName))) {
            list = Lists.newArrayList();
            String data;
            while ((data = reader.readLine())!= null){
                if(!list.contains(data)){
                    list.add(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        write("/Users/wy/ids.dat",read("/Users/wy/books.dat"));
    }
}
