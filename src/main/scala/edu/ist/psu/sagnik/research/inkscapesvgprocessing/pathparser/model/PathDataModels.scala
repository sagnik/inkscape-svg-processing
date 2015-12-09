package edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.model

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.Rectangle
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.impl.{RecursiveEP, RecursiveBB}

/**
 * Created by sagnik on 11/19/15.
 */


trait PathCommand{
  def isAbsolute:Boolean
  def args:Seq[Any]
  def getBoundingBox[A](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]):Rectangle
  def getEndPoint[A](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]):CordPair
}

case class CordPair(x:Double,y:Double)

case class EllipsePath(rx:Double,ry:Double,rotation:Double,largeArcFlag:Boolean,sweepFlag:Boolean,endCordPair:CordPair)
case class EllipseCommand(isAbsolute:Boolean,args:Seq[EllipsePath]) extends PathCommand{
  def getBoundingBox[Ellipse](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveBB[EllipsePath].getBoundingBox(lastEndPoint,isAbs, paths.asInstanceOf[Seq[EllipsePath]],Rectangle(0,0,0,0))
  def getEndPoint[Ellipse](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveEP[EllipsePath].getEndPoint(lastEndPoint,isAbs, paths.asInstanceOf[Seq[EllipsePath]])

}

case class SmQBCPath(eP:CordPair)
case class SmQBC(isAbsolute:Boolean,args: Seq[SmQBCPath]) extends PathCommand{
  def getBoundingBox[SmQBC](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveBB[SmQBCPath].getBoundingBox(lastEndPoint,isAbs, paths.asInstanceOf[Seq[SmQBCPath]],Rectangle(0,0,0,0))
  def getEndPoint[SmQBC](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveEP[SmQBCPath].getEndPoint(lastEndPoint,isAbs, paths.asInstanceOf[Seq[SmQBCPath]])
}//smooth-quadratic-bezier-curveto

case class QBCPath(cP1:CordPair,eP:CordPair)
case class QBC(isAbsolute:Boolean,args:Seq[QBCPath]) extends PathCommand{
  def getBoundingBox[QBC](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveBB[QBCPath].getBoundingBox(lastEndPoint,isAbs, paths.asInstanceOf[Seq[QBCPath]],Rectangle(0,0,0,0))
  def getEndPoint[QBC](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveEP[QBCPath].getEndPoint(lastEndPoint,isAbs, paths.asInstanceOf[Seq[QBCPath]])
}

case class SMCPath(cP1:CordPair,eP:CordPair)
case class SMC(isAbsolute:Boolean,args:Seq[SMCPath]) extends PathCommand{
  def getBoundingBox[SMC](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveBB[SMCPath].getBoundingBox(lastEndPoint,isAbs, paths.asInstanceOf[Seq[SMCPath]],Rectangle(0,0,0,0))
  def getEndPoint[SMC](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveEP[SMCPath].getEndPoint(lastEndPoint,isAbs, paths.asInstanceOf[Seq[SMCPath]])
}

case class VLPath(eP:Double)
case class VL(isAbsolute:Boolean,args:Seq[VLPath]) extends PathCommand{
  def getBoundingBox[VL](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveBB[VLPath].getBoundingBox(lastEndPoint,isAbs, paths.asInstanceOf[Seq[VLPath]],Rectangle(0,0,0,0))
  def getEndPoint[VL](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveEP[VLPath].getEndPoint(lastEndPoint,isAbs, paths.asInstanceOf[Seq[VLPath]])
}

case class HLPath(eP:Double)
case class HL(isAbsolute:Boolean,args:Seq[HLPath]) extends PathCommand{
  def getBoundingBox[HL](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveBB[HLPath].getBoundingBox(lastEndPoint,isAbs, paths.asInstanceOf[Seq[HLPath]],Rectangle(0,0,0,0))
  def getEndPoint[HL](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveEP[HLPath].getEndPoint(lastEndPoint,isAbs, paths.asInstanceOf[Seq[HLPath]])
}

case class Close(isAbsolute:Boolean, args:Seq[Any]) extends PathCommand{
  def getBoundingBox[Close](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) = Rectangle(0,0,0,0)
  def getEndPoint[Close](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) = CordPair(0,0)
}

case class CurvePath(cP1:CordPair,cP2:CordPair,eP:CordPair)
case class Curve(isAbsolute:Boolean,args:Seq[CurvePath]) extends PathCommand{
  def getBoundingBox[Curve](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveBB[CurvePath].getBoundingBox(lastEndPoint,isAbs, paths.asInstanceOf[Seq[CurvePath]],Rectangle(0,0,0,0))
  def getEndPoint[Curve](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveEP[CurvePath].getEndPoint(lastEndPoint,isAbs, paths.asInstanceOf[Seq[CurvePath]])
}

case class LinePath(eP:CordPair)
case class Line(isAbsolute:Boolean, args:Seq[LinePath]) extends PathCommand {
  def getBoundingBox[Line](lastEndPoint: CordPair, isAbs: Boolean, paths: Seq[Any]) =
    new RecursiveBB[LinePath].getBoundingBox(lastEndPoint, isAbs, paths.asInstanceOf[Seq[LinePath]], Rectangle(0,0,0,0))
  def getEndPoint[Line](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveEP[LinePath].getEndPoint(lastEndPoint,isAbs, paths.asInstanceOf[Seq[LinePath]])
}

case class MovePath(eP:CordPair)
case class Move(isAbsolute:Boolean, args:Seq[MovePath]) extends PathCommand{
  def getBoundingBox[Move](lastEndPoint: CordPair, isAbs: Boolean, paths: Seq[Any]) =
    new RecursiveBB[MovePath].getBoundingBox(lastEndPoint, isAbs, paths.asInstanceOf[Seq[MovePath]],Rectangle(0,0,0,0))
  def getEndPoint[Move](lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[Any]) =
    new RecursiveEP[MovePath].getEndPoint(lastEndPoint,isAbs, paths.asInstanceOf[Seq[MovePath]])
}










