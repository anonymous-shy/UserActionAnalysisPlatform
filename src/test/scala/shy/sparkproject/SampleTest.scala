package shy.sparkproject

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by AnonYmous_shY on 2016/8/23.
  */
object SampleTest extends App {

  val conf = new SparkConf().setMaster("local").setAppName("sample test")
  val sc = new SparkContext(conf)
  private val rdd1: RDD[Int] = sc.parallelize(1 to 10)
  println(rdd1.sample(true, 0.4).collect())
  println(rdd1.takeSample(true, 4))
}
