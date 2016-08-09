package shy.sparkproject.spark

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import shy.sparkproject.conf.ConfigurationManager
import shy.sparkproject.dao.ITaskDao
import shy.sparkproject.dao.factory.DaoFactory
import shy.sparkproject.domain.Task
import shy.sparkproject.utils.{ParamUtils, StringUtils}

import scala.collection.mutable.ArrayBuffer

/**
  * 用户访问session分析
  * Spark作业接收用户创建的任务，J2EE品台接收到用户创建的任务后，将记录插入task表中，
  * 参数以JSON格式封装在task_param字段中，接着J2EE调用Spark-submit shell脚本启动作业
  * 参数task_id，task_param会传递进spark main中
  * Created by AnonYmous_shY on 2016/8/8.
  */
object UserSessionAnalysis {

  def main(args: Array[String]) {

    val cm: ConfigurationManager = new ConfigurationManager
    val conf = new SparkConf()
      .setAppName(cm.getProperty("spark-app.SESSION_AppName"))
      .setMaster(cm.getProperty("spark-ctx.master"))
    val sc = new SparkContext(conf)
    val sqlContext: SQLContext = new SQLContext(sc)
    //拿到指定行为参数
    val taskDao: ITaskDao = DaoFactory.getTaskDao
    val task: Task = taskDao.findById(ParamUtils.getTaskIdFromArgs(args, ""))
    val task_Param: JSONObject = JSON.parseObject(task.getTask_Param)

    /**
      * 按照session粒度进行聚合,从user_visit_action表中,查询出指定范围的行为数据
      * 1,时间范围过滤：起始时间-结束时间
      * 2,性别
      * 3,年龄范围
      * 4,城市(多选)
      * 5,搜索词：多个搜索词
      * 6,点击品类：多个品类
      */

    //1.从用户行为数据表(hive table)中获取指定时间范围的行为数据
    val start_date = ParamUtils.getParam(task_Param, cm.getProperty("PARAM_START_DATE"))
    val end_date = ParamUtils.getParam(task_Param, cm.getProperty("PARAM_END_DATE"))
    val user_action_sql = "select * from user_visit_action where " +
      "date >= '" + start_date + "' and " +
      "date <= '" + end_date + "'"
    val actionDF: DataFrame = sqlContext.sql(user_action_sql)
    val actionRDDByDateRange: RDD[Row] = actionDF.rdd

    //2.对行为数据按照session粒度聚合
    val sessionId_acctionRdd: RDD[(String, Row)] = actionRDDByDateRange
      .map(row => (row.getAs[String]("session_id"), row))
    val sessionId_acctionsRdd: RDD[(String, Iterable[Row])] = sessionId_acctionRdd.groupByKey

    //3.对聚合好数据将关键行为指标提出
    val userId_partAggrInfoRdd: RDD[(Long, JSONObject)] = sessionId_acctionsRdd.map(f = x => {
      val session_id: String = x._1
      val iterator: Iterator[Row] = x._2.iterator

      val search_keyword_buffer = new ArrayBuffer[String]
      val click_category_id_buffer = new ArrayBuffer[Long]
      val click_product_id_buffer = new ArrayBuffer[Long]
      var user_id: Long = 0
      while (iterator.hasNext) {
        val row: Row = iterator.next()
        user_id = row.getAs[Long]("user_id")
        val search_keyword: String = row.getAs[String]("search_keyword")
        val click_category_id: Long = row.getAs[Long]("click_category_id")
        val click_product_id: Long = row.getAs[Long]("click_product_id")

        if (StringUtils.isNotEmpty(search_keyword))
          if (search_keyword_buffer.contains(search_keyword))
            search_keyword_buffer += search_keyword

        if (click_category_id != null)
          if (click_category_id_buffer.contains(click_category_id))
            click_category_id_buffer += click_category_id

        if (click_product_id != null)
          if (click_product_id_buffer.contains(click_product_id))
            click_product_id_buffer += click_product_id
      }

      val search_keywords: String = search_keyword_buffer.mkString(",").toString
      val click_category_ids: String = click_category_id_buffer.mkString(",").toString
      val click_product_ids: String = click_product_id_buffer.mkString(",").toString
      //使用JSON存储聚合后的拼接行为数据
      val aggrInfo: JSONObject = new JSONObject
      aggrInfo.put("session_id", session_id)
      aggrInfo.put("search_keywords", search_keywords)
      aggrInfo.put("click_category_ids", click_category_ids)
      aggrInfo.put("click_product_ids", click_product_ids)
      (user_id, aggrInfo)
    })

    //4.与用户信息对接
    val user_info_sql = "select * from user_info"
    val user_infoDF: DataFrame = sqlContext.sql(user_info_sql)
    val user_infoRdd: RDD[Row] = user_infoDF.rdd
    val userId_info: RDD[(Long, Row)] = user_infoRdd.map(row => (row.getAs[Long]("user_id"), row))
    val userId_fullInfo: RDD[(Long, (JSONObject, Row))] = userId_partAggrInfoRdd.join(userId_info)

    //5.拼接最终查询待过滤信息
    val sessionId_fullAggrInfoRDD: RDD[(Any, JSONObject)] = userId_fullInfo.map(x => {
      val aggrInfo: JSONObject = x._2._1
      val row: Row = x._2._2
      val session_id: Any = aggrInfo.get("session_id")
      val age: Int = row.getAs[Int]("age")
      val professional: String = row.getAs[String]("professional")
      val city: String = row.getAs[String]("city")
      val sex: String = row.getAs[String]("sex")

      aggrInfo.put("age", age)
      aggrInfo.put("professional", professional)
      aggrInfo.put("city", city)
      aggrInfo.put("sex", sex)

      (session_id, aggrInfo)
    })

  }
}
