package edu.ist.psu.sagnik.research.inkscapesvgprocessing.impl

import breeze.linalg.DenseMatrix
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.Rectangle
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.model.SVGChar

/**
 * Created by sagnik on 12/17/15.
 */
object SVGCharBB {
  def apply(svgChar: SVGChar):SVGChar={
    val groups=svgChar.groups
    val charBBwoTransforms=svgChar.bb
    val transforms=groups.map(x=>x.transformOps).flatten ++ //the ordering is important
      svgChar.transformOps

    val finalTMatrix=transforms.map(a=>a.matrix)
      .foldLeft(DenseMatrix.eye[Float](3))((a,b)=>a*b)

    val bb=SVGPathBB.changedBB(charBBwoTransforms,finalTMatrix) match{
      case Some(bb) => bb
      case None => Rectangle(0,0,0,0)
    }
    svgChar.copy(charSVGString = addTransforms(svgChar,finalTMatrix), bb=bb)
  }

  def addTransforms(svgChar: SVGChar,fM: DenseMatrix[Float]):String=
    if (DenseMatrix.eye[Float](3).equals(fM)) //there wasn't any transformation
      "<text style=\"" +
        svgChar.styleString+
        "\" " +
        "y=\"" +
        svgChar.bb.y1 +
        "\" x=\"" +
        svgChar.bb.x1 +
        "\">" +
        svgChar.content+
        "</text>"

    else
      "<text style=\"" +
        svgChar.styleString+
        "\" " +
        "transform=\"matrix(" +
        fM(0,0).toString+","+ //a
        fM(1,0).toString+","+ //b
        fM(0,1).toString+","+ //c
        fM(1,1).toString+","+ //d
        fM(0,2).toString+","+ //e
        fM(1,2).toString+ //f
        ")\" y=\"" +
        svgChar.bb.y1 +
        "\" x=\"" +
        svgChar.bb.x1 +
        "\">" +
        svgChar.content +
        "</text>"



}
