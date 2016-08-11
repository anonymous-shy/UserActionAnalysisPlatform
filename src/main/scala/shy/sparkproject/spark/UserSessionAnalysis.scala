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
    val taskParam: JSONObject = JSON.parseObject(task.getTask_Param)

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
    val start_date = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE)
    val end_date = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE)
    val user_action_sql = "select * from user_visit_action where " +
      "date >= '" + start_date + "' and " +
      "date <= '" + end_date + "'"
    val actionDF: DataFrame = sqlContext.sql(user_action_sql)
    val actionRDDByDateRange: RDD[Row] = actionDF.rdd

    //2.对行为数据按照session粒度聚合
    val sessionId_acctionRdd: RDD[(String, Row)] = actionRDDByDateRange
      .map(row => (row.getAs[String](Constants.FIELD_SESSION_ID), row))
    val sessionId_acctionsRdd: RDD[(String, Iterable[Row])] = sessionId_acctionRdd.groupByKey

    //3.对聚合好数据将关键行为指标提出
    val userId_partAggrInfoRdd: RDD[(Long, Map[String, String])] = sessionId_acctionsRdd.map(x => {
      val sessionId: String = x._1
      val iterator: Iterator[Row] = x._2.iterator

      val search_keyword_buffer = new ArrayBuffer[String]
      val click_category_id_buffer = new ArrayBuffer[Long]
      val click_product_id_buffer = new ArrayBuffer[Long]
      var userId: Long = 0
      while (iterator.hasNext) {
        val row: Row = iterator.next()
        userId = row.getAs[Long]("user_id")
        val searchKeyword: String = row.getAs[String]("search_keyword")
        val clickCategoryId: Long = row.getAs[Long]("click_category_id")
        val clickProductId: Long = row.getAs[Long]("click_product_id")

        if (StringUtils.isNotEmpty(searchKeyword))
          if (search_keyword_buffer.contains(searchKeyword))
            search_keyword_buffer += searchKeyword

        if (clickCategoryId != null)
          if (click_category_id_buffer.contains(clickCategoryId))
            click_category_id_buffer += clickCategoryId

        if (clickProductId != null)
          if (click_product_id_buffer.contains(clickProductId))
            click_product_id_buffer += clickProductId
      }

      val searchKeywords: String = search_keyword_buffer.mkString(",").toString
      val clickCategoryIds: String = click_category_id_buffer.mkString(",").toString
      val clickProductIds: String = click_product_id_buffer.mkString(",").toString
      //指定Map存放session信息
      var aggrInfoMap = Map[String, String]()

      aggrInfoMap += (Constants.FIELD_SESSION_ID -> sessionId)
      aggrInfoMap += (Constants.FIELD_SEARCH_KEYWORDS -> searchKeywords)
      aggrInfoMap += (Constants.FIELD_CLICK_CATEGORY_IDS -> clickCategoryIds)
      aggrInfoMap += (Constants.FIELD_CLICK_PRODUCT_IDS -> clickProductIds)

      (userId, aggrInfoMap)
    })

    //4.与用户信息对接
    val user_info_sql = "select * from user_info"
    val user_infoDF: DataFrame = sqlContext.sql(user_info_sql)
    val user_infoRdd: RDD[Row] = user_infoDF.rdd
    val userId_info: RDD[(Long, Row)] = user_infoRdd.map(row => (row.getAs[Long]("user_id"), row))
    val userId_fullInfo: RDD[(Long, (Map[String, String], Row))] = userId_partAggrInfoRdd.join(userId_info)

    //5.拼接最终查询待过滤信息
    val sessionId_fullAggrInfoRDD: RDD[(String, Map[String, String])] = userId_fullInfo.map(x => {
      var aggrInfoMap: Map[String, String] = x._2._1
      val row: Row = x._2._2
      val session_id: Option[String] = aggrInfoMap.get(Constants.FIELD_SESSION_ID)
      val age: String = row.getAs[Int]("age").toString //Int类型 使用时不要忘记
      val professional: String = row.getAs[String]("professional")
      val city: String = row.getAs[String]("city")
      val sex: String = row.getAs[String]("sex")

      aggrInfoMap += (Constants.FIELD_AGE -> age)
      aggrInfoMap += (Constants.FIELD_PROFESSIONAL -> professional)
      aggrInfoMap += (Constants.FIELD_CITY -> city)
      aggrInfoMap += (Constants.FIELD_SEX -> sex)

      (session_id.toString, aggrInfoMap)
    })

    //6.过滤已聚合好的session数据

    val paramMap =
      Map(Constants.PARAM_START_AGE -> ParamUtils.getParam(taskParam, Constants.PARAM_START_AGE),
        Constants.PARAM_END_AGE -> ParamUtils.getParam(taskParam, Constants.PARAM_END_AGE),
        Constants.PARAM_PROFESSIONALS -> ParamUtils.getParam(taskParam, Constants.PARAM_PROFESSIONALS),
        Constants.PARAM_CITIES -> ParamUtils.getParam(taskParam, Constants.PARAM_CITIES),
        Constants.PARAM_SEX -> ParamUtils.getParam(taskParam, Constants.PARAM_SEX),
        Constants.PARAM_SEARCH_KEYWORDS -> ParamUtils.getParam(taskParam, Constants.PARAM_SEARCH_KEYWORDS),
        Constants.PARAM_CATEGORY_IDS -> ParamUtils.getParam(taskParam, Constants.PARAM_CATEGORY_IDS),
        Constants.PARAM_PRODUCT_IDS -> ParamUtils.getParam(taskParam, Constants.PARAM_PRODUCT_IDS))

    //过滤session数据
    val filterSessionId_fullAggrInfoRDD: RDD[(String, Map[String, String])] = sessionId_fullAggrInfoRDD.filter(x => {
      val aggrInfoMap: Map[String, String] = x._2
      //按年龄范围过滤
      if (!ValidUtils.between(aggrInfoMap, Constants.FIELD_AGE, paramMap, Constants.PARAM_START_AGE, Constants.PARAM_END_AGE))
        false
      //按职业就行过滤
      else if (!ValidUtils.in(aggrInfoMap, Constants.FIELD_PROFESSIONAL, paramMap, Constants.PARAM_PROFESSIONALS))
        false
      //按城市过滤
      else if (!ValidUtils.in(aggrInfoMap, Constants.FIELD_CITY, paramMap, Constants.PARAM_CITIES))
        false
      //按性别过滤
      else if (!ValidUtils.equal(aggrInfoMap, Constants.FIELD_SEX, paramMap, Constants.PARAM_SEX))
        false
      //按搜索词过滤
      else if (!ValidUtils.in(aggrInfoMap, Constants.FIELD_SEARCH_KEYWORDS, paramMap, Constants.PARAM_SEARCH_KEYWORDS))
        false
      //按点击品类过滤
      else if (!ValidUtils.in(aggrInfoMap, Constants.FIELD_CLICK_CATEGORY_IDS, paramMap, Constants.PARAM_CATEGORY_IDS))
        false
      //按点击商品过滤
      else if (!ValidUtils.in(aggrInfoMap, Constants.FIELD_CLICK_PRODUCT_IDS, paramMap, Constants.PARAM_PRODUCT_IDS))
        false
      else true
    })

  }
}
