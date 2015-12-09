package edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.model

/**
 * Created by sagnik on 12/2/15.
 */

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.Rectangle

object EllipseCommandHelper {

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

  def getCenterandChangedRadii(lep:CordPair,isAbs:Boolean,ep: EllipsePath):(CordPair,Double,Double)={
    val x1=lep.x; val y1=lep.y;
    val x2=if (isAbs) ep.endCordPair.x else lep.x+ep.endCordPair.x
    val y2=if (isAbs) ep.endCordPair.y else lep.y+ep.endCordPair.y
    val fA=ep.largeArcFlag;
    val fS=ep.sweepFlag;
    var rX=scala.math.abs(ep.rx); // http://users.ecs.soton.ac.uk/rfp07r/interactive-svg-examples/arc.html, line 148
    var rY=scala.math.abs(ep.ry);
    val x1_1=((x1-x2)/2)*cosine(ep.rotation)+((y1-y2)/2)*sine(ep.rotation)
    val y1_1=((y1-y2)/2)*cosine(ep.rotation)-((x1-x2)/2)*sine(ep.rotation)
    println(x1_1,y1_1)
    // Ensure radii are large enough
    // Step 1: Ensure radii are non-zero
    // Step 2: Ensure radii are positive
    //this part is from http://users.ecs.soton.ac.uk/rfp07r/interactive-svg-examples/arc.html
    val lambda = ( (x1_1 * x1_1) / (rX * rX) ) + ( (y1_1 * y1_1) / (rY * rY) );
    rX=if (lambda>1) scala.math.sqrt(lambda)*rX else rX;
    rY=if (lambda>1) scala.math.sqrt(lambda)*rY else rY;

    val cx_temp=(rX*rX*rY*rY - rX*rX*y1_1*y1_1 - rY*rY*x1_1*x1_1)/(rX*rX*y1_1*y1_1 + rY*rY*x1_1*x1_1)
    val cx_temp_sqrt=if (cx_temp<0) 0 else scala.math.sqrt(cx_temp)
    println(cx_temp_sqrt)

    val sign= if (fA == fS) -1 else 1
    val cx_1= sign* (cx_temp_sqrt*rX*y1_1)/rY
    val cy_1= sign* -(cx_temp_sqrt*rY*x1_1)/rX

    //println(cx_1,cy_1)
    val cx=digitReduce(cx_1*cosine(ep.rotation)-cy_1*sine(ep.rotation)+(x1+x2)/2)
    val cy=digitReduce(cx_1*sine(ep.rotation)+cy_1*cosine(ep.rotation)+(y1+y2)/2)
    println(s"[x2,y2]:${x2} [center]: ${cx},${cy}")
    (CordPair(cx,cy),rX,rY)
  }

  /*
  * see http://fridrich.blogspot.com/2011/06/bounding-box-of-svg-elliptical-arc.html
  * for reference. There's a possible error in the blog post. However, we are still not getting the "tightest"
  * bounding box for the ellipse.
  * */

  def getBoundingBoxOnePath(lep:CordPair,isAbs:Boolean,ep: EllipsePath):Rectangle={
    val results=getCenterandChangedRadii(lep,isAbs,ep)
    val center=results._1
    val rX=results._2
    val rY=results._3
    val xOptTheta=digitReduce(R2D(scala.math.atan(-(ep.ry/ep.rx)*scala.math.tan(D2R(ep.rotation)))))
    val yOptTheta=digitReduce(R2D(scala.math.atan(ep.ry/(ep.rx*scala.math.tan(D2R(ep.rotation))))))
    //println(xOptTheta+" "+yOptTheta)

    val x1=digitReduce(center.x+rX*cosine(xOptTheta)*cosine(ep.rotation) -rY*sine(xOptTheta)*sine(ep.rotation))
    val x2=digitReduce(center.x+rX*cosine(xOptTheta+180)*cosine(ep.rotation) -rY*sine(xOptTheta+180)*sine(ep.rotation))
    val y1=digitReduce(center.y+rX*cosine(yOptTheta)*sine(ep.rotation)+rY*sine(yOptTheta)*cosine(ep.rotation))
    val y2=digitReduce(center.y+rX*cosine(yOptTheta+180)*sine(ep.rotation)+rY*sine(yOptTheta+180)*cosine(ep.rotation))

    implicit def min(d1:Double,d2:Double)=scala.math.min(d1,d2).toFloat
    implicit def max(d1:Double,d2:Double)=scala.math.max(d1,d2).toFloat

    Rectangle(min(x1,x2),min(y1,y2),max(x1,x2),max(y1,y2))
  }

  def getEndPoint(lep:CordPair, isAbs:Boolean, ePath: EllipsePath):CordPair=
    if (isAbs) ePath.endCordPair
    else CordPair(lep.x+ePath.endCordPair.x,lep.y+ePath.endCordPair.y)

}
