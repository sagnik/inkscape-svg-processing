package edu.ist.psu.sagnik.research.inkscapesvgprocessing.writer.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.{Rectangle, SVGPath}
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.model.SVGChar
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.writer.model.{CharStyle, SVGCharJson, PathStyle, SVGPathJson}

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
            stroke= returnPattern(x.pContent,"stroke:"),
            strokeWidth = if ("-1".equals(returnPattern(x.pContent,"stroke-width:"))) -1f else returnPattern(x.pContent,"stroke-width:").toFloat ,
            strokeLinecap = returnPattern(x.pContent,"stroke-linecap:"),
            strokeLinejoin = returnPattern(x.pContent,"stroke-linejoin:"),
            strokeMiterlimit = returnPattern(x.pContent,"stroke-miterlimit:"),
            strokeDasharray = returnPattern(x.pContent,"stroke-dasharray:"),
            strokeOpacity = if ("-1".equals(returnPattern(x.pContent,"stroke-opacity:"))) -1f else returnPattern(x.pContent,"stroke-width:").toFloat
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
            fontSize = if ("-1".equals(returnPattern(x.styleString,"font-size:"))) -1f else returnPattern(x.styleString,"font-size:").substring(0,returnPattern(x.styleString,"font-size:").length-2).toFloat,
            fontFamily=returnPattern(x.styleString,"font-family:"),
            inkscapeFontSpecification=returnPattern(x.styleString,"-inkscape-font-specification:"),
            writingMode = returnPattern(x.styleString,"writing-mode:"),
            fill = returnPattern(x.styleString,"fill:"),
            fillOpacity = if ("-1".equals(returnPattern(x.styleString,"fill-opacity:"))) -1f else returnPattern(x.styleString,"fill-opacity:").toFloat,
            fillRule = returnPattern(x.styleString,"fill-rule:"),
            stroke = if (x.styleString.contains("stroke")) x.styleString.split("stroke")(1).split(" ")(0) else "-1"

          ),
          charWholeString = x.charSVGString
        )
        )
        case None => Seq.empty[SVGPathJson]
      }


  }
  def returnPattern(pContent:String,s:String)=
    if (pContent.contains(s)) pContent.split(s)(1).split(";")(0) else "-1"
}
