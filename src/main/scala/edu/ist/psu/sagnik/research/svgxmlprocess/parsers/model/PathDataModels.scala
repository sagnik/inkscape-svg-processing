package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model

import edu.ist.psu.sagnik.research.svgxmlprocess.model.Rectangle

/**
 * Created by sagnik on 11/19/15.
 */

sealed trait PathCommand{
  def isAbsolute:Boolean
  def args:Any
}

case class CordPair(x:Double,y:Double)


case class EllipsePath(rx:Double,ry:Double,rotation:Double,largeArcFlag:Boolean,sweepFlag:Boolean,endCordPair:CordPair)
case class EllipseCommand(isAbsolute:Boolean,args:Seq[EllipsePath]) extends PathCommand

object EllispeCommand{
  def getBoundingBox(lastEndPoint:CordPair, isAbs:Boolean, ePaths:Seq[EllipsePath], bb:Rectangle):Rectangle=
    ePaths match{
      case Nil => bb
      case ePath::Nil => getBoundingBoxOnePath(lastEndPoint,isAbs,ePath)
      case ePath::restPaths => getBoundingBox(
        getEndPoint(lastEndPoint,isAbs,ePath),
        isAbs,
        restPaths,
        Rectangle.rectMerge(bb,getBoundingBoxOnePath(lastEndPoint,isAbs,ePath))
      )
    }
  /*input is assumed to be degrees, not radians*/
  def digitReduce(d:Double)=d-d%0.01
  def D2R(deg:Double)=deg*(scala.math.Pi/180)
  def R2D(deg:Double)=deg/(scala.math.Pi/180)
  def cosine(deg:Double)=digitReduce(scala.math.cos(D2R(deg)))
  def sine(deg:Double)=digitReduce(scala.math.sin(D2R(deg)))

  /*
  * See http://www.w3.org/TR/SVG/implnote.html#ArcConversionEndpointToCenter
  * for reference
  * */

  def getCenter(lep:CordPair,isAbs:Boolean,ep: EllipsePath):CordPair={
    val x1=lep.x; val y1=lep.y;
    val x2=if (isAbs) ep.endCordPair.x else lep.x+ep.endCordPair.x
    val y2=if (isAbs) ep.endCordPair.y else lep.y+ep.endCordPair.y
    val fA=ep.largeArcFlag; val fS=ep.sweepFlag; val rX=ep.rx val rY=ep.ry
    val x1_1=((x1-x2)/2)*cosine(ep.rotation)+((y1-y2)/2)*sine(ep.rotation)
    val y1_1=((y1-y2)/2)*cosine(ep.rotation)-((x1-x2)/2)*cosine(ep.rotation)
    val cx_temp_sqrt=scala.math.sqrt((rX*rX*rY*rY - rX*rX*y1_1*y1_1 - rY*rY*x1_1*x1_1)/(rX*rX*y1_1*y1_1 + rY*rY*x1_1*x1_1)) //TODO:possible exception?
    val cx_1= if (fA!=fS) (cx_temp_sqrt*rX*y1_1)/rY else -(cx_temp_sqrt*rX*y1_1)/rY
    val cy_1= if (fA!=fS) -(cx_temp_sqrt*rY*x1_1)/rX else (cx_temp_sqrt*rY*x1_1)/rX
    val cx=digitReduce(cx_1*cosine(ep.rotation)-cy_1*sine(ep.rotation)+(x1+x2)/2)
    val cy=digitReduce(cx_1*sine(ep.rotation)+cy_1*cosine(ep.rotation)+(y1+y2)/2)
    CordPair(cx,cy)
  }

  /*
  * see http://fridrich.blogspot.com/2011/06/bounding-box-of-svg-elliptical-arc.html
  * for reference. Thre's a possible error.
  * */

  def getBoundingBoxOnePath(lep:CordPair,isAbs:Boolean,ep: EllipsePath):Rectangle={
    val center=getCenter(lep,isAbs,ep)
    val xOptTheta=digitReduce(R2D(scala.math.atan(-(ep.ry/ep.rx)*scala.math.tan(D2R(ep.rotation)))))
    val yOptTheta=digitReduce(R2D(scala.math.atan(ep.ry/(ep.rx*scala.math.tan(D2R(ep.rotation))))))
    val xmin=
  }

  def getEndPoint(lep:CordPair, isAbs:Boolean, ePath: EllipsePath):CordPair=
    if (isAbs) ePath.endCordPair
    else CordPair(lep.x+ePath.endCordPair.x,lep.y+ePath.endCordPair.y)
}

case class SmQBC(isAbsolute:Boolean,args: Seq[CordPair]) extends PathCommand//smooth-quadratic-bezier-curveto

case class QBC(isAbsolute:Boolean,args:Seq[(CordPair,CordPair)]) extends PathCommand

case class SMC(isAbsolute:Boolean,args:Seq[(CordPair,CordPair)]) extends PathCommand

case class Curve(isAbsolute:Boolean,args:Seq[(CordPair,CordPair,CordPair)]) extends PathCommand

case class VL(isAbsolute:Boolean,args:Seq[Double]) extends PathCommand

case class HL(isAbsolute:Boolean,args:Seq[Double]) extends PathCommand

case class Line(isAbsolute:Boolean, args:Seq[CordPair]) extends PathCommand

case class ClosePath(isAbsolute:Boolean, args:Seq[Any]) extends PathCommand

case class Move(isAbsolute:Boolean, args:Seq[Any]) extends PathCommand



