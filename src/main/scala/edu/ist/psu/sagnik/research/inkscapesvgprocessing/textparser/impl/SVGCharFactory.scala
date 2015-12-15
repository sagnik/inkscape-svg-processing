package edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.impl.SVGTextExtract
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.Rectangle
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.model.{TSpanPath, TextPath, SVGChar}

import scala.xml.Node

/**
 * Created by sagnik on 12/14/15.
 */
object SVGCharFactory {

  def apply(textPath:TextPath):Seq[SVGChar]={
    val tspElems=scala.xml.XML.loadString(textPath.tPContent) \\ "tspan"
    val tsps=tspElems.map(x=>getTSpanObject(x.text,x,textPath))
    val chars=TSpanToChar(tsps,0f,0f,Seq.empty[SVGChar])
    //tsps.foreach(a=>println(a.id,a.charString,a.tspanStyle,a.x,a.y))
    Seq.empty[SVGChar]
  }

  def getTSpanObject(tsps:String,tspWhole:Node,tp:TextPath):TSpanPath=
    TSpanPath(
     id=tspWhole\@"id",
     x= (tspWhole\@"x"), //TODO: possible exceptions
     y= (tspWhole\@"y"),
     charString=tspWhole.text.toCharArray.toList,
     textPath=tp
     //tspanStyle = tspWhole \@ "style" TODO: tspan can also contain style, not considering that now.
    )

  def TSpanToChar(ts:Seq[TSpanPath],textXPosition:Float,textYPosition:Float, chars:Seq[SVGChar]):Seq[SVGChar]=
  ts match{
    case Nil => Seq.empty[SVGChar]
    case tsp::Nil => chars++(OneTspanToCharSeq(tsp,textXPosition,textYPosition)._1)
    case tsp :: tsps =>{
      val results=OneTspanToCharSeq(tsp,textXPosition,textYPosition)
      TSpanToChar(tsps,results._2,results._3,chars++results._1)
    }
  }

  def OneTspanToCharSeq(tsp:TSpanPath,txp:Float,typ:Float)= {
    val tspx=if ("".equals(tsp.x)) Seq.empty[Float].toList else (tsp.x.split("\\s+")).toList.map(a=>a.toFloat)
    val tspy=if ("".equals(tsp.y)) Seq.empty[Float].toList else (tsp.y.split("\\s+")).toList.map(a=>a.toFloat)
    if (tspx.isEmpty || tspy.isEmpty) (Seq.empty[SVGChar], txp, typ) //TODO: x,y positions can actually come from the text tag itself
    if (!(tspx.length==tsp.charString.length)||(tspy.length==tsp.charString.length))
      (Seq.empty[SVGChar], txp, typ)
    else if (tspy.length==tsp.charString.length){
      val styleString=tsp.textPath.styleString
      val widthHeight=if ("".equals(styleString)) 10f else styleString.split("font-size:")(1).split("px")(0).toFloat //TODO: possible exception?
      tsp.charString.map(x=>
        SVGChar(
          content=x,
          bb=Rectangle(
          
          )
        )
      )
    }

    (Seq.empty[SVGChar], 0f, 0f)
  }
  def main(args: Array[String]):Unit={
    val dataLoc="src/test/resources/pg_0006.svg"
    val textpaths=SVGTextExtract(dataLoc)
  }
}
