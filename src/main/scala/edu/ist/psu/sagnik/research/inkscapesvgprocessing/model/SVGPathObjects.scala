package edu.ist.psu.sagnik.research.inkscapesvgprocessing.model

import scala.xml.{Node, Elem}

/**
 * Created by szr163 on 11/8/15.
 */
case class point(x:Float,y:Float)

/* Transform and path operations: 1. On group it is usually translate etc. 2. path operation is LMQCR etc.*/
case class groupTransformOp(operator:String,operand: Seq[Float])
case class pathOp(operator:String, operand:Seq[point] )

/* Data models for SVG paths*/
case class SVGPath(id:String, pdContent:String, pContent:String, pOps:Seq[pathOp], gIds:Seq[String])


/* Data models for SVG groups*/
case class SVGGroup(id:String, gtContent:String, gContent:String, transformOps:Seq[groupTransformOp])


//case class SVGPathwithBB(pContent:String, )