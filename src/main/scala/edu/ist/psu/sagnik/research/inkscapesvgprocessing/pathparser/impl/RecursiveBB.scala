package edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.Rectangle
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.model._

import scala.language.implicitConversions
/**
 * Created by sagnik on 12/2/15.
 */
class RecursiveBB [A] {
  def getBoundingBox(lastEndPoint:CordPair, isAbs:Boolean, paths:Seq[A],bb:Rectangle):Rectangle=
    paths.toList match {
      case Nil => bb
      case path :: Nil => Rectangle.rectMerge(bb, getBoundingBoxOnePath(lastEndPoint, isAbs, path))
      case path :: restPaths => getBoundingBox(
        new RecursiveEP[A].getEndPointOnePath(lastEndPoint, isAbs, path),
        isAbs,
        restPaths,
        Rectangle.rectMerge(bb, getBoundingBoxOnePath(lastEndPoint, isAbs, path))
      )

  }

  implicit def min(d1:Double,d2:Double)=scala.math.min(d1,d2).toFloat
  implicit def max(d1:Double,d2:Double)=scala.math.max(d1,d2).toFloat
  implicit def min(d:List[Double])=d.min.toFloat
  implicit def max(d:List[Double])=d.max.toFloat

  def getBoundingBoxOnePath(lastEndPoint:CordPair,isAbs:Boolean,path:A)=
    path match{
      case path: MovePath => {
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
      //2 is arbitrary, just creating a box
      case path: HLPath =>{
        if (isAbs)
          Rectangle(min(lastEndPoint.x,path.eP),(lastEndPoint.y-2).toFloat,max(lastEndPoint.x,path.eP),(lastEndPoint.y+2).toFloat)
        else
          Rectangle(min(lastEndPoint.x,lastEndPoint.x+path.eP),(lastEndPoint.y-2).toFloat,max(lastEndPoint.x,lastEndPoint.x+path.eP),(lastEndPoint.y+2).toFloat)
      }
      case path: VLPath =>{
        if (isAbs)
          Rectangle((lastEndPoint.x-2).toFloat,min(lastEndPoint.y,path.eP),(lastEndPoint.x+2).toFloat,max(lastEndPoint.y,path.eP))
        else
          Rectangle((lastEndPoint.x-2).toFloat,min(lastEndPoint.y,lastEndPoint.y+path.eP),(lastEndPoint.x+2).toFloat,max(lastEndPoint.y,lastEndPoint.y+path.eP))
      }

      case _ => ???

    }

}
