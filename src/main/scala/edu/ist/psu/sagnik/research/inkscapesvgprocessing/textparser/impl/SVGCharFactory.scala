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
    //tsps.foreach(a=>println(a.id,a.textPath.groups.map(x=>x.id)))
    chars.foreach(x=>println(x.content,x.bb))
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

  //TODO: lot's of assumptions here
  def OneTspanToCharSeq(tsp:TSpanPath,txp:Float,typ:Float)= {
    val tspx=if ("".equals(tsp.x)) Seq.empty[Float].toList else (tsp.x.split("\\s+")).toList.map(a=>a.toFloat)
    val tspy=if ("".equals(tsp.y)) Seq.empty[Float].toList else (tsp.y.split("\\s+")).toList.map(a=>a.toFloat)
    if (tspx.isEmpty || tspy.isEmpty) {println("tspan x or y empty"); (Seq.empty[SVGChar], txp, typ)} //TODO: x,y positions can actually come from the text tag itself
    //println(s"[tspx length] ${tspx.length} [tspy length]: ${tspy.length} [no chars]: ${tsp.charString.length}")
    if (!(tspx.length==tsp.charString.length) && !(tspy.length==tsp.charString.length))
      {println("tspan x or tspan y doesn't have same number of elements as no. of chars"); (Seq.empty[SVGChar], txp, typ)}
    else if ((tspx.length>1 && tspy.length>1))
      {println("both tspanx and tspan y contains more than one char");(Seq.empty[SVGChar], txp, typ)}
    else {
      val charSeq=
        if (tspy.length==tsp.charString.length){
          val styleString=tsp.textPath.styleString
          val widthHeight=if ("".equals(styleString)) 10f else styleString.split("font-size:")(1).split("px")(0).toFloat //TODO: possible exception?
          val x=tspx(0)
          tsp.charString.zipWithIndex.map(c=>
            SVGChar(
              content=c._1,
              bb=Rectangle(
                x1=x,
                y1=typ+tspy(c._2),
                x2=x+widthHeight,
                y2=typ+tspy(c._2)+widthHeight
              ),
              styleString=styleString,
              charSVGString = "",
              transformOps = tsp.textPath.transformOps,
              groups=tsp.textPath.groups
            )
          )
        }
        else {
          val styleString=tsp.textPath.styleString
          val widthHeight=if ("".equals(styleString)) 10f else styleString.split("font-size:")(1).split("px")(0).toFloat //TODO: possible exception?
          val y=tspy(0)
          tsp.charString.zipWithIndex.map(c=>
            SVGChar(
              content=c._1,
              bb=Rectangle(
                x1=txp+tspx(c._2),
                y1=y,
                x2=txp+tspx(c._2)+widthHeight,
                y2=y+widthHeight
              ),
              styleString=styleString,
              charSVGString = ???,
              transformOps = tsp.textPath.transformOps,
              groups=tsp.textPath.groups
            )
          )
        }
      (charSeq,charSeq.last.bb.x2,charSeq.last.bb.y2)
    }
  }

  def main(args: Array[String]):Unit={
    val dataLoc="src/test/resources/pg_0006.svg"
    val textpaths=SVGTextExtract(dataLoc)
  }
}
