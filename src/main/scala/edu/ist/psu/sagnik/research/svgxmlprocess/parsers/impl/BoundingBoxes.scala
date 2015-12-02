package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.impl

import edu.ist.psu.sagnik.research.svgxmlprocess.model.Rectangle
import edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model.{EllipseCommand, CordPair}


/**
 * Created by szr163 on 11/30/15.
 */
object BoundingBoxes extends SVGPathParser{
  def main(args: Array[String]) = {
    //val command="M80 80 A 45 45, 0, 0, 0, 125 125"
    //val command="M230 80\n           A 45 45, 0, 1, 0, 275 125"
    //val command="M230 230\n           A 45 45, 0, 1, 1, 275 275"
    val command="M80 230\n           A 45 45, 0, 0, 1, 125 275"
    //val command="M 150,150 A 76,55 30 1 1 433,278"

    val lastEndPoint=CordPair(80,230)
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
