package shy.sparkproject

import scala.collection.mutable.ArrayBuffer

/**
  * Created by AnonYmous_shY on 2016/8/9.
  */
object IteratorTest extends App {

//  val it = Iterator("Baidu", "Google", "Runoob", "Taobao")
//  while (it.hasNext)
//    println(it.next())
//  println("==========")
//  it.foreach(s => println(s))
  val arr = new ArrayBuffer[String]()
  arr += "Baidu"
  arr += "Google"
  arr += "Runoob"
  arr += "Taobao"
  arr += "Anonymous"
  private val s: String = arr.mkString(",").toString
  println(s"arr=$s|")
}
