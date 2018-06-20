package shy.sparkproject.spark.streaming

import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Shy on 2018/6/19
  * 广告流量实时统计
  */

object AdClickRTStatistics {

  def main(args: Array[String]): Unit = {
    val resConf = ConfigFactory.load()
    val zkQuorums = resConf.getString("ZKNodes")
    val confTopics: String = resConf.getString("kafka.topics")
    val topicsAB = ArrayBuffer[String]()
    confTopics.split(",").foreach(s => topicsAB.append(s))
    val topics: Array[String] = topicsAB.toArray
    val group = resConf.getString("kafka.group")
    val brokerList = resConf.getString("kafka.brokers")
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokerList,
      "group.id" -> group,
      "zookeeper.connect" -> zkQuorums
    )

    val sparkConf = new SparkConf()
      .setAppName("Test-tfidf")
      .set("spark.streaming.stopGracefullyOnShutdown", "true") //优雅的关闭
      .set("spark.streaming.backpressure.enabled", "true") //激活削峰功能
      .set("spark.streaming.backpressure.initialRate", "5000") //第一次读取的最大数据值
      .set("spark.streaming.kafka.maxRatePerPartition", "2000") //每个进程每秒最多从kafka读取的数据条数
    val ssc = new StreamingContext(sparkConf, Seconds(resConf.getInt("batchDurationMs")))

    val kafkaStream = StreamingUtils.CreateKafkaDirectStream(ssc, kafkaParams, zkQuorums, topics)
    var offsetRanges = Array[OffsetRange]()
    kafkaStream.transform { rdd =>
      offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      rdd
    }.map(_._2)

    ssc.start()
    ssc.awaitTermination()
  }
}
