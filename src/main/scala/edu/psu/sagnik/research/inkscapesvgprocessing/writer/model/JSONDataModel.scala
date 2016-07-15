package edu.psu.sagnik.research.inkscapesvgprocessing.writer.model

import edu.psu.sagnik.research.inkscapesvgprocessing.model.Rectangle

/**
 * Created by szr163 on 3/1/16.
 */

case class PathStyle(
                      fill:Option[String],
                      fillRule:Option[String],
                      fillOpacity:Option[String],
                      stroke:Option[String],
                      strokeWidth:Option[String],
                      strokeLinecap:Option[String],
                      strokeLinejoin:Option[String],
                      strokeMiterlimit:Option[String],
                      strokeDasharray:Option[String],
                      strokeDashoffset:Option[String],
                      strokeOpacity:Option[String]
                    )

case class CharStyle(fontVariant:Option[String],
                     fontWeight:Option[String],
                     fontSize:Option[String],
                     fontFamily:Option[String],
                     inkscapeFontSpecification:Option[String],
                     writingMode:Option[String],
                     fill:Option[String],
                     fillOpacity:Option[String],
                     fillRule:Option[String],
                     stroke:Option[String])

case class SVGPathJson(bb:Rectangle,pathDString:String,pathStyle:PathStyle,pathWholeString:String)
case class SVGCharJson(bb:Rectangle,c:Char,charStyle:CharStyle,charWholeString:String)
