package edu.ist.psu.sagnik.research.svgxmlprocess.test

/**
 * Created by szr163 on 11/8/15.
 */
import edu.ist.psu.sagnik.research.svgxmlprocess.impl.ProcessXML
import org.scalatest.FunSpec

class SVGPathOutputTest extends FunSpec {


  describe("testing the SVG output by printing") {
    import edu.ist.psu.sagnik.research.svgxmlprocess.test.DataLocation._
    it("should print the path info.") {
      ProcessXML(svgFileLoc).foreach(x=>println(s"[path content]: ${x.pContent}, [path id]: ${x.id}, [path ops]: ${x.pOps}"))
    }
  }

}
