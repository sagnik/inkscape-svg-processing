package edu.psu.sagnik.research.inkscapesvgprocessing.impl

import breeze.linalg.DenseMatrix
import edu.psu.sagnik.research.inkscapesvgprocessing.model.Rectangle
import edu.psu.sagnik.research.inkscapesvgprocessing.rasterparser.model.{SVGRaster, SVGRasterIm}
import edu.psu.sagnik.research.inkscapesvgprocessing.textparser.model.SVGChar

/**
 * Created by sagnik on 12/24/15.
 */
object SVGRasterBB {
  def apply(rasterIm: SVGRasterIm):SVGRaster= {
    val groups = rasterIm.groups
    val rasterBBwoTransforms = Rectangle(
      rasterIm.x,
      rasterIm.y,
      rasterIm.x + rasterIm.width,
      rasterIm.y + rasterIm.height
    )

    val transforms = groups.map(x => x.transformOps).flatten ++ //the ordering is important
      rasterIm.transformOps

    val finalTMatrix = transforms.map(a => a.matrix)
      .foldLeft(DenseMatrix.eye[Float](3))((a, b) => a * b)

    val rasterBB = SVGPathBB.changedBB(rasterBBwoTransforms, finalTMatrix) match {
      case Some(bb) => bb
      case None => Rectangle(0, 0, 0, 0)
    }

    SVGRaster(
      id = rasterIm.id,
      bb = rasterBB,
      imageString = addTransforms(rasterIm,finalTMatrix)
    )
  }

  def addTransforms(rIm: SVGRasterIm,fM: DenseMatrix[Float]):String=
   "<image id=\"" +
     rIm.id +
     "\" xlink:href=\"" +
     rIm.imageDString +
     "\" " +
     "transform=" +
     "\"matrix(" +
     fM(0,0).toString+","+ //a
     fM(1,0).toString+","+ //b
     fM(0,1).toString+","+ //c
     fM(1,1).toString+","+ //d
     fM(0,2).toString+","+ //e
     fM(1,2).toString+ //f
     ")\" " +
     "preserveAspectRatio=\"none\" height=\"" +
     rIm.height +
     "\" width=\"" +
     rIm.width +
     "\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" />"

}
