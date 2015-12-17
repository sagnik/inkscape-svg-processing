package edu.ist.psu.sagnik.research.inkscapesvgprocessing.impl

import breeze.linalg.DenseMatrix
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.model.SVGChar

/**
 * Created by sagnik on 12/17/15.
 */
class SVGCharBB {
  def apply(svgChar: SVGChar):SVGChar={
    val groups=svgChar.groups
    val charBBwoTransforms=svgChar.bb
    val transforms=groups.map(x=>x.transformOps).flatten ++ //the ordering is important
      svgChar.transformOps

    val finalTMatrix=transforms.map(a=>a.matrix)
      .foldLeft(DenseMatrix.eye[Float](3))((a,b)=>a*b)
    svgChar.copy(charSVGString = addTransforms(svgChar,finalTMatrix), bb=new SVGPathBB.changedBB(charBBwoTransforms,finalTMatrix))
  }

}
