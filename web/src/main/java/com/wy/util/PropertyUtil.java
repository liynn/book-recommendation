package com.wy.util;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by wy on 2017/3/29.
 */
public class PropertyUtil {
    private static Map<String,String> properties = Maps.newHashMap();

    static {
        Properties property = new Properties();
        try(InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream("book.properties")) {
            property.load(inputStream);

            properties.put("fs.defaultFS",property.getProperty("fs.defaultFS"));
            properties.put("mapreduce.framework.name",property.getProperty("mapreduce.framework.name"));
            properties.put("yarn.resourcemanager.address",property.getProperty("yarn.resourcemanager.address"));
            properties.put("yarn.resourcemanager.scheduler.address",property.getProperty("yarn.resourcemanager.scheduler.address"));
            properties.put("mapreduce.jobhistory.address",property.getProperty("mapreduce.jobhistory.address"));

            properties.put("spark.master",property.getProperty("spark.master"));
            properties.put("yarn.cluster",property.getProperty("yarn.cluster"));

            properties.put("book.ratings.dir",property.getProperty("book.ratings.dir"));
            properties.put("book.base.dir",property.getProperty("book.base.dir"));
            properties.put("user.base.dir",property.getProperty("user.base.dir"));
            properties.put("model.save.dir",property.getProperty("model.save.dir"));
            properties.put("result.save.dir",property.getProperty("result.save.dir"));

            properties.put("recommend.jar",property.getProperty("recommend.jar"));
            properties.put("model.main.class",property.getProperty("model.main.class"));
            properties.put("recommend.main.class",property.getProperty("recommend.main.class"));

            properties.put("model.submitted.progress",property.getProperty("model.submitted.progress"));
            properties.put("model.running.progress",property.getProperty("model.running.progress"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取属性值
     * @param key 属性key
     * @return
     */
    public static String getProperty(String key){
        return properties.get(key);
    }
}
