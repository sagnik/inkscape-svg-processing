package edu.ist.psu.sagnik.research.inkscapesvgprocessing.rasterparser.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.impl.{SVGRasterExtract, SVGTextExtract}

/**
 * Created by sagnik on 12/24/15.
 */
object SVGRasterFactory {
  def main(args: Array[String]):Unit={
    val dataLoc="src/test/resources/pdffigures/page_2.svg"
    val textpaths=SVGRasterExtract(dataLoc)
  }

}
