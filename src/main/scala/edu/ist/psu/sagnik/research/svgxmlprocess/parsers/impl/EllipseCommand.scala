package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.impl

import edu.ist.psu.sagnik.research.svgxmlprocess.model.Rectangle
import edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model.{PathCommand, EllipsePath, CordPair}

/**
 * Created by sagnik on 11/26/15.
 */
case class EllipseCommand(isAbsolute:Boolean,args:Seq[EllipsePath]) extends PathCommand {

  def getBoundingBox(lastEndPoint:CordPair, isAbs:Boolean, ePaths:Seq[EllipsePath], bb:Rectangle):Rectangle=
    ePaths.toList match{
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
  def digitReduce(d:Double)=BigDecimal(d).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
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
    val fA=ep.largeArcFlag; val fS=ep.sweepFlag; val rX=ep.rx; val rY=ep.ry
    val x1_1=((x1-x2)/2)*cosine(ep.rotation)+((y1-y2)/2)*sine(ep.rotation)
    val y1_1=((y1-y2)/2)*cosine(ep.rotation)-((x1-x2)/2)*cosine(ep.rotation)
    val cx_temp_sqrt=scala.math.sqrt((rX*rX*rY*rY - rX*rX*y1_1*y1_1 - rY*rY*x1_1*x1_1)/(rX*rX*y1_1*y1_1 + rY*rY*x1_1*x1_1)) //TODO:possible exception?
    val cx_1= if (fA!=fS) (cx_temp_sqrt*rX*y1_1)/rY else -(cx_temp_sqrt*rX*y1_1)/rY
    val cy_1= if (fA!=fS) -(cx_temp_sqrt*rY*x1_1)/rX else (cx_temp_sqrt*rY*x1_1)/rX
    val cx=digitReduce(cx_1*cosine(ep.rotation)-cy_1*sine(ep.rotation)+(x1+x2)/2)
    val cy=digitReduce(cx_1*sine(ep.rotation)+cy_1*cosine(ep.rotation)+(y1+y2)/2)
    println(s"[x2,y2]:${x2;y2} [center]: ${cx},${cy}")
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
    val rX=ep.rx; val rY=ep.ry
    val x1=center.x+rX*cosine(xOptTheta)*cosine(ep.rotation) -rY*sine(xOptTheta)*sine(ep.rotation)
    val x2=center.x+rX*cosine(xOptTheta+180)*cosine(ep.rotation) -rY*sine(xOptTheta+180)*sine(ep.rotation)
    val y1=center.y+rX*cosine(yOptTheta)*sine(ep.rotation)+rY*sine(yOptTheta)*cosine(ep.rotation)
    val y2=center.y+rX*cosine(yOptTheta)*sine(ep.rotation)+rY*sine(yOptTheta)*cosine(ep.rotation)

    implicit def min(d1:Double,d2:Double)=scala.math.min(d1,d2).toFloat
    implicit def max(d1:Double,d2:Double)=scala.math.max(d1,d2).toFloat

    Rectangle(min(x1,x2),min(y1,y2),max(x1,x2),max(y1,y2))
  }

  def getEndPoint(lep:CordPair, isAbs:Boolean, ePath: EllipsePath):CordPair=
    if (isAbs) ePath.endCordPair
    else CordPair(lep.x+ePath.endCordPair.x,lep.y+ePath.endCordPair.y)

}
