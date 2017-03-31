package com.wy.common;

import com.wy.util.HadoopUtil;
import com.wy.util.PropertyUtil;

import org.apache.spark.SparkConf;
import org.apache.spark.deploy.yarn.Client;
import org.apache.spark.deploy.yarn.ClientArguments;

/**
 * Created by wy on 2017/3/29.
 */
public class SparkTask {
    public static void submit() {
        String[] args = new String[]{
                "--jar", "/Users/wy/workspace/book-recommendation/recommend/target/recommend-1.0-SNAPSHOT.jar",
                "--class", "com.wy.BuildRecommendModel",
                "--arg", "hdfs://master:9000/book/ratings.dat",
                "--arg", "10",
                "--arg", "0.1",
                "--arg", "20",
                "--arg", "hdfs://master:9000/model"
        };
        System.setProperty("SPARK_YARN_MODE", "true");
        SparkConf sparkConf = new SparkConf().setMaster("yarn-client").setAppName("BuildRecommendModel");
        sparkConf.set("spark.yarn.jars", PropertyUtil.getProperty("spark.yarn.jars"));

        ClientArguments arguments = new ClientArguments(args);
        new Client(arguments, HadoopUtil.getConf(), sparkConf).run();

    }

    public static void main(String[] args){
        submit();
    }
}
