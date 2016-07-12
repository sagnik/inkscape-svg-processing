package edu.psu.sagnik.research.inkscapesvgprocessing.test

/**
 * Created by sagnik on 12/23/15.
 */

import edu.psu.sagnik.research.inkscapesvgprocessing.impl.{SVGPathExtract}
import org.scalatest.FunSpec

class SVGPathOutputTest extends FunSpec {

  describe("testing the SVG output by printing") {
    import edu.psu.sagnik.research.inkscapesvgprocessing.test.DataLocation._
    it("should print the path info.") {
      val results=SVGPathExtract(svgFileLoc)
      results.foreach(x=>println(x.pContent))
/*
      results.foreach(
        x=>println(s"[path id]: ${x.id}, [pconent]: ${x.pContent}" +
          s"[pathbb ]: ${
            x.bb match{
              case Some(bb) => (bb.x1,bb.y1,bb.x2-bb.x1,bb.y2-bb.y1)
              case None => None
            }
          }" +
          s"")
      )
*/

      //results.filter(p=>"path170".equals(p.id)).foreach(x=>println(s"[path content]: ${x.pdContent} [pathcommands]: ${x.pOps} [pathbb] ${x.bb}"))

      /*assert(results.find(p=>p.id=="p1").head.gIds.isEmpty)
      assert(results.find(p=>p.id=="p2").head.gIds.isEmpty)
      assert(List("g1","g2").toSet.equals(results.find(p=>"p3".equals(p.id)).head.gIds.toSet))
      assert(List("g1").toSet.equals(results.find(p=>"p4".equals(p.id)).head.gIds.toSet))
      assert(List("g3").toSet.equals(results.find(p=>"p5".equals(p.id)).head.gIds.toSet))
      assert(List("g4").toSet.equals(results.find(p=>"p6".equals(p.id)).head.gIds.toSet))
*/
    }
  }

}
