package com.wy

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.mllib.recommendation.{MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by wy on 2017/3/14.
  */
//基于模型的图书推荐
object BookRecommend {

  def main(args: Array[String]) {
    if (args.length < 4) {
      System.err.println("参数个数错误:{}" + args.length)
    }
    val conf = new SparkConf().setMaster("yarn").setAppName("BookRecommend")
    val sc = new SparkContext(conf)

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

    val userId = args(1).toInt
    val testBookRatings = bookRatings.filter(_._1 >= 8)
      .values
      .cache()

    //获取构建模型
    val modelDir = args(2)
    val model = MatrixFactorizationModel.load(sc,modelDir)
    //将最佳模型在测试数据集上进行测试
    val testVariance = variance(model, testBookRatings, testBookRatings.count())
    println("在测试数据集上计算的方差为:" + testVariance)

    //图书列表总数据，元组格式
    val bookData = args(3)
    val bookList = sc.textFile(bookData).map { lines =>
      val fields = lines.split("::")
      (fields(0).toInt, fields(1), fields(2))
    }
    println("图书总数量:"+bookList.count())
    //获取图书名称信息数据
    val booksName = bookList.map(x => (x._1, x._2)).collect().toMap

    //需要推荐用户的已阅读图书编号
    val personalRatingBookIds = bookRatingsList.filter(_._1 == userId).map(_._2).collect().toSet
    println("已阅读并打分图书数量:"+personalRatingBookIds.size)
    //在图书总数据中过滤掉已经阅读图书数据,得到需要对其评分的图书数据
    val needRatingBook = sc.parallelize(booksName.keys.filter(!personalRatingBookIds.contains(_)).toSeq)
    println("需要打分的图书数量:"+needRatingBook.count())
    val resultDir = args(4)
    val hadoopConf = sc.hadoopConfiguration
    hadoopConf.set("fs.defaultFS", "hdfs://master:9000/")
    val fs = FileSystem.get(hadoopConf)
    val path = new Path(resultDir)
    if(fs.exists(path)){
      if(fs.isDirectory(path)){
        fs.delete(path,true)
      }else{
        fs.delete(path,false)
      }
    }
    fs.close()
    //将结果按评分从高到低排序保存在HDFS中
    model.predict(needRatingBook.map((userId, _))).sortBy(-_.rating).map(x=>(x.product,x.rating)).saveAsTextFile(resultDir)
    sc.stop()
  }

  //计算方差函数
  def variance(model: MatrixFactorizationModel, predictionData: RDD[Rating], n: Long): Double = {
    //根据参数model，来对验证数据集进行预测
    val prediction = model.predict(predictionData.map(x => (x.user, x.product)))
    //将预测结果和验证数据集join之后计算评分的方差并返回
    val predictionAndOldRatings = prediction.map(x => ((x.user, x.product), x.rating))
      .join(predictionData.map(x => ((x.user, x.product), x.rating))).values
    math.sqrt(predictionAndOldRatings.map(x => (x._1 - x._2) * (x._1 - x._2)).reduce(_ - _) / n)
  }
}
