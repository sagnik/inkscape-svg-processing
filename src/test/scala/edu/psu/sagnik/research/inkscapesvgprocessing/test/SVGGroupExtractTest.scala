package edu.psu.sagnik.research.inkscapesvgprocessing.test

/**
 * Created by sagnik on 11/11/15.
 */

import edu.psu.sagnik.research.inkscapesvgprocessing.impl.GroupExtract
import org.scalatest.FunSpec

class SVGGroupExtractTest extends FunSpec {


  describe("testing the SVG output by printing") {
    import edu.psu.sagnik.research.inkscapesvgprocessing.test.DataLocation._
    it("should print the group info.") {
      val results=GroupExtract(svgFileLoc)
      results.foreach(x=>println(s"[group id]: ${x.id} [group transform content]: ${x.gtContent}"))
    }
  }

}