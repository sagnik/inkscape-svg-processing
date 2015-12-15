package edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.impl.SVGTextExtract
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.model.{TSpanPath, TextPath, SVGChar}

import scala.xml.Node

/**
 * Created by sagnik on 12/14/15.
 */
object SVGCharFactory {

  def apply(textPath:TextPath):Seq[SVGChar]={
    val tspElems=scala.xml.XML.loadString(textPath.tPContent) \\ "tspan"
    val tsps=tspElems.map(x=>getTSpanObject(x.text,x,textPath))
    tsps.foreach(a=>println(a.id,a.charString,a.tspanStyle,a.x,a.y))
    Seq.empty[SVGChar]
  }

  def getTSpanObject(tsps:String,tspWhole:Node,tp:TextPath):TSpanPath=
    TSpanPath(
     id=tspWhole\@"id",
     x= ((tspWhole\@"x").split("\\s+")).toList.map(a=>a.toFloat), //TODO: possible exceptions
     y= ((tspWhole\@"y").split("\\s+")).toList.map(a=>a.toFloat),
     charString=tspWhole.text,
     textPath=tp,
     tspanStyle = tspWhole \@ "style"
    )


  def main(args: Array[String]):Unit={
    val dataLoc="src/test/resources/pg_0006.svg"
    val textpaths=SVGTextExtract(dataLoc)
  }
}
