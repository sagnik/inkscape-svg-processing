package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model

/**
 * Created by sagnik on 12/3/15.
 */
import scala.language.reflectiveCalls
//scala type parameterization test
//----------------------------------------

trait X{
  val a=1;
  val b=2;
}

case class Y(override val a:Int, override val b:Int) extends X{
  def sum(c:Int)=a+b+c
}

case class Z(override val a:Int, override val b:Int) extends X{
  def sum(c:Int)=a+b+c+10
}



object Test{
  def main(args: Array[String]) {
    val z = Z(1, 2)
    val y = Y(3, 4)
    //println (y.sum(5))
    //new MyTest[Y]().myPrint(y)
    myPrint(z)
  }

  def myPrint[A<: { def sum(i: Int): Int }](z: A):Unit=
    if (z.isInstanceOf[A]) {
      val c = z.asInstanceOf[A]
      println(c.sum(4)) //error here
    }

}
/*
class MyTest[A <: { def sum(i: Int): Int }] {
  def myPrint(z: A): Unit =
    println(z.sum(0)) // I added a zero just to fix the example
}
*/

