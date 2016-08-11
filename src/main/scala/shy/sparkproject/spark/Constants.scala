package shy.sparkproject.spark

/**
  * Created by AnonYmous_shY on 2016/8/10.
  */
object Constants {

  /**
    * Spark作业相关的常量
    */
  lazy val FIELD_SESSION_ID = "sessionId"
  lazy val FIELD_SEARCH_KEYWORDS = "searchKeywords"
  lazy val FIELD_CLICK_CATEGORY_IDS = "clickCategoryIds"
  lazy val FIELD_CLICK_PRODUCT_IDS = "clickProductIds"
  lazy val FIELD_AGE = "age"
  lazy val FIELD_PROFESSIONAL = "professional"
  lazy val FIELD_CITY = "city"
  lazy val FIELD_SEX = "sex"
  lazy val FIELD_VISIT_LENGTH = "visitLength"
  lazy val FIELD_STEP_LENGTH = "stepLength"
  lazy val FIELD_START_TIME = "startTime"
  lazy val FIELD_CLICK_COUNT = "clickCount"
  lazy val FIELD_ORDER_COUNT = "orderCount"
  lazy val FIELD_PAY_COUNT = "payCount"
  lazy val FIELD_CATEGORY_ID = "categoryid"

  lazy val SESSION_COUNT = "session_count"

  lazy val TIME_PERIOD_1s_3s = "1s_3s"
  lazy val TIME_PERIOD_4s_6s = "4s_6s"
  lazy val TIME_PERIOD_7s_9s = "7s_9s"
  lazy val TIME_PERIOD_10s_30s = "10s_30s"
  lazy val TIME_PERIOD_30s_60s = "30s_60s"
  lazy val TIME_PERIOD_1m_3m = "1m_3m"
  lazy val TIME_PERIOD_3m_10m = "3m_10m"
  lazy val TIME_PERIOD_10m_30m = "10m_30m"
  lazy val TIME_PERIOD_30m = "30m"

  lazy val STEP_PERIOD_1_3 = "1_3"
  lazy val STEP_PERIOD_4_6 = "4_6"
  lazy val STEP_PERIOD_7_9 = "7_9"
  lazy val STEP_PERIOD_10_30 = "10_30"
  lazy val STEP_PERIOD_30_60 = "30_60"
  lazy val STEP_PERIOD_60 = "60"

  /**
    * Task相关的常量
    */
  lazy val PARAM_START_DATE = "startDate"
  lazy val PARAM_END_DATE = "endDate"
  lazy val PARAM_START_AGE = "startAge"
  lazy val PARAM_END_AGE = "endAge"
  lazy val PARAM_PROFESSIONALS = "professionals"
  lazy val PARAM_CITIES = "cities"
  lazy val PARAM_SEX = "sex"
  lazy val PARAM_SEARCH_KEYWORDS = "searchKeywords"
  lazy val PARAM_CATEGORY_IDS = "categoryIds"
  lazy val PARAM_PRODUCT_IDS = "productIds"
  lazy val PARAM_TARGET_PAGE_FLOW = "targetPageFlow"

}
