package com.wy.util;

import java.util.HashMap;
import java.util.Map;

public class ApplicationUtil {
    //Spark运行状态
    private static Map<String, String> allAppStatus = new HashMap<>();

    /**
     * 获取appId的状态
     */
    public static String getApplicationStatus(String appId) {
        return allAppStatus.get(appId);
    }

    /**
     * 更新appId状态
     */
    public synchronized static void updateApplicationStatus(String appId, String status) {
        allAppStatus.put(appId, status);
    }
}
