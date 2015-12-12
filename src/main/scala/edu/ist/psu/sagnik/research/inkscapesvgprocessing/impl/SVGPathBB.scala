package edu.ist.psu.sagnik.research.inkscapesvgprocessing.impl

import breeze.linalg._

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.{SVGPath, Rectangle}
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.impl.SVGPathfromDString
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.model.{MovePath, CordPair}

import scala.None

/**
 * Created by sagnik on 12/11/15.
 */
//what we will do here is get the bounding box of all paths and chnage the transform command in
// a path to accomodate all the transforms that has happened so far.

object SVGPathBB {
  def apply(svgPath: SVGPath):SVGPath={
    val groups=svgPath.groups
    //val pathDCommandString=svgPath.pdContent
    val pathCommands=svgPath.pOps
    val pathBBwoTransforms=if (pathCommands.nonEmpty)
      SVGPathfromDString.getPathBB(
        pathCommands.slice(1,pathCommands.length),Rectangle(0,0,0,0),
        CordPair(pathCommands(0).args(0).asInstanceOf[MovePath].eP.x,pathCommands(0).args(0).asInstanceOf[MovePath].eP.y)
      )
    else Rectangle(0f,0f,0f,0f)
    val transforms=groups.map(x=>x.transformOps).flatten ++ //the ordering is important
      svgPath.transformOps


    val finalTMatrix=transforms.map(a=>a.matrix)
      .foldLeft(DenseMatrix.eye[Float](3))((a,b)=>a*b)

    //    println(s"[path id]: ${svgPath.id}\n---------------------\n")
    //    transforms.foreach(println)
    //    println(s"[final matrix]: ${finalTMatrix}")
    //    println(s"\n---------------------\n")

    svgPath.copy(pContent = addTransforms(svgPath.pContent,finalTMatrix), bb=changedBB(pathBBwoTransforms,finalTMatrix))
  }

  def addTransforms(pC:String,ftMat:DenseMatrix[Float]):String=
    if (DenseMatrix.eye[Float](3).equals(ftMat)) //there wasn't any transformation
      pC
    else {
      //see http://www.w3.org/TR/SVG/coords.html#TransformMatrixDefined
      val transformStr = "transform=\"matrix("+
        ftMat(0,0).toString+","+ //a
        ftMat(1,0).toString+","+ //b
        ftMat(0,1).toString+","+ //c
        ftMat(1,1).toString+","+ //d
        ftMat(0,2).toString+","+ //e
        ftMat(1,2).toString+")\"" //f

      if (pC.contains("transform="))
        pC.replaceAll("transform=.*\\)\"", transformStr)
      else pC.replace("<path","<path "+transformStr)
    }

  def changedBB(bb:Rectangle,fm:DenseMatrix[Float]):Option[Rectangle]=
    if (Rectangle(0,0,0,0).equals(bb)) None
    else{
      val topleft=inv(fm)*(new DenseMatrix[Double](3,1,Array[Double](bb.x1,bb.y1,1)))
      val rightbottom=inv(fm)*(new DenseMatrix[Double](3,1,Array[Double](bb.x2,bb.y2,1)))
      Some(Rectangle(topleft(1,0).toFloat,topleft(2,0).toFloat,rightbottom(1,0).toFloat,rightbottom(2,0).toFloat))
    }

}
