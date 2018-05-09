package shy.sparkproject.spark

import java.util.Date

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.commons.lang3.StringUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{Accumulator, SparkConf, SparkContext}
import shy.sparkproject.conf.ConfigurationManager
import shy.sparkproject.dao.ITaskDao
import shy.sparkproject.dao.factory.DaoFactory
import shy.sparkproject.domain.{SessionAggrRate, Task}
import shy.sparkproject.utils.{DateUtils, MyStringUtils, NumberUtils, ParamUtils}

import scala.collection.mutable.{ArrayBuffer, Map => MMap}

/**
  * Created by Shy on 2018/5/9
  */

object NewUserSessionAnalysis {

  def main(args: Array[String]): Unit = {
    val cm: ConfigurationManager = new ConfigurationManager
    val conf = new SparkConf()
      .setAppName(cm.getProperty("spark-app.SESSION_AppName"))
      .setMaster(cm.getProperty("spark-ctx.master"))
    val sc = new SparkContext(conf)
    val sqlContext: SQLContext = new SQLContext(sc)

    val sessionAggrAccumulator: Accumulator[String] =
      sc.accumulator("", "SessionAggrAccumulator")(new SessionAggrAccumulator)

    val taskDao: ITaskDao = DaoFactory.getTaskDao
    val task: Task = taskDao.findById(ParamUtils.getTaskIdFromArgs(args, ""))
    val taskId = task.getTask_Id
    val taskParam: JSONObject = JSON.parseObject(task.getTask_Param)
    val actionRDDByDateRange: RDD[Row] = getActionRddByDateRange(sqlContext, taskParam)
    val sessionFullAggrInfoRDD: RDD[(String, Map[String, String])] = aggrBySession(actionRDDByDateRange, sqlContext)
    val filteredSessionRDD: RDD[(String, Map[String, String])] = filterSession(sessionFullAggrInfoRDD, taskParam, sessionAggrAccumulator)
    filteredSessionRDD.take(10) // 触发action
    sessionStatistics(sessionAggrAccumulator, taskId)
  }

