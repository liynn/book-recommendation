package com.wy.common;

import org.apache.spark.deploy.SparkSubmit;

/**
 * Created by wy on 2017/3/29.
 */
public class SparkTask {
    public static void submit() {
//        String[] args = new String[]{
//                "--name", "BuildRecommendModel",
//                "--class", "com.wy.BuildRecommendModel",
//                "--driver-memory", "2g",
//                "--executor-memory", "2g",
//                "--num-executors", "1",
//                "--jar", "/Users/wy/recommend-1.0-SNAPSHOT.jar",
//                "--files", "/usr/local/hadoop/etc/hadoop/yarn-site.xml",
//                "--arg", "book/ratings.dat",
//                "--arg", "10",
//                "--arg", "0.1",
//                "--arg", "20",
//                "--arg", "model"
//        };
//
//        System.setProperty("SPARK_YARN_MODE", "true");
//        SparkConf sparkConf = new SparkConf();
//        sparkConf.set("spark.yarn.scheduler.heartbeat.interval-ms",
//                "1000");
//
//        ClientArguments arguments = new ClientArguments(args);
//        new Client(arguments, HadoopUtil.getConf(), sparkConf).run();
        String[] args = new String[]{
                "--master","spark://master:7077",
                "--deploy-mode", "client",
                "--name", "BuildRecommendModel",
                "--class", "com.wy.BuildRecommendModel",
                "--driver-memory", "2g",
                "--executor-memory", "2g",
                "--executor-cores", "1",
                "/Users/wy/recommend-1.0-SNAPSHOT.jar",
                "book/ratings.dat",
                "10",
                "0.1",
                "10",
                "model"
        };
        SparkSubmit.main(args);
    }

    public static void main(String[] args) {
        submit();
    }
}
