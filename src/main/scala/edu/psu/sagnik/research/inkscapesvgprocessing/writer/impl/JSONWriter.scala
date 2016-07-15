package edu.psu.sagnik.research.inkscapesvgprocessing.writer.impl

import edu.psu.sagnik.research.inkscapesvgprocessing.model.{Rectangle, SVGPath}
import edu.psu.sagnik.research.inkscapesvgprocessing.textparser.model.SVGChar
import edu.psu.sagnik.research.inkscapesvgprocessing.writer.model.{CharStyle, SVGCharJson, PathStyle, SVGPathJson}

/*
TODO: This is wrong, see edu.psu.sagnik.research.linegraphcurveseparation.impl.SVGPathExtract for the correct implementation of style info.
*/

/**
 * Created by szr163 on 3/1/16.
 */
object JSONWriter {
  def apply(jsonLoc:String,svgPaths:Option[Seq[SVGPath]],svgChars:Option[Seq[SVGChar]]): Unit={
    val svgJsonPaths=
      svgPaths match{
        case Some(svgPaths) => svgPaths.map(x=>SVGPathJson(
          bb=x.bb match{case Some(bb) => bb; case None => Rectangle(0f,0f,0f,0f);},
          pathDString=x.pdContent,
          pathStyle = PathStyle(

            fill= returnPattern(x.pContent,"fill:"),
            fillRule = returnPattern(x.pContent,"fill-rule:"),
            stroke= returnPattern(x.pContent,"stroke:"),
            strokeWidth = returnPattern(x.pContent,"stroke-width:"),
            strokeLinecap = returnPattern(x.pContent,"stroke-linecap:"),
            strokeLinejoin = returnPattern(x.pContent,"stroke-linejoin:"),
            strokeMiterlimit = returnPattern(x.pContent,"stroke-miterlimit:"),
            strokeDasharray = returnPattern(x.pContent,"stroke-dasharray:"),
            strokeOpacity = returnPattern(x.pContent,"stroke-opacity:"," "),
            fillOpacity = returnPattern(x.pContent,"fill-opacity:"," "),
            strokeDashoffset = returnPattern(x.pContent,"stroke-dashoffset:")
          ),
          pathWholeString = x.pContent
        )
        )
        case None => Seq.empty[SVGPathJson]
      }
    val sVGJsonChars=
      svgChars match{
        case Some(svgChars) => svgChars.map(x=>SVGCharJson(
          bb=x.bb,
          c=x.content,
          charStyle=CharStyle(
            fontVariant = returnPattern(x.styleString,"font-variant:"),
            fontWeight = returnPattern(x.styleString,"font-weight:"),
            fontSize = returnPattern(x.styleString,"font-size:") match {case Some(s) => Some(s.substring(0,s.length-2)) case _ => None},
            fontFamily=returnPattern(x.styleString,"font-family:"),
            inkscapeFontSpecification=returnPattern(x.styleString,"-inkscape-font-specification:"),
            writingMode = returnPattern(x.styleString,"writing-mode:"),
            fill = returnPattern(x.styleString,"fill:"),
            fillOpacity = returnPattern(x.styleString,"fill-opacity:"),
            fillRule = returnPattern(x.styleString,"fill-rule:"),
            stroke = returnPattern(x.styleString,"fill-rule:"," ")
          ),
          charWholeString = x.charSVGString
        )
        )
        case None => Seq.empty[SVGPathJson]
      }


  }
  def returnPattern(pContent:String,s:String)=
    if (pContent.contains(s)) Some(pContent.split(s)(1).split(";")(0)) else None

  def returnPattern(pContent:String,s1:String, s2:String)=
    if (pContent.contains(s1) && pContent.contains(s2)) Some(pContent.split(s1)(1).split(s2)(0)) else None

}
