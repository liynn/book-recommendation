package com.wy.common;

import com.wy.BuildRecommendModel$;
import com.wy.util.HadoopUtil;
import com.wy.util.PropertyUtil;

/**
 * Created by wy on 2017/3/29.
 */
public class SparkTask {

    public static void main(String[] args){
        HadoopUtil.getConf();
        String[] t = new String[]{
                PropertyUtil.getProperty("fs.defaultFS")+"/book/ratings.dat",
                "10",
                "0.1",
                "20",
                PropertyUtil.getProperty("fs.defaultFS")+"/model"
        };
        BuildRecommendModel$.MODULE$.build(t);
    }
}
