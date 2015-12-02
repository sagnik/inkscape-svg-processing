package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model

import edu.ist.psu.sagnik.research.svgxmlprocess.model.Rectangle

/**
 * Created by sagnik on 11/19/15.
 */

trait PathCommand{
  def isAbsolute:Boolean
  def args:Any
}

case class CordPair(x:Double,y:Double)


case class EllipsePath(rx:Double,ry:Double,rotation:Double,largeArcFlag:Boolean,sweepFlag:Boolean,endCordPair:CordPair)

case class SmQBC(isAbsolute:Boolean,args: Seq[CordPair]) extends PathCommand//smooth-quadratic-bezier-curveto

case class QBC(isAbsolute:Boolean,args:Seq[(CordPair,CordPair)]) extends PathCommand

case class SMC(isAbsolute:Boolean,args:Seq[(CordPair,CordPair)]) extends PathCommand

case class VL(isAbsolute:Boolean,args:Seq[Double]) extends PathCommand

case class HL(isAbsolute:Boolean,args:Seq[Double]) extends PathCommand

case class ClosePath(isAbsolute:Boolean, args:Seq[Any]) extends PathCommand{
  def getEndpoint(startingPoint: CordPair)=startingPoint
}

case class CurvePath(cP1:CordPair,cP2:CordPair,eP:CordPair)

case class Curve(isAbsolute:Boolean,args:Seq[CurvePath]) extends PathCommand{
  def getBoundingBox(lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[CurvePath],bb:Rectangle) =
    new RecursiveBB[CurvePath].getBoundingBox(lastEndPoint,isAbs, paths,bb)
}

case class EllipseCommand(isAbsolute:Boolean,args:Seq[EllipsePath]) extends PathCommand{
  def getBoundingBox(lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[EllipsePath],bb:Rectangle) =
    new RecursiveBB[EllipsePath].getBoundingBox(lastEndPoint,isAbs, paths,bb)
}








