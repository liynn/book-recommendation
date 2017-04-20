package com.wy.common;

import com.wy.util.ApplicationUtil;
import com.wy.util.PropertyUtil;

import org.apache.spark.launcher.SparkAppHandle;

/**
 * Created by wy on 2017/4/20.
 */
public class MonitorThread implements Runnable {
    private SparkAppHandle handle;
    private String appId;

    public MonitorThread(SparkAppHandle sparkAppHandle, String appId) {
        this.handle = sparkAppHandle;
        this.appId = appId;
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (SparkAppHandle.State.UNKNOWN == handle.getState() || SparkAppHandle.State.CONNECTED == handle.getState()) {
                ApplicationUtil.updateApplicationStatus(appId, count + "%");
            }
            if (SparkAppHandle.State.SUBMITTED == handle.getState()) {
                System.out.println(handle.getAppId() + ": {} " + handle.getState());
                if (count < Integer.parseInt(PropertyUtil.getProperty("model.submitted.progress"))) {
                    count++;
                    ApplicationUtil.updateApplicationStatus(appId, count + "%");
                } else {
                    ApplicationUtil.updateApplicationStatus(appId, PropertyUtil.getProperty("model.submitted.progress") + "%");
                }
            }
            if (SparkAppHandle.State.RUNNING == handle.getState()) {
                System.out.println(handle.getAppId() + ": {} " + handle.getState());
                if (count < Integer.parseInt(PropertyUtil.getProperty("model.running.progress"))) {
                    count++;
                    ApplicationUtil.updateApplicationStatus(appId, count + "%");
                } else {
                    ApplicationUtil.updateApplicationStatus(appId, PropertyUtil.getProperty("model.running.progress") + "%");
                }
            }
            if (SparkAppHandle.State.FINISHED == handle.getState()) {
                System.out.println(handle.getAppId() + ": {} " + handle.getState());
                ApplicationUtil.updateApplicationStatus(appId, handle.getState().name());
                return;
            }
        }
    }
}

