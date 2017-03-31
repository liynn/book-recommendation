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
        try(InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream("hadoop.properties")) {
            property.load(inputStream);
            properties.put("mapreduce.app-submission.cross-platform",property.getProperty("mapreduce.app-submission.cross-platform"));
            properties.put("fs.defaultFS",property.getProperty("fs.defaultFS"));
            properties.put("mapreduce.framework.name",property.getProperty("mapreduce.framework.name"));
            properties.put("yarn.resourcemanager.address",property.getProperty("yarn.resourcemanager.address"));
            properties.put("yarn.resourcemanager.scheduler.address",property.getProperty("yarn.resourcemanager.scheduler.address"));
            properties.put("mapreduce.jobhistory.address",property.getProperty("mapreduce.jobhistory.address"));
            properties.put("spark.yarn.jars",property.getProperty("spark.yarn.jars"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        return properties.get(key);
    }
}
