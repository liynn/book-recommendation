package com.wy.common;

import com.google.common.collect.Lists;

import com.wy.util.HadoopUtil;
import com.wy.util.PropertyUtil;

import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
public class SparkTask {

    private SparkLauncher launcher;

    public SparkTask() {
        launcher = new SparkLauncher();
        launcher.setSparkHome("/usr/local/spark");
        launcher.setAppResource(PropertyUtil.getProperty("recommend.jar"));
    }

    /**
     * 建立图书推荐模型Spark任务，并监控其运行状态
     *
     * @return 模型任务名称
     */
    public String buildRecommendModel(String address, String rank, String lambda, String iterator) {
        String appId = "BuildRecommendModel";
        launcher.setMainClass(PropertyUtil.getProperty("model.main.class"));
        launcher.setMaster(PropertyUtil.getProperty("yarn.cluster"));
        launcher.addAppArgs(PropertyUtil.getProperty("fs.defaultFS") + address, rank, lambda, iterator, PropertyUtil.getProperty("model.save.dir"));
        SparkAppHandle sparkAppHandle = null;
        try {
            sparkAppHandle = launcher.startApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //开启建立模型线程监控
        new Thread(new MonitorThread(sparkAppHandle, appId)).start();
        return appId;
    }

    /**
     * 得到图书推荐结果Spark任务
     */
    public List<String> getRecommendResult(String userId) {
        HadoopUtil.delete(PropertyUtil.getProperty("result.save.dir"));
        if (HadoopUtil.isExist(PropertyUtil.getProperty("model.save.dir"))) {
            launcher.setMainClass(PropertyUtil.getProperty("recommend.main.class"));
            launcher.setMaster(PropertyUtil.getProperty("spark.master"));
            launcher.addAppArgs(PropertyUtil.getProperty("book.ratings.dir"), userId, PropertyUtil.getProperty("model.save.dir"), PropertyUtil.getProperty("book.base.dir"), PropertyUtil.getProperty("result.save.dir"));
            SparkAppHandle sparkAppHandle = null;
            try {
                sparkAppHandle = launcher.startApplication();
                CountDownLatch countDownLatch = new CountDownLatch(1);
                sparkAppHandle.addListener(new SparkAppHandle.Listener() {
                    @Override
                    public void stateChanged(SparkAppHandle handle) {
                        if (handle.getState().isFinal()) {
                            countDownLatch.countDown();
                        }
                    }

                    @Override
                    public void infoChanged(SparkAppHandle handle) {
                    }
                });
                countDownLatch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(sparkAppHandle.getAppId() + ": {} " + sparkAppHandle.getState());
            return HadoopUtil.read(PropertyUtil.getProperty("result.save.dir") + HadoopUtil.PREFIX);
        } else {
            return Lists.newArrayList();
        }
    }
}
