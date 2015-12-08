package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model

import edu.ist.psu.sagnik.research.svgxmlprocess.model.Rectangle

/**
 * Created by szr163 on 12/1/15.
 */
class MoveHelper(isAbsolute:Boolean, args:Seq[CordPair]){

  def getEndPoint(lastEndPoint:CordPair, isAbs:Boolean, mPaths:Seq[CordPair], ep:CordPair):CordPair=
    mPaths.toList match{
      case Nil => ep
      case mPath::Nil => getEndPointOnePath(lastEndPoint,isAbs,mPath)
      case mPath::restPaths => getEndPoint(
        getEndPointOnePath(lastEndPoint,isAbs,mPath),
        isAbs,
        restPaths,
        ep
      )
    }

  def getEndPointOnePath(lep:CordPair, isAbs:Boolean, mPath: CordPair):CordPair=
    if (isAbs) mPath
    else CordPair(lep.x+mPath.x,lep.y+mPath.y)

}
