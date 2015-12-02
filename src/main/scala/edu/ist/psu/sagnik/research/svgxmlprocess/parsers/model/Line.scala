package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model

import edu.ist.psu.sagnik.research.svgxmlprocess.model.Rectangle

/**
 * Created by szr163 on 12/1/15.
 */
case class Line(isAbsolute:Boolean, args:Seq[CordPair]) extends PathCommand{
  def getBoundingBox(lastEndPoint:CordPair, isAbs:Boolean, lPaths:Seq[CordPair], bb:Rectangle):Rectangle=
    lPaths.toList match{
      case Nil => bb
      case lPath::Nil => getBoundingBoxOnePath(lastEndPoint,isAbs,lPath)
      case lPath::restPaths => getBoundingBox(
        getEndPoint(lastEndPoint,isAbs,lPath),
        isAbs,
        restPaths,
        Rectangle.rectMerge(bb,getBoundingBoxOnePath(lastEndPoint,isAbs,lPath))
      )
    }

  implicit def min(d1:Double,d2:Double)=scala.math.min(d1,d2).toFloat
  implicit def max(d1:Double,d2:Double)=scala.math.max(d1,d2).toFloat

  def getBoundingBoxOnePath(lep:CordPair,isAbs:Boolean,lPath:CordPair): Rectangle = {
    if (isAbs) Rectangle(min(lep.x,lPath.x),min(lep.y,lPath.y),max(lep.x,lPath.x),max(lep.y,lPath.y))
    else
      Rectangle(min(lep.x,lep.x+lPath.x),min(lep.y,lep.y+lPath.y),max(lep.x,lep.x+lPath.x),max(lep.y,lep.y+lPath.y))
  }

  def getEndPoint(lep:CordPair, isAbs:Boolean, lPath: CordPair):CordPair=
    if (isAbs) lPath
    else CordPair(lep.x+lPath.x,lep.y+lPath.y)

}



