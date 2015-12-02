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

case class Curve(isAbsolute:Boolean,args:Seq[(CordPair,CordPair,CordPair)]) extends PathCommand

case class VL(isAbsolute:Boolean,args:Seq[Double]) extends PathCommand

case class HL(isAbsolute:Boolean,args:Seq[Double]) extends PathCommand

case class ClosePath(isAbsolute:Boolean, args:Seq[Any]) extends PathCommand{
  def
}






