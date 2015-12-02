package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model

import edu.ist.psu.sagnik.research.svgxmlprocess.model.Rectangle

/**
 * Created by szr163 on 12/1/15.
 */
case class Move(isAbsolute:Boolean, args:Seq[CordPair]) extends PathCommand{

  def getEndPoint(lastEndPoint:CordPair, isAbs:Boolean, mPaths:Seq[CordPair], ep:CordPair):CordPair=
    mPaths.toList match{
      case Nil => ep
      case mPath::Nil => getEndPointOnePath(lastEndPoint,isAbs,mPath)
      case mPath::restPaths => getEndPoint(
        getEndPoint(lastEndPoint,isAbs,mPath),
        isAbs,
        restPaths,
        Rectangle.rectMerge(bb,getBoundingBoxOnePath(lastEndPoint,isAbs,mPath))
      )
    }

  implicit def min(d1:Double,d2:Double)=scala.math.min(d1,d2).toFloat
  implicit def max(d1:Double,d2:Double)=scala.math.max(d1,d2).toFloat

  def getBoundingBoxOnePath(lep:CordPair,isAbs:Boolean,mPath:CordPair): Rectangle = {
    if (isAbs) Rectangle(min(lep.x,mPath.x),min(lep.y,mPath.y),max(lep.x,mPath.x),max(lep.y,mPath.y))
    else
      Rectangle(min(lep.x,lep.x+mPath.x),min(lep.y,lep.y+mPath.y),max(lep.x,lep.x+mPath.x),max(lep.y,lep.y+mPath.y))
  }

  def getEndPointOnePath(lep:CordPair, isAbs:Boolean, mPath: CordPair):CordPair=
    if (isAbs) mPath
    else CordPair(lep.x+mPath.x,lep.y+mPath.y)

}