  // 1.从用户行为数据表(hive table)中获取指定时间范围的行为数据
  def getActionRddByDateRange(sqlContext: SQLContext, taskParam: JSONObject): RDD[Row] = {
    val start_date = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE)
    val end_date = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE)
    val user_action_sql =
      s"""
         |SELECT * FROM
         | user_visit_action
         |WHERE date >= $start_date AND date < $end_date
       """.stripMargin
    val dataFrame = sqlContext.sql(user_action_sql)
    dataFrame.rdd
  }

  // 2.对行为数据按照session粒度聚合，添加计算访问时长步长
  def aggrBySession(actionRdd: RDD[Row], sqlContext: SQLContext): RDD[(String, Map[String, String])] = {
    // 对行为数据按照session粒度聚合
    val sessionId_acctionsRdd: RDD[(String, Iterable[Row])] = actionRdd
      .map(row => (row.getAs[String](Constants.FIELD_SESSION_ID), row))
      .groupByKey()
    // 转换rdd 编程 (userId,sessionInfoMap)
    val userId_partAggrInfoRdd: RDD[(Long, Map[String, String])] = sessionId_acctionsRdd.map(x => {
      val sessionId = x._1
      val sessionIterator = x._2.toIterator
      var userId: Long = 0
      val aggrInfoMap = MMap[String, String]()
      val search_keyword_buffer = ArrayBuffer[String]()
      val click_category_id_buffer = ArrayBuffer[Long]()
      val click_product_id_buffer = ArrayBuffer[Long]()

      //在过滤session中提取session访问时长与访问步长
      var startTime: Date = null
      var endTime: Date = null
      var stepLength: Long = 0

      sessionIterator.foreach(row => {
        if (userId != null)
          userId = row.getAs[Long]("user_id")
        val searchKeyword: String = row.getAs[String]("search_keyword")
        val clickCategoryId: Long = row.getAs[Long]("click_category_id")
        val clickProductId: Long = row.getAs[Long]("click_product_id")
        if (StringUtils.isNotEmpty(searchKeyword) && !search_keyword_buffer.contains(searchKeyword))
          search_keyword_buffer += searchKeyword
        if (clickCategoryId != null && click_category_id_buffer.contains(clickCategoryId))
          click_category_id_buffer += clickCategoryId
        if (clickProductId != null && click_product_id_buffer.contains(clickProductId))
          click_product_id_buffer += clickProductId
        //获取action_time
        val actionTime = DateUtils.parseTime(row.getAs[String]("action_time"))
        //第一次情况下,将actionTime 同时赋值给 start end
        if (startTime == null)
          startTime = actionTime
        if (endTime == null)
          endTime = actionTime
        if (actionTime.before(startTime))
          startTime = actionTime
        if (actionTime.after(endTime))
          endTime = actionTime
        //每循环一次步长 +1
        stepLength += 1
      })
      //计算访问时长
      val visitLength: Long = (endTime.getTime - startTime.getTime) / 1000
      aggrInfoMap(Constants.FIELD_SESSION_ID) = sessionId
      aggrInfoMap(Constants.FIELD_SEARCH_KEYWORDS) = search_keyword_buffer.mkString(",")
      aggrInfoMap(Constants.FIELD_CLICK_CATEGORY_IDS) = click_category_id_buffer.mkString(",")
      aggrInfoMap(Constants.FIELD_CLICK_PRODUCT_IDS) = click_product_id_buffer.mkString(",")
      aggrInfoMap(Constants.FIELD_VISIT_LENGTH) = visitLength.toString
      aggrInfoMap(Constants.FIELD_STEP_LENGTH) = stepLength.toString
      (userId, aggrInfoMap.toMap)
    })
    // 提取用户信息
    val userInfoDF = sqlContext.sql(
      """
    SELECT * FROM `user_info`
  """.stripMargin)
    val userId_info: RDD[(Long, Row)] = userInfoDF.rdd.map(row => (row.getAs[Long]("user_id"), row))
    // 与用户信息对接
    val userId_fullInfo: RDD[(Long, (Map[String, String], Row))] = userId_partAggrInfoRdd.join(userId_info)
    // 拼接最终查询待过滤信息 (sessionId,session_user_InfoMap)
    val sessionId_fullAggrInfoRDD: RDD[(String, Map[String, String])] = userId_fullInfo.map(x => {
      var aggrInfoMap: Map[String, String] = x._2._1
      val userRow: Row = x._2._2
      val sessionId: String = aggrInfoMap(Constants.FIELD_SESSION_ID)
      val age: String = userRow.getAs[Int]("age").toString //Int类型 使用时不要忘记
      val professional: String = userRow.getAs[String]("professional")
      val city: String = userRow.getAs[String]("city")
      val sex: String = userRow.getAs[String]("sex")
      aggrInfoMap += (Constants.FIELD_AGE -> age)
      aggrInfoMap += (Constants.FIELD_PROFESSIONAL -> professional)
      aggrInfoMap += (Constants.FIELD_CITY -> city)
      aggrInfoMap += (Constants.FIELD_SEX -> sex)
      (sessionId, aggrInfoMap)
    })
    sessionId_fullAggrInfoRDD
  }

  // 3.过滤已聚合好的session数据，将访问时长步长及session次数添加到spark accumulator中
  def filterSession(sessionFullAggrInfoRDD: RDD[(String, Map[String, String])],
                    taskParam: JSONObject,
                    aggrAccumulator: Accumulator[String]): RDD[(String, Map[String, String])] = {
    // 获取 args 中传入的 task 参数封装成 Map
    val paramMap =
      Map(Constants.PARAM_START_AGE -> ParamUtils.getParam(taskParam, Constants.PARAM_START_AGE),
        Constants.PARAM_END_AGE -> ParamUtils.getParam(taskParam, Constants.PARAM_END_AGE),
        Constants.PARAM_PROFESSIONALS -> ParamUtils.getParam(taskParam, Constants.PARAM_PROFESSIONALS),
        Constants.PARAM_CITIES -> ParamUtils.getParam(taskParam, Constants.PARAM_CITIES),
        Constants.PARAM_SEX -> ParamUtils.getParam(taskParam, Constants.PARAM_SEX),
        Constants.PARAM_SEARCH_KEYWORDS -> ParamUtils.getParam(taskParam, Constants.PARAM_SEARCH_KEYWORDS),
        Constants.PARAM_CATEGORY_IDS -> ParamUtils.getParam(taskParam, Constants.PARAM_CATEGORY_IDS),
        Constants.PARAM_PRODUCT_IDS -> ParamUtils.getParam(taskParam, Constants.PARAM_PRODUCT_IDS))
    sessionFullAggrInfoRDD.filter(x => {
      val aggrInfoMap: Map[String, String] = x._2
      if (ValidUtils.between(aggrInfoMap, Constants.FIELD_AGE, paramMap, Constants.PARAM_START_AGE, Constants.PARAM_END_AGE) //按年龄范围过滤
        && ValidUtils.in(aggrInfoMap, Constants.FIELD_PROFESSIONAL, paramMap, Constants.PARAM_PROFESSIONALS) //按职业就行过滤
        && ValidUtils.in(aggrInfoMap, Constants.FIELD_CITY, paramMap, Constants.PARAM_CITIES) //按城市过滤
        && ValidUtils.equal(aggrInfoMap, Constants.FIELD_SEX, paramMap, Constants.PARAM_SEX) //按性别过滤
        && ValidUtils.in(aggrInfoMap, Constants.FIELD_SEARCH_KEYWORDS, paramMap, Constants.PARAM_SEARCH_KEYWORDS) //按搜索词过滤
        && ValidUtils.in(aggrInfoMap, Constants.FIELD_CLICK_CATEGORY_IDS, paramMap, Constants.PARAM_CATEGORY_IDS) //按点击品类过滤
        && ValidUtils.in(aggrInfoMap, Constants.FIELD_CLICK_PRODUCT_IDS, paramMap, Constants.PARAM_PRODUCT_IDS)) {
        //按点击商品过滤
        //当程序进入if时就是过滤后的session数据，直接在过滤后返回时算出 过滤后session条数 以及 访问时长，步长统计
        aggrAccumulator.add(Constants.SESSION_COUNT)
        calVisitLength(aggrInfoMap(Constants.FIELD_VISIT_LENGTH).toLong)
        calStepLength(aggrInfoMap(Constants.FIELD_STEP_LENGTH).toLong)
        true
      }
      else
        false
    })

    //定义统计时长步长方法
    def calVisitLength(visitLength: Long): Unit = {
      if (visitLength >= 1 && visitLength <= 3) aggrAccumulator.add(Constants.TIME_PERIOD_1s_3s)
      if (visitLength >= 4 && visitLength <= 6) aggrAccumulator.add(Constants.TIME_PERIOD_4s_6s)
      if (visitLength >= 7 && visitLength <= 9) aggrAccumulator.add(Constants.TIME_PERIOD_7s_9s)
      if (visitLength >= 10 && visitLength <= 30) aggrAccumulator.add(Constants.TIME_PERIOD_10s_30s)
      if (visitLength >= 30 && visitLength <= 60) aggrAccumulator.add(Constants.TIME_PERIOD_30s_60s)
      if (visitLength >= 60 && visitLength <= 180) aggrAccumulator.add(Constants.TIME_PERIOD_1m_3m)
      if (visitLength >= 180 && visitLength <= 600) aggrAccumulator.add(Constants.TIME_PERIOD_3m_10m)
      if (visitLength >= 600 && visitLength <= 1800) aggrAccumulator.add(Constants.TIME_PERIOD_10m_30m)
      if (visitLength >= 1800) aggrAccumulator.add(Constants.TIME_PERIOD_30m)
    }

    def calStepLength(stepLength: Long): Unit = {
      if (stepLength >= 1 && stepLength <= 3) aggrAccumulator.add(Constants.STEP_PERIOD_1_3)
      if (stepLength >= 4 && stepLength <= 6) aggrAccumulator.add(Constants.STEP_PERIOD_4_6)
      if (stepLength >= 7 && stepLength <= 9) aggrAccumulator.add(Constants.STEP_PERIOD_7_9)
      if (stepLength >= 10 && stepLength <= 30) aggrAccumulator.add(Constants.STEP_PERIOD_10_30)
      if (stepLength >= 30 && stepLength <= 60) aggrAccumulator.add(Constants.STEP_PERIOD_30_60)
      if (stepLength > 60) aggrAccumulator.add(Constants.STEP_PERIOD_60)
    }
  }

  // 4.对已聚合好的数据进行按访问时长，步长比例统计
  def sessionStatistics(sessionAggrAccumulator: Accumulator[String], taskId: Int): Unit = {
    val value = sessionAggrAccumulator.value
    //拿到session总数
    val sessionCount: Long = MyStringUtils.getFieldFromConcatString(value, "|", Constants.SESSION_COUNT).toLong
    val sessionAggrRate = new SessionAggrRate(taskId, sessionCount,
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.TIME_PERIOD_1s_3s).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.TIME_PERIOD_4s_6s).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.TIME_PERIOD_7s_9s).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.TIME_PERIOD_10s_30s).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.TIME_PERIOD_30s_60s).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.TIME_PERIOD_1m_3m).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.TIME_PERIOD_3m_10m).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.TIME_PERIOD_10m_30m).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.TIME_PERIOD_30m).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.STEP_PERIOD_1_3).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.STEP_PERIOD_4_6).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.STEP_PERIOD_7_9).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.STEP_PERIOD_10_30).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.STEP_PERIOD_30_60).toLong / sessionCount).toDouble, 2),
      NumberUtils.formatDouble((MyStringUtils.getFieldFromConcatString(value, "|", Constants.STEP_PERIOD_60).toLong / sessionCount).toDouble, 2)
    )
    DaoFactory.getSessionAggrDao.insert(sessionAggrRate)
  }
}