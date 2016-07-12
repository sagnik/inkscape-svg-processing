package edu.psu.sagnik.research.inkscapesvgprocessing.test

/**
 * Created by szr163 on 11/8/15.
 */
import edu.psu.sagnik.research.inkscapesvgprocessing.impl.{SVGTextExtract}
import org.scalatest.FunSpec

import scala.reflect.io.File


class SVGTextOutputTest extends FunSpec {


  describe("testing the SVG output by printing") {
    import edu.psu.sagnik.research.inkscapesvgprocessing.test.DataLocation._
    it("should print the path info.") {
      //val results=SVGPathExtract(svgFileLoc)
      val results=SVGTextExtract(svgFileLoc)
      val text=results.map(x=>x.charSVGString).foldLeft("")((a,b)=> a+"\n"+b)
      val height=990
      val width=765
      val svgStart="<?xml version=\"1.0\" standalone=\"no\"?>\n\n<svg height=\"" +
        height +
        "\" width=\"" +
        width +
        "\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">"+
        "\n"
      val svgEnd="\n</svg>"

      val svgLoc="src/test/resources/page_006_cortina_test.svg"
      File(svgLoc).writeAll(svgStart+"\n"+text+"\n"+svgEnd)
      //results.foreach(x=>println(x.pContent))
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
