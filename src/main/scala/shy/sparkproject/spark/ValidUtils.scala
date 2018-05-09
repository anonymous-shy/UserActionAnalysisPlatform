package shy.sparkproject.spark

/**
  * Created by AnonYmous_shY on 2016/8/11.
  */
object ValidUtils {

  def between(dataMap: Map[String, String], dateField: String, paramMap: Map[String, String], startParamField: String, endParamField: String): Boolean = {
    if (paramMap.get(startParamField).isEmpty || paramMap.get(endParamField).isEmpty)
      true
    else {
      val start: Int = paramMap(startParamField).toInt
      val end: Int = paramMap(endParamField).toInt
      if (dataMap.get(dateField).isDefined) {
        val data: Int = dataMap(dateField).toInt
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
      val params: Seq[String] = paramMap(paramField).split(",").toSeq
      if (dataMap.get(dataField).isDefined) {
        val datas: Seq[String] = dataMap(dataField).split(",").toSeq
        params.containsSlice(datas)
      } else false
    }
  }

  def equal(dataMap: Map[String, String], dataField: String, paramMap: Map[String, String], paramField: String): Boolean = {
    if (paramMap.get(paramField).isEmpty)
      true
    else {
      val param: String = paramMap(paramField)
      if (dataMap.get(dataField).isDefined) {
        val data: String = dataMap(dataField)
        param.equalsIgnoreCase(data)
      } else false
    }
  }
}
