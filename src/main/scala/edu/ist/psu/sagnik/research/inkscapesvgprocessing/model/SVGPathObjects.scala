package edu.ist.psu.sagnik.research.inkscapesvgprocessing.model

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.model.PathCommand
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.model.TransformCommand

import scala.xml.{Node, Elem}

/**
 * Created by szr163 on 11/8/15.
 */
case class point(x:Float,y:Float)

/* Transform and path operations: 1. On group it is usually translate etc. 2. path operation is LMQCR etc.*/
case class groupTransformOp(operator:String,operand: Seq[Float])

/* Data models for SVG paths*/
case class SVGPath(id:String, pdContent:String, pContent:String, pOps:Seq[PathCommand],
                   gIds:Seq[String], transformOps:Seq[TransformCommand], bb:Option[Rectangle])

/* Data models for SVG groups*/
case class SVGGroup(id:String, gtContent:String, gContent:String, transformOps:Seq[TransformCommand])


//case class SVGPathwithBB(pContent:String, )