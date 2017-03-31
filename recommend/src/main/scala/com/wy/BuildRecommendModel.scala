package com.wy

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by wy on 2017/3/14.
  */
//建立推荐模型
object BuildRecommendModel {

  def main(args: Array[String]) {
    if (args.length < 5) {
      System.err.println("参数个数错误:{}" + args.length)
    }
    val conf = new SparkConf().setMaster("yarn").setAppName("BuildRecommendModel")

    val sc = new SparkContext(conf)
    sc.addFile("")

    //获取图书评分数据
    val bookRatingData = args(0)
    val bookRatingsList = sc.textFile(bookRatingData).map { lines =>
      val fields = lines.split("::")
      //用户编号,图书编号,评分,0～9的数值用于做数据分类
      (fields(0).toInt, fields(1).toInt, fields(2).toDouble, fields(3).toLong % 10)
    }

    //将图书评分数据进行分类,其中键为0～9的数字,值为Rating类型
    val bookRatings = bookRatingsList.map(x =>
      (x._4, Rating(x._1.toInt, x._2.toInt, x._3.toDouble)))

    //设置分区数
    val numPartitions = 2

    //获取图书评分训练数据集(图书评分总数据等于图书评分数据加上个人评分数据),并将80%的评分总数据作为训练数据集
    val trainBookRatings = bookRatings.filter(_._1 < 8)
      .values
      .repartition(numPartitions)
      .cache()

    //隐藏因子数
    val rank = args(1).toInt
    //正则化参数
    val lambda = args(2).toDouble
    //迭代次数
    val iterator = args(3).toInt
    //获取一个训练模型
    val model = ALS.train(trainBookRatings, rank, iterator, lambda)
    //模型保存路径
    val modelDir = args(4)
    val hadoopConf = sc.hadoopConfiguration
    hadoopConf.set("fs.defaultFS", "hdfs://master:9000/")
    val fs = FileSystem.get(hadoopConf)
    val path = new Path(modelDir)
    if(fs.exists(path)){
      if(fs.isDirectory(path)){
        fs.delete(path,true)
      }else{
        fs.delete(path,false)
      }
    }
    fs.close()
    model.save(sc,modelDir)
    sc.stop()
  }
}