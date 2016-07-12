package edu.psu.sagnik.research.inkscapesvgprocessing.textparser.impl

import edu.psu.sagnik.research.inkscapesvgprocessing.impl.{SVGCharBB, SVGTextExtract}
import edu.psu.sagnik.research.inkscapesvgprocessing.model.Rectangle
import edu.psu.sagnik.research.inkscapesvgprocessing.textparser.model.{TSpanPath, TextPath, SVGChar}

import scala.xml.Node

/**
 * Created by sagnik on 12/14/15.
 */
object SVGCharFactory {

  def apply(textPath:TextPath):Seq[SVGChar]=
    TSpanToChar(
      (scala.xml.XML.loadString(textPath.tPContent) \\ "tspan")
        .map(x=>getTSpanObject(x.text,x,textPath)),
      0f,
      0f,
      Seq.empty[SVGChar]
    ).map(x=>SVGCharBB(x))



  def getTSpanObject(tsps:String,tspWhole:Node,tp:TextPath):TSpanPath=
    TSpanPath(
      id=tspWhole\@"id",
      x= (tspWhole\@"x"), //TODO: possible exceptions
      y= (tspWhole\@"y"),
      charString=tspWhole.text.toCharArray.toList,
      textPath=tp,
      tspanStyleString = tspWhole \@ "style" //TODO: tspan can also contain style, not considering that now.
    )

  def TSpanToChar(ts:Seq[TSpanPath],textXPosition:Float,textYPosition:Float, chars:Seq[SVGChar]):Seq[SVGChar]=
    ts match{
      case Nil => Seq.empty[SVGChar]
      case tsp::Nil => chars++OneTspanToCharSeq(tsp,textXPosition,textYPosition)
      case tsp :: tsps => TSpanToChar(tsps,0,0,chars++OneTspanToCharSeq(tsp,textXPosition,textYPosition))
    }


  //TODO: lot's of assumptions here
  def OneTspanToCharSeq(tsp:TSpanPath,txp:Float,typ:Float):Seq[SVGChar]= {
    val tspx=if ("".equals(tsp.x)) Seq.empty[Float].toList else (tsp.x.split("\\s+")).toList.map(a=>a.toFloat)
    val tspy=if ("".equals(tsp.y)) Seq.empty[Float].toList else (tsp.y.split("\\s+")).toList.map(a=>a.toFloat)
    if (tspx.isEmpty || tspy.isEmpty) {println("tspan x or y empty"); (Seq.empty[SVGChar], txp, typ)} //TODO: x,y positions can actually come from the text tag itself
    if (!(tspx.length==tsp.charString.length) && !(tspy.length==tsp.charString.length))
    {println("tspan x or tspan y doesn't have same number of elements as no. of chars"); Seq.empty[SVGChar]}
    else if ((tspx.length>1 && tspy.length>1))
    {println("both tspanx and tspan y contains more than one char");Seq.empty[SVGChar]}
    else {
      //TODO: we don't actually have a proper implementation here, because width can't properly be determined from the font size
      val charSeq=
        if (tspy.length==tsp.charString.length){
          val styleString=if (tsp.textPath.styleString.isEmpty) tsp.tspanStyleString else tsp.textPath.styleString
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
          val styleString=if (tsp.textPath.styleString.isEmpty) tsp.tspanStyleString else tsp.textPath.styleString
          val height=if ("".equals(styleString)) 10f else styleString.split("font-size:")(1).split("px")(0).toFloat //TODO: possible exception?
          val y=tspy(0)
          tsp.charString.zipWithIndex.map(c=>
            SVGChar(
              content=c._1,
              bb=Rectangle(
                x1=txp+tspx(c._2),
                y1=y,
                x2=if (c._2<tsp.charString.length-1) txp+tspx(c._2+1)-0.1f else txp+tspx(c._2)+5f,
                y2=y+height
              ),
              styleString=styleString,
              charSVGString = "",
              transformOps = tsp.textPath.transformOps,
              groups=tsp.textPath.groups
            )
          )
        }
      charSeq
    }
  }

  def main(args: Array[String]):Unit={
    val dataLoc="src/test/resources/pg_0006.svg"
    val textpaths=SVGTextExtract(dataLoc)
  }
}
