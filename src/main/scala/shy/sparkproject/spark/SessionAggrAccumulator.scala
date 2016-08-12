package shy.sparkproject.spark

import org.apache.spark.AccumulatorParam
import shy.sparkproject.utils.StringUtils

/**
  * Created by AnonYmous_shY on 2016/8/12.
  */
class SessionAggrAccumulator extends AccumulatorParam[String] {

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

  override def addInPlace(r1: String, r2: String): String = {
    if (r1 == "")
      r2
    else {
      val oldValue: String = StringUtils.getFieldFromConcatString(r1, "|", r2)
      val newValue: Long = oldValue.toLong + 1
      StringUtils.setFieldInConcatString(r1, "|", r2, newValue.toString)
    }
  }
}
