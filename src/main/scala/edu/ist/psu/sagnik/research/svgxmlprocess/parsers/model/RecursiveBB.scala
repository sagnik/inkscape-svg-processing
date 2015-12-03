package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model

import edu.ist.psu.sagnik.research.svgxmlprocess.model.Rectangle
import scala.language.implicitConversions
/**
 * Created by sagnik on 12/2/15.
 */
class RecursiveBB [A] {
  def getBoundingBox(lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[A],bb:Rectangle):Rectangle=
    paths.toList match{
      case Nil => bb
      case path::Nil => getBoundingBoxOnePath(lastEndPoint,isAbs,path)
      case path::restPaths => getBoundingBox(
        getEndPoint(lastEndPoint,isAbs,path),
        isAbs,
        restPaths,
        Rectangle.rectMerge(bb,getBoundingBoxOnePath(lastEndPoint,isAbs,path))
      )
    }

  implicit def min(d1:Double,d2:Double)=scala.math.min(d1,d2).toFloat
  implicit def max(d1:Double,d2:Double)=scala.math.max(d1,d2).toFloat
  implicit def min(d:List[Double])=d.min.toFloat
  implicit def max(d:List[Double])=d.max.toFloat

  def getBoundingBoxOnePath(lastEndPoint:CordPair,isAbs:Boolean,path:A)=
    path match{
      case path: EllipsePath => EllipseCommandHelper.getBoundingBoxOnePath(lastEndPoint,isAbs,path)
      case path: CurvePath => Rectangle(
        min(List(lastEndPoint.x,path.cP1.x,path.cP2.x,path.eP.x)),
        min(List(lastEndPoint.y,path.cP1.y,path.cP2.y,path.eP.y)),
        max(List(lastEndPoint.x,path.cP1.x,path.cP2.x,path.eP.x)),
        max(List(lastEndPoint.y,path.cP1.y,path.cP2.y,path.eP.y))
      )
      case path: QBCPath => Rectangle(
        min(List(lastEndPoint.x,path.cP1.x,path.eP.x)),
        min(List(lastEndPoint.y,path.cP1.y,path.eP.y)),
        max(List(lastEndPoint.x,path.cP1.x,path.eP.x)),
        max(List(lastEndPoint.y,path.cP1.y,path.eP.y))
      )
      case path: LinePath => {
        if (isAbs)
          Rectangle(min(lastEndPoint.x,path.eP.x),min(lastEndPoint.y,path.eP.y),
            max(lastEndPoint.x,path.eP.x),max(lastEndPoint.y,path.eP.y))
        else
          Rectangle(
            min(lastEndPoint.x,lastEndPoint.x+path.eP.x),
            min(lastEndPoint.y,lastEndPoint.y+path.eP.y),
            max(lastEndPoint.x,lastEndPoint.x+path.eP.x),
            max(lastEndPoint.y,lastEndPoint.y+path.eP.y))
      }
      case _ => ???

    }

  def getEndPoint(lastEndPoint:CordPair,isAbs:Boolean,path:A)=
   path match{
     case path: EllipsePath => EllipseCommandHelper.getEndPoint(lastEndPoint,isAbs,path.asInstanceOf[EllipsePath])
     case path: CurvePath => if (isAbs) path.eP else CordPair(lastEndPoint.x+path.eP.x,lastEndPoint.y+path.eP.y)
     case path: QBCPath => if (isAbs) path.eP else CordPair(lastEndPoint.x+path.eP.x,lastEndPoint.y+path.eP.y)
     case path: LinePath => if (isAbs) path.eP else CordPair(lastEndPoint.x+path.eP.x,lastEndPoint.y+path.eP.y)
     case _ => ???
   }

}
