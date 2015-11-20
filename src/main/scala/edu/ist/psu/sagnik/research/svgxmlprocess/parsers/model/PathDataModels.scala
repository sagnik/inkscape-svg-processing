package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model

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

case class SmQBC(isAbsolute:Boolean,args: Seq[CordPair]) extends PathCommand//smooth-quadratic-bezier-curveto

case class QBC(isAbsolute:Boolean,args:Seq[(CordPair,CordPair)]) extends PathCommand

case class SMC(isAbsolute:Boolean,args:Seq[(CordPair,CordPair)]) extends PathCommand

case class Curve(isAbsolute:Boolean,args:Seq[(CordPair,CordPair,CordPair)]) extends PathCommand

case class VL(isAbsolute:Boolean,args:Seq[Double]) extends PathCommand

case class HL(isAbsolute:Boolean,args:Seq[Double]) extends PathCommand

case class Line(isAbsolute:Boolean, args:Seq[CordPair]) extends PathCommand

case class ClosePath(isAbsolute:Boolean, args:Seq[Any]) extends PathCommand

case class Move(isAbsolute:Boolean, args:Seq[Any]) extends PathCommand



