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
      val results=ProcessXML(svgFileLoc)
      /*
      results.foreach(x=>println(s"[path content]: ${x.pContent}, [path id]: ${x.id}, [groupIds]: ${x.gIds}"))

      assert(results.find(p=>p.id=="p1").head.gIds.isEmpty)
      assert(results.find(p=>p.id=="p2").head.gIds.isEmpty)
      assert(List("g1","g2").toIndexedSeq.equals(results.find(p=>p.id=="p3").head.gIds))
      assert(List("g1").toIndexedSeq.equals(results.find(p=>p.id=="p4").head.gIds))
      assert(List("g3").toIndexedSeq.equals(results.find(p=>p.id=="p5").head.gIds))
      assert(List("g4").toIndexedSeq.equals(results.find(p=>p.id=="p5").head.gIds))
      */
    }
  }

}
