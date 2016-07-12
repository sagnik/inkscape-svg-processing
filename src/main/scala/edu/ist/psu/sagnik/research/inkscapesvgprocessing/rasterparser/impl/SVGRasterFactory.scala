package edu.psu.sagnik.research.inkscapesvgprocessing.rasterparser.impl

import edu.psu.sagnik.research.inkscapesvgprocessing.impl.{SVGRasterExtract, SVGTextExtract}
import edu.psu.sagnik.research.inkscapesvgprocessing.rasterparser.model.{SVGRaster, SVGRasterIm}

/**
 * Created by sagnik on 12/24/15.
 */
object SVGRasterFactory {

  def main(args: Array[String]):Unit={
    val dataLoc="src/test/resources/pdffigures/page_2.svg"
    val textpaths=SVGRasterExtract(dataLoc)
  }

}
