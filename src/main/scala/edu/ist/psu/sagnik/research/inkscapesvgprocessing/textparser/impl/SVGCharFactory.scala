package edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.impl.SVGTextExtract
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.model.{TextPath, SVGChar}

/**
 * Created by sagnik on 12/14/15.
 */
object SVGCharFactory {

  def apply(textPath:TextPath):Seq[SVGChar]={
    val tspElems=scala.xml.XML.loadString(textPath.tPContent) \\ "tspan"
    val tsps=tspElems.map(x=>getTSpanObject(x.text,textPath))
    Seq.empty[SVGChar]
  }

  def getTSpanObject(tsps:String,tp:TextPath)={println(s"[tspan string]: ${tsps} [textpath id]: ${tp.id}"); "abc"}

  def main(args: Array[String]):Unit={
    val dataLoc="src/test/resources/pg_0006.svg"
    val textpaths=SVGTextExtract(dataLoc)
  }
}
