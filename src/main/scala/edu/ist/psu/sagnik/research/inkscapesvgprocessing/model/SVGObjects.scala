package edu.ist.psu.sagnik.research.inkscapesvgprocessing.model

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.model.PathCommand
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.model.TransformCommand

import scala.xml.{Node, Elem}

/**
 * Created by szr163 on 11/8/15.
 */
/* Transform and path operations: 1. On group it is usually translate etc. 2. path operation is LMQCR etc.*/
case class groupTransformOp(operator:String,operand: Seq[Float])

case class PathGroups(path:Node,gIds:Seq[String])

case class TextGroups(textNode:Node,gIds:Seq[String])

case class ImageGroups(raster:Node,gIds:Seq[String])

/* Data models for SVG paths*/
case class SVGPath(id:String, pdContent:String, pContent:String, pOps:Seq[PathCommand],
                   groups:Seq[SVGGroup], transformOps:Seq[TransformCommand], bb:Option[Rectangle])

/* Data models for SVG groups*/
case class SVGGroup(id:String, gtContent:String, gContent:String, transformOps:Seq[TransformCommand])
