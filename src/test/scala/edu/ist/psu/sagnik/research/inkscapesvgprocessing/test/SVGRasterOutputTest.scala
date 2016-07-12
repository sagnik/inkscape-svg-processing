package edu.psu.sagnik.research.inkscapesvgprocessing.test

import edu.psu.sagnik.research.inkscapesvgprocessing.impl.{SVGRasterExtract, SVGPathExtract}
import org.scalatest.FunSpec

import scala.reflect.io.File

/**
 * Created by sagnik on 12/24/15.
 */
class SVGRasterOutputTest extends FunSpec {

  describe("testing the SVG output by printing") {
    import edu.psu.sagnik.research.inkscapesvgprocessing.test.DataLocation._
    it("should print the path info.") {
      val results = SVGRasterExtract(svgFileLoc)
      val text=results.map(x=>x.imageString).foldLeft("")((a,b)=> a+"\n"+b)
      val height=990
      val width=765
      val svgStart="<?xml version=\"1.0\" standalone=\"no\"?>\n\n<svg height=\"" +
        height +
        "\" width=\"" +
        width +
        "\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">"+
        "\n"
      val svgEnd="\n</svg>"

      val svgLoc="src/test/resources/pdffigures/page_2_test_raster.svg"
      File(svgLoc).writeAll(svgStart+"\n"+text+"\n"+svgEnd)
    }
  }
}
