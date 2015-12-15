package edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.model

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.{Rectangle, SVGGroup}
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.model.TransformCommand

/**
 * Created by szr163 on 12/13/15.
 */

// TODO: while we only see tspans inside a text and not any characters, ideally text path can contain chars.

case class TextPath(id:String,styleString:String,transformOps:Seq[TransformCommand],
                     groups:Seq[SVGGroup],tPContent:String)

case class TSpanPath(id:String,x:String,y:String,charString:List[Char],textPath:TextPath)

case class SVGChar(content:Char,bb:Rectangle,charSVGString:String,
                   styleString:String,transformOps:Seq[TransformCommand],groups:Seq[SVGGroup])
