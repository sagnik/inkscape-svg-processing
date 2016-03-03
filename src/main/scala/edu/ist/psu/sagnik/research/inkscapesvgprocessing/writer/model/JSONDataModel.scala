package edu.ist.psu.sagnik.research.inkscapesvgprocessing.writer.model

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.Rectangle

/**
 * Created by szr163 on 3/1/16.
 */

case class PathStyle(fill:String,
                     stroke:String,
                     strokeWidth:Float,
                     strokeLinecap:String,
                     strokeLinejoin:String,
                     strokeMiterlimit:String,
                     strokeDasharray:String,
                     strokeOpacity:Float)

case class CharStyle(fontVariant:String,
                     fontWeight:String,
                     fontSize:Float,
                     fontFamily:String,
                     inkscapeFontSpecification:String,
                     writingMode:String,
                     fill:String,
                     fillOpacity:Float,
                     fillRule:String,
                     stroke:String)

case class SVGPathJson(bb:Rectangle,pathDString:String,pathStyle:PathStyle,pathWholeString:String)
case class SVGCharJson(bb:Rectangle,c:Char,charStyle:CharStyle,charWholeString:String)
