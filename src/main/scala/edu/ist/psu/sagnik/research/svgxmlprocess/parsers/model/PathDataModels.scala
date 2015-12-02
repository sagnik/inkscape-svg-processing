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
case class EllipseCommand(isAbsolute:Boolean,args:Seq[EllipsePath]) extends PathCommand{
  def getBoundingBox(lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[EllipsePath],bb:Rectangle) =
    new RecursiveBB[EllipsePath].getBoundingBox(lastEndPoint,isAbs, paths,bb)
}

case class SmQBCPath(eP:CordPair)
case class SmQBC(isAbsolute:Boolean,args: Seq[SmQBCPath]) extends PathCommand//smooth-quadratic-bezier-curveto

case class QBCPath(cP1:CordPair,eP:CordPair)
case class QBC(isAbsolute:Boolean,args:Seq[QBCPath]) extends PathCommand

case class SMCPath(cP1:CordPair,eP:CordPair)
case class SMC(isAbsolute:Boolean,args:Seq[SMCPath]) extends PathCommand

case class VLPath(eP:Double)
case class VL(isAbsolute:Boolean,args:Seq[VLPath]) extends PathCommand

case class HLPath(eP:Double)
case class HL(isAbsolute:Boolean,args:Seq[HLPath]) extends PathCommand

case class ClosePath(isAbsolute:Boolean, args:Seq[Any]) extends PathCommand{
  def getEndpoint(startingPoint: CordPair)=startingPoint
}

case class CurvePath(cP1:CordPair,cP2:CordPair,eP:CordPair)

case class Curve(isAbsolute:Boolean,args:Seq[CurvePath]) extends PathCommand{
  def getBoundingBox(lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[CurvePath],bb:Rectangle) =
    new RecursiveBB[CurvePath].getBoundingBox(lastEndPoint,isAbs, paths,bb)
}









