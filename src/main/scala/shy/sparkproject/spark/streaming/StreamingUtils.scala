package shy.sparkproject.spark.streaming

import kafka.api.{OffsetRequest, PartitionOffsetRequestInfo}
import kafka.common.{BrokerNotAvailableException, TopicAndPartition}
import kafka.consumer.SimpleConsumer
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import kafka.utils.{Json, ZKGroupTopicDirs, ZKStringSerializer, ZkUtils}
import org.I0Itec.zkclient.ZkClient
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{KafkaUtils, OffsetRange}
import org.slf4j.{Logger, LoggerFactory}


/**
  * Created by Shy on 2017/12/28
  */

object StreamingUtils {
  lazy private val log: Logger = LoggerFactory.getLogger(getClass)

  def CreateKafkaDirectStream(ssc: StreamingContext,
                              kafkaParams: Map[String, String],
                              zkServers: String,
                              topics: Array[String]): InputDStream[(String, String)] = {
    val kafkaGroup: String = kafkaParams("group.id")
    //创建zkClient注意最后一个参数最好是ZKStringSerializer类型的，不然写进去zk里面的偏移量是乱码
    val zkClient = new ZkClient(zkServers, 30000, 30000, ZKStringSerializer)
    val partitions4Topics = ZkUtils.getPartitionsForTopics(zkClient, topics)
    var fromOffsets: Map[TopicAndPartition, Long] = Map()
    partitions4Topics.foreach(partitions4Topic => {
      val topic = partitions4Topic._1
      val partitions = partitions4Topic._2
      /**
        * kafka.utils.ZkUtils.ZKGroupTopicDirs
        * val ConsumersPath = "/consumers"
        * val BrokerIdsPath = "/brokers/ids"
        * val BrokerTopicsPath = "/brokers/topics"
        * val TopicConfigPath = "/config/topics"
        * val TopicConfigChangesPath = "/config/changes"
        * val ControllerPath = "/controller"
        * val ControllerEpochPath = "/controller_epoch"
        * val ReassignPartitionsPath = "/admin/reassign_partitions"
        * val DeleteTopicsPath = "/admin/delete_topics"
        * val PreferredReplicaLeaderElectionPath = "/admin/preferred_replica_election"
        * *
        * class ZKGroupDirs(val group: String) {
        * def consumerDir = ZkUtils.ConsumersPath
        * def consumerGroupDir = consumerDir + "/" + group
        * def consumerRegistryDir = consumerGroupDir + "/ids"
        * }
        *
        * class ZKGroupTopicDirs(group: String, topic: String) extends ZKGroupDirs(group) {
        * def consumerOffsetDir = consumerGroupDir + "/offsets/" + topic
        * def consumerOwnerDir = consumerGroupDir + "/owners/" + topic
        * }
        */
      val topicDirs = new ZKGroupTopicDirs(kafkaGroup, topic)
      partitions.foreach(partition => {
        //获取 zookeeper 中的路径，这里会变成 /consumers/$group/offsets/$topic  <=> /consumers/group-shy/offsets/topic1
        val zkPath = s"${topicDirs.consumerOffsetDir}/$partition"
        /**
          * make sure a persistent path exists in ZK. Create the path if not exist.
          * 若为第一次则创建zkPath
          *
          * def makeSurePersistentPathExists(client: ZkClient, path: String) {
          * if (!client.exists(path))
          *     client.createPersistent(path, true) // won't throw NoNodeException or NodeExistsException
          * }
          */
        ZkUtils.makeSurePersistentPathExists(zkClient, zkPath)
        val untilOffset = zkClient.readData[String](zkPath)
        val tp = TopicAndPartition(topic, partition)
        // 获取 kafkaParams 中 auto.offset.reset,若不存在则默认从最新消费.
        val reset = kafkaParams.getOrElse("auto.offset.reset", OffsetRequest.LargestTimeString).trim.toLowerCase
        val offset: Long = try {
          if (untilOffset == null || untilOffset.trim.equals("")) {
            getResetOffset(zkClient, tp, reset)
          } else
            untilOffset.toLong
        } catch {
          case e: Exception =>
            log.error(e.getMessage)
            getResetOffset(zkClient, tp, reset)
        }
        fromOffsets += (tp -> offset)
        log.info(s"@@@@@@ topic[ $topic ] partition[ $partition ] offset[ $offset ] @@@@@@")
      })
    })
    //这个会将 kafka 的消息进行 transform，最终 kafka 的数据都会变成 (topic_name, message) 这样的 tuple
    val messageHandler = (mmd: MessageAndMetadata[String, String]) => (mmd.topic, mmd.message())
    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](ssc, kafkaParams, fromOffsets, messageHandler)
    kafkaStream
  }

  def saveOffsets(offsetRanges: Array[OffsetRange], kafkaParams: Map[String, String], zkServers: String): Unit = {
    val kafkaGroup: String = kafkaParams("group.id")
    val zkClient = new ZkClient(zkServers, 30000, 30000, ZKStringSerializer)
    offsetRanges.foreach(o => {
      val topicDirs = new ZKGroupTopicDirs(kafkaGroup, o.topic)
      val zkPath = s"${topicDirs.consumerOffsetDir}/${o.partition}"
      ZkUtils.updatePersistentPath(zkClient, zkPath, o.untilOffset.toString)
      log.info(s"@@@@@@ ${o.toString()} @@@@@@")
      // log.info(s"Offset update: set offset of ${o.topic}/${o.partition} as ${o.untilOffset.toString}")
    })
  }

  private def getResetOffset(zkClient: ZkClient, tp: TopicAndPartition, reset: String): Long = {
    /**
      * kafka.api.OffsetRequest
      * case class PartitionOffsetRequestInfo(time: Long, maxNumOffsets: Int)
      * val SmallestTimeString = "smallest"
      * val LargestTimeString = "largest"
      * val LatestTime = -1L
      * val EarliestTime = -2L
      * 第二个参数maxNumOffsets : 具体意思不明!
      * 但是测试后发现传入 1 时返回 offset.reset 对应的offset,传入 2 返回一个包含最大和最小offset的元组
      */
    // val request = OffsetRequest(Map(tp -> PartitionOffsetRequestInfo(OffsetRequest.EarliestTime, 1)))
    val request = reset match {
      case OffsetRequest.SmallestTimeString => OffsetRequest(Map(tp -> PartitionOffsetRequestInfo(OffsetRequest.EarliestTime, 1)))
      case OffsetRequest.LargestTimeString => OffsetRequest(Map(tp -> PartitionOffsetRequestInfo(OffsetRequest.LatestTime, 1)))
      case _ => OffsetRequest(Map(tp -> PartitionOffsetRequestInfo(OffsetRequest.LatestTime, 1)))
    }

    ZkUtils.getLeaderForPartition(zkClient, tp.topic, tp.partition) match {
      case Some(brokerId) =>
        ZkUtils.readDataMaybeNull(zkClient, ZkUtils.BrokerIdsPath + "/" + brokerId)._1 match {
          case Some(brokerInfoString) =>
            Json.parseFull(brokerInfoString) match {
              case Some(m) =>
                val brokerInfo = m.asInstanceOf[Map[String, Any]]
                val host = brokerInfo("host").asInstanceOf[String]
                val port = brokerInfo("port").asInstanceOf[Int]
                new SimpleConsumer(host, port, 10000, 100000, "getResetOffset")
                  .getOffsetsBefore(request)
                  .partitionErrorAndOffsets(tp)
                  .offsets
                  .head
              case None =>
                throw new BrokerNotAvailableException("Broker id %d does not exist".format(brokerId))
            }
          case None =>
            throw new BrokerNotAvailableException("Broker id %d does not exist".format(brokerId))
        }
      case None =>
        throw new Exception("No broker for partition %s - %s".format(tp.topic, tp.partition))
    }
  }
}
