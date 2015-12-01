package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.impl

import edu.ist.psu.sagnik.research.svgxmlprocess.model.Rectangle
import edu.ist.psu.sagnik.research.svgxmlprocess.parsers.impl.TestSVGPathParser._
import edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model.CordPair

import scala.Error

/**
 * Created by szr163 on 11/30/15.
 */
object BoundingBoxes extends SVGPathParser{
  def main(args: Array[String]) = {
    val command="M80 80 A 45 45, 0, 0, 0, 125 125"
    val lastEndPoint=CordPair(80,80)
    val paths=parse(svg_path,command) match {
      case Success(matched,_) => matched
      case Failure(msg,_) => {println(s"couldn't parse the command ${msg}"); sys.exit(1);}
      case Error(msg,_) => {{println(s"couldn't parse the command, exceptions: ${msg}"); sys.exit(1);}}
    }
    if (paths(1).isInstanceOf[EllipseCommand]) {
      val ellipseCommand=paths(1).asInstanceOf[EllipseCommand]
      val bb=ellipseCommand.getBoundingBox(lastEndPoint,ellipseCommand.isAbsolute,ellipseCommand.args,Rectangle(0,0,0,0))
      println(ellipseCommand.args(0))
      println(bb)
    }
  }
}
