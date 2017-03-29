package com.wy.util;


import org.apache.hadoop.conf.Configuration;

/**
 * Created by wy on 2017/3/29.
 */
public class HadoopUtil {
    public static Configuration getConf() {
        Configuration conf = new Configuration();
        //配置跨平台提交任务
        conf.setBoolean("mapreduce.app-submission.cross-platform", Boolean.parseBoolean(PropertyUtil.getProperty("mapreduce.app-submission.cross-platform")));
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

}
