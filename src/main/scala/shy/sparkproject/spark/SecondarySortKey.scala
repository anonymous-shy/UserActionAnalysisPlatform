package shy.sparkproject.spark

/**
  * Created by Shy on 2018/5/14
  */

class SecondarySortKey(val clickCount: Int,
                       val orderCount: Int,
                       val payCount: Int) extends Ordered[SecondarySortKey] with Serializable {
  override def compare(that: SecondarySortKey): Int = {
    if (clickCount - that.clickCount != 0)
      clickCount - that.clickCount
    else if (orderCount - that.orderCount != 0)
      orderCount - that.orderCount
    else if (payCount - that.payCount != 0)
      payCount - that.payCount
    else 0
  }
}
