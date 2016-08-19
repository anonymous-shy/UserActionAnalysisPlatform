package shy.sparkproject

import java.util.Date

/**
  * Created by AnonYmous_shY on 2016/8/12.
  */
object DateTest extends App {

  //  private val date: Date = new Date
  //  println(date.getTime)

  var map1 = Map[String, Map[String, Long]]()
  var map2 = Map[String, Long]()

  map2 += ("AA" -> 123)
  map1 += ("A" -> map2)
  println(map2)
  println(map1)
}
