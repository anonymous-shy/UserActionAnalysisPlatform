package shy.sparkproject

/**
  * Created by Shy on 2018/5/7
  */

object OptionTest extends App {

  bar

  def foo {
    println("foo...")
  }

  def bar: Unit = {
    foo
    println("bar...")
  }
}
