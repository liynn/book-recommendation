package com.wy.util;

import com.wy.domain.Book;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by wy on 2017/3/14.
 */
public class FileUtil {

    public static void readFile(String srcName,String destName){
        File srcFile = new File(srcName);
        File destFile = new File(destName);
        try(BufferedReader reader = new BufferedReader(new FileReader(srcFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(destFile))){
            String len;
            while ((len = reader.readLine()) != null){
                writer.write(len);
                writer.newLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void writerFile(String srcName,Book book){
        File srcFile = new File(srcName);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(srcFile,true))){
            writer.write(book.getId()
                    +"::"+book.getName()
                    +"::"+book.getAuthor()
                    +"::"+book.getPress()
                    +"::"+book.getTags()
                    +"::"+book.getImgUrl());
            writer.newLine();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
