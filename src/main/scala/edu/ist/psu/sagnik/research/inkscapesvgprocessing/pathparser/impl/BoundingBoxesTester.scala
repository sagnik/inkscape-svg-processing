package edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.Rectangle
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.model._

/**
 * Created by sagnik on 12/2/15.
 */
object BoundingBoxesTester extends SVGPathParser{
  def main(args: Array[String]) = {
    //ellipse commands
    val command="M80 80 A 45 45, 0, 0, 0, 125 125"
    //val command="M230 80\n           A 45 45, 0, 1, 0, 275 125"
    //val command="M230 230\n           A 45 45, 0, 1, 1, 275 275"
    //val command="M80 230\n           A 45 45, 0, 0, 1, 125 275"
    //val command="M 150,150 A 76,55 30 1 1 433,278"

    //move command
    //val command="m80 230 40 50\n           A 45 45, 0, 0, 1, 125 275"

    //curve command
    //val command="M10 10 C 20 20, 40 20, 50 10"
    //val command="M130 10 C 120 20, 180 20, 170 10"
    //val command="M130 110 C 120 140, 180 140, 170 110"

    //QBC command
    //val command="M10 80 Q 95 10 180 80"

    //line command
    //val command="M10 80 L 95 10 180 80"

    val lastEndPoint=CordPair(80,80)
    val paths=parse(svg_path,command) match {
      case Success(matched,_) => matched
      case Failure(msg,_) => {println(s"couldn't parse the command ${msg}"); sys.exit(1);}
      case Error(msg,_) => {{println(s"couldn't parse the command, exceptions: ${msg}"); sys.exit(1);}}
    }
    println(paths)

    paths(1) match{
      case a: QBC => printBB[QBC](a,lastEndPoint)
      case a: EllipseCommand => printBB[EllipseCommand](a,lastEndPoint)
      case a: Line => printBB[Line](a,lastEndPoint)
      case a: HL => printBB[HL](a,lastEndPoint)
      case a: VL => printBB[VL](a,lastEndPoint)
      case a: SMC => ???
      case a: SmQBC => ???
      case _ => ???
    }


  }

 def printBB[A<: PathCommand](p:A,lep:CordPair):Unit=
      p match {
        case p: QBC => println("[Quadretic Beizer Curve BB]: "+
          p.getBoundingBox[QBCPath](lep, p.isAbsolute, p.args))
        case p: EllipseCommand => println("[Ellipse BB]: "+
          p.getBoundingBox[EllipsePath](lep, p.isAbsolute, p.args))
        case p: Line => println("[Line BB]: "+
          p.getBoundingBox[LinePath](lep, p.isAbsolute, p.args))
        case p: HL => println("[Horizontal Line BB]: "+
          p.getBoundingBox[HLPath](lep, p.isAbsolute, p.args))
        case p: VL => println("[Vertical Line BB]: "+
          p.getBoundingBox[VLPath](lep, p.isAbsolute, p.args))
        case p: SMC => println("[Smooth Curve BB]: "+
          p.getBoundingBox[SMCPath](lep, p.isAbsolute, p.args))
        case p: SmQBC => println("[Smooth Quadretic Curve BB]: "+
          p.getBoundingBox[SmQBCPath](lep, p.isAbsolute, p.args))
        case _ => ???
      }


}
