package edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.model

/**
 * Created by sagnik on 12/9/15.
 */
import breeze.linalg._

case class TransformCommand(command:String,matrix:DenseMatrix[Float])

case class MatrixOpArg(a:Float,b:Float,c:Float,d:Float,e:Float,f:Float)
case class MatrixOp(command:String,args:MatrixOpArg)

case class TranslateOpArg(tX:Float, tY:Option[Float])
case class TranslateOp(command:String,args:TranslateOpArg)

case class ScaleOpArg(sX:Float, sY:Option[Float])
case class ScaleOp(command:String, args:ScaleOpArg)

case class RotateOpArg(rAngle:Float, translateArg:Option[(Float,Float)])
case class RotateOp(command:String,args:RotateOpArg)

case class SkewXOpArg(skAngle:Float)
case class SkewXOp(command:String,args:SkewXOpArg)

case class SkewYOpArg(skAngle:Float)
case class SkewYOp(command:String, args:SkewYOpArg)