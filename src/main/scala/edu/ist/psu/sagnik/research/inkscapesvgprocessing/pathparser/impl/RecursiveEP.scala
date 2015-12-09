package edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.model._

/**
 * Created by sagnik on 12/8/15.
 */
class RecursiveEP[A] {
  def getEndPoint(lastEndPoint: CordPair, isAbs: Boolean, paths: Seq[A]): CordPair =
    paths.toList match {
      case Nil => lastEndPoint
      case path :: Nil => getEndPointOnePath(lastEndPoint, isAbs, path)
      case path :: restPaths => getEndPoint(
        getEndPointOnePath(lastEndPoint, isAbs, path),
        isAbs,
        restPaths
      )
    }

  def getEndPointOnePath(lastEndPoint:CordPair,isAbs:Boolean,path:A)=
    path match{
      case path: EllipsePath => EllipseCommandHelper.getEndPoint(lastEndPoint,isAbs,path.asInstanceOf[EllipsePath])
      case path: CurvePath => if (isAbs) path.eP else CordPair(lastEndPoint.x+path.eP.x,lastEndPoint.y+path.eP.y)
      case path: QBCPath => if (isAbs) path.eP else CordPair(lastEndPoint.x+path.eP.x,lastEndPoint.y+path.eP.y)
      case path: LinePath => if (isAbs) path.eP else CordPair(lastEndPoint.x+path.eP.x,lastEndPoint.y+path.eP.y)
      case path: HLPath => if (isAbs) CordPair(path.eP,lastEndPoint.y) else CordPair(lastEndPoint.x+path.eP,lastEndPoint.y)
      case path: VLPath => if (isAbs) CordPair(lastEndPoint.x,path.eP) else CordPair(lastEndPoint.x,lastEndPoint.y+path.eP)
      case path: MovePath => if (isAbs) path.eP else CordPair(lastEndPoint.x+path.eP.x,lastEndPoint.y+path.eP.y)
      case _ => ???
    }
}

