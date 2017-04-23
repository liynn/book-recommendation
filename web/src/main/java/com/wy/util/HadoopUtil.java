package com.wy.util;


import com.google.common.collect.Lists;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HadoopUtil {

    public static final String PREFIX = "/part-00000";

    /**
     * HADOOP配置信息设置
     */
    public static Configuration getConf() {
        Configuration conf = new Configuration();
        //HDFS文件路径
        conf.set("fs.defaultFS", PropertyUtil.getProperty("fs.defaultFS"));
        //配置使用yarn框架
        conf.set("mapreduce.framework.name", PropertyUtil.getProperty("mapreduce.framework.name"));
        //配置resourcemanager
        conf.set("yarn.resourcemanager.address", PropertyUtil.getProperty("yarn.resourcemanager.address"));
        //配置资源分配器
        conf.set("yarn.resourcemanager.scheduler.address", PropertyUtil.getProperty("yarn.resourcemanager.scheduler.address"));
        conf.set("mapreduce.jobhistory.address", PropertyUtil.getProperty("mapreduce.jobhistory.address"));
        return conf;
    }

    /**
     * 读取HADOOP文件信息
     *
     * @param srcName 读取文件的路径
     */
    public static List<String> read(String srcName) {
        List<String> result = Lists.newArrayList();
        Configuration conf = getConf();
        Path path = new Path(srcName);
        String line;
        try (FileSystem fileSystem = FileSystem.get(conf)) {
            if (fileSystem.exists(path)) {
                FSDataInputStream inputStream = fileSystem.open(path);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    result.add(line);
                }
                reader.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断文件或目录是否存在
     */
    public static Boolean isExist(String srcName) {
        Configuration conf = getConf();
        Path path = new Path(srcName);
        Boolean result = null;
        try (FileSystem fileSystem = FileSystem.get(conf)) {
            result = fileSystem.exists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除HADOOP指定文件或目录
     *
     * @param srcName 删除文件的路径
     */
    public static void delete(String srcName) {
        Configuration conf = getConf();
        Path path = new Path(srcName);
        try (FileSystem fileSystem = FileSystem.get(conf)) {
            if (fileSystem.exists(path)) {
                if (fileSystem.isDirectory(path)) {
                    fileSystem.delete(path, true);
                } else {
                    fileSystem.delete(path, false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
