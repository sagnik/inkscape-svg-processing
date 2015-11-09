package edu.ist.psu.sagnik.research.svgxmlprocess.model

import scala.xml.{Node, Elem}

/**
 * Created by szr163 on 11/8/15.
 */
case class point(x:Float,y:Float)

case class transformOp(operator:String,operand: Seq[Float])

case class pathOp(operator:String, operand:Seq[point] )

case class SVGPath(id:String, pdContent:String, pContent:String, pOps:Seq[pathOp], gIds:Seq[String])

case class Group(gContent:String,transformOps:Seq[transformOp])

//case class SVGPathwithBB(pContent:String, )