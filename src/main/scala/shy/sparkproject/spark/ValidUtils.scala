package shy.sparkproject.spark

/**
  * Created by AnonYmous_shY on 2016/8/11.
  */
object ValidUtils {

  def between(dataMap: Map[String, String], dateField: String, paramMap: Map[String, String], startParamField: String, endParamField: String): Boolean = {
    if (paramMap.get(startParamField).isEmpty || paramMap.get(endParamField).isEmpty)
      true
    else {
      val start: Int = paramMap.get(startParamField).get.toInt
      val end: Int = paramMap.get(endParamField).get.toInt
      if (dataMap.get(dateField).isDefined) {
        val data: Int = dataMap.get(dateField).get.toInt
        if (data >= start && data <= end)
          true
        else false
      } else false
    }
  }

  def in(dataMap: Map[String, String], dataField: String, paramMap: Map[String, String], paramField: String): Boolean = {
    if (paramMap.get(paramField).isEmpty)
      true
    else {
      val params: Seq[String] = paramMap.get(paramField).get.split(",").toSeq
      if (dataMap.get(dataField).isDefined) {
        val datas: Seq[String] = dataMap.get(dataField).get.split(",").toSeq
        params.containsSlice(datas)
      } else false
    }
  }

  def equal(dataMap: Map[String, String], dataField: String, paramMap: Map[String, String], paramField: String): Boolean = {
    if (paramMap.get(paramField).isEmpty)
      true
    else {
      val param: String = paramMap.get(paramField).get
      if (dataMap.get(dataField).isDefined) {
        val data: String = dataMap.get(dataField).get
        param.equalsIgnoreCase(data)
      } else false
    }
  }
}
