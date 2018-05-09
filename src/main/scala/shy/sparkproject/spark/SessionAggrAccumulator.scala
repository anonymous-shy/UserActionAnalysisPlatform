package shy.sparkproject.spark

import org.apache.spark.AccumulatorParam
import shy.sparkproject.utils.MyStringUtils

/**
  * Created by AnonYmous_shY on 2016/8/12.
  */
class SessionAggrAccumulator extends AccumulatorParam[String] {

  /**
    * 用于数据初始化
    * 返回一个值，初始化中，所有范围区间的数量都是 0，
    * 各个范围的统计数量的拼接，采用key=value|key=value连接串的格式
    *
    * @param initialValue 初始值
    * @return
    */
  override def zero(initialValue: String): String = Constants.SESSION_COUNT + "=0|" +
    Constants.TIME_PERIOD_1s_3s + "=0|" +
    Constants.TIME_PERIOD_4s_6s + "=0|" +
    Constants.TIME_PERIOD_7s_9s + "=0|" +
    Constants.TIME_PERIOD_10s_30s + "=0|" +
    Constants.TIME_PERIOD_30s_60s + "=0|" +
    Constants.TIME_PERIOD_1m_3m + "=0|" +
    Constants.TIME_PERIOD_3m_10m + "=0|" +
    Constants.TIME_PERIOD_10m_30m + "=0|" +
    Constants.TIME_PERIOD_30m + "=0|" +
    Constants.STEP_PERIOD_1_3 + "=0|" +
    Constants.STEP_PERIOD_4_6 + "=0|" +
    Constants.STEP_PERIOD_7_9 + "=0|" +
    Constants.STEP_PERIOD_10_30 + "=0|" +
    Constants.STEP_PERIOD_30_60 + "=0|" +
    Constants.STEP_PERIOD_60 + "=0"

  /**
    * r1初始化字符串，r2为遍历session时，判断某个session对应的区间，
    * 在r1中找到r2对应的value，累加1，更新到r1字符串中
    *
    * @param r1 初始化字符串
    * @param r2 累加字符串
    * @return
    */
  override def addInPlace(r1: String, r2: String): String = {
    if (r1 == "")
      r2
    else {
      val oldValue: String = MyStringUtils.getFieldFromConcatString(r1, "|", r2)
      val newValue: Long = oldValue.toLong + 1
      val newR1 = MyStringUtils.setFieldInConcatString(r1, "|", r2, newValue.toString)
      newR1
    }
  }
}
