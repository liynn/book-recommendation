package com.wy

import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by wy on 2017/3/14.
  */
//基于模型的协同过滤
object BookRecommendALS {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local[*]").setAppName("BookRecommendALS")
    val sc = new SparkContext(conf)

    //获取图书评分数据
    val bookRatingsList = sc.textFile("/Users/wy/Documents/毕业设计/data/ratings.dat").map { lines =>
      val fields = lines.split("::")
      //用户编号,图书编号,评分,0～9的数值用于做数据分类
      (fields(0).toInt, fields(1).toInt, fields(2).toDouble, fields(3).toLong % 10)
    }
    //打印前10条评分数据
    println("前10条的图书评分信息:")
    bookRatingsList.take(10).foreach(println)

    //将图书评分数据进行分类,其中键为0～9的数字,值为Rating类型
    val bookRatings = bookRatingsList.map(x =>
      (x._4, Rating(x._1.toInt, x._2.toInt, x._3.toDouble)))

    //打印基本信息
    println("\n评分总记录数: "+ bookRatings.count()
      + " 用户总数: "+ bookRatings.map(_._2.user).distinct().count()
      + " 图书总数: " + bookRatings.map(_._2.product).distinct().count())

    //获取个人评分信息
    val personalBookRatings = sc.textFile("/Users/wy/Documents/毕业设计/data/personalRatings.dat").map { lines =>
      val fields = lines.split("::")
      Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble)
    }

    //设置分区数
    val numPartitions = 3

    //获取图书评分训练数据集(图书评分总数据等于图书评分数据加上个人评分数据),并将80%的评分总数据作为训练数据集
    val trainBookRatings = bookRatings.filter(_._1 < 8)
      .values
      .union(personalBookRatings)
      .repartition(numPartitions).cache()

    //获取图书评分验证数据集(将20%的图书评分数据作为验证数据集)
    val validateBookRatings = bookRatings.filter(x => x._1 >= 6 && x._1 < 8)
      .values
      .repartition(numPartitions)
      .cache()

    //获取图书评分测试数据集(另取20%的图书评分数据作为测试数据集)
    val testBookRatings = bookRatings.filter(_._1 >= 8)
      .values
      .cache()

    println("\n训练数据集总数: " + trainBookRatings.count()
      + " 验证数据集总数: " + validateBookRatings.count()
      + " 测试数据集总数: " + testBookRatings.count())

    //开始构造模型,并根据方差来选择最佳模型

    //隐藏因子数
    val ranks = List(8, 22)
    //正则化参数
    val lambdas = List(0.1, 10.0)
    //迭代次数
    val iterators = List(5, 7)

    //定义最好的训练模型
    var bestModel: MatrixFactorizationModel = null
    var bestValidateRnse = Double.MaxValue
    var bestRank = 0
    var bestLambda = -1.0
    var bestIterator = -1

    //三层嵌套循环,产生8个模型，计算模型的方差，方差值最小的模型记为最佳模型
    for (rank <- ranks; lambda <- lambdas; iterator <- iterators) {
      //获取一个训练模型
      val model = ALS.train(trainBookRatings, rank, iterator, lambda)
      //计算模型方差
      val temp = variance(model, validateBookRatings, validateBookRatings.count())
      //打印模型信息
      println("模型方差:" + temp
          + " 隐藏因子数为:" + rank
          + " 正则化参数为:" + lambda
          + " 迭代次数为:" + iterator)

      //判断最佳模型
      if (temp < bestValidateRnse) {
         bestModel = model
         bestValidateRnse = temp
         bestRank = rank
         bestLambda = lambda
         bestIterator = iterator
      }
    }

    //将最佳模型在测试数据集上进行测试
    val testVariance = variance(bestModel, testBookRatings, testBookRatings.count())
    println("\n最佳模型信息:")
    println(" 隐藏因子数为:" + bestRank
      + " 正则化参数为: " + bestLambda
      + " 迭代次数为: " + bestIterator
      + " 在测试数据集上计算的方差为: "+testVariance)

    //训练数据加上验证数据的分数的平均分
    val meanRating = trainBookRatings.union(validateBookRatings).map(_.rating).mean()
    //测试数据的计算出得方差
    val baseVariance = math.sqrt(testBookRatings.map(x => (meanRating - x.rating) * (meanRating - x.rating)).mean())
    //最佳模型计算出的方差与测试数据基本方法提升比率
    val improvement = (baseVariance - testVariance) / baseVariance * 100
    println("\n最佳模型计算的方差提升比率为:" + "%2.2f".format(improvement) + "%")

    //图书列表总数据，元组格式
    val movieList = sc.textFile("/Users/wy/Documents/毕业设计/data/books.dat").map { lines =>
      val fields = lines.split("::")
      (fields(0).toInt, fields(1), fields(2))
    }

    //获取图书名称信息数据
    val booksName = movieList.map(x =>
      (x._1, x._2)).collect().toMap

    movieList.take(10).foreach(println)

    //获取图书类型信息数据
    val booksType = movieList.map(x =>
      (x._1, x._3)).collect().toMap

    var i = 1
    println("\n根据个人阅读图书记录,你可能喜欢的书籍有:")
    //获取已阅读的图书编号
    val personalRatingBookIds = personalBookRatings.map(_.product).collect().toSet
    //在图书总数据中过滤掉我已经阅读图书数据,得到需要对其评分的图书数据
    val needRatingBook = sc.parallelize(booksName.keys.filter(personalRatingBookIds.contains(_)).toSeq)
    //预测对图书的评分，并按评分从高到低进行排序，将评分最高的10条记录作为可能喜欢的图书
    bestModel.predict(needRatingBook.map((0, _))).collect().sortBy(-_.rating).take(10).foreach { r =>
      println("%2d".format(i) + "----------> : \n图书名称 --> "
        + booksName(r.product) + " \n图书类型 --> "
        + booksType(r.product))
      i += 1
    }
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
