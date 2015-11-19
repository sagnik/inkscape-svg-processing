package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model

/**
 * Created by sagnik on 11/19/15.
 */
case class EllipsePath(rx:Double,ry:Double,rotation:Double,largeArcFlag:Boolean,sweepFlag:Boolean,endX:Double,endY:Double)
case class EllipseCommand(isAbsolute:Boolean,paths:Seq[EllipsePath])

