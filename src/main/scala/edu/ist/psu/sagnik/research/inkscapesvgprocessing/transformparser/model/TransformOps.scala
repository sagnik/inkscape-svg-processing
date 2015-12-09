package edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.model

/**
 * Created by sagnik on 12/9/15.
 */
import breeze.linalg._

trait TransformCommand {
  def command:String
  def args:Any
  //def getMatrix[A<:TransformCommand](op:A):DenseMatrix[Float]
}

case class MatrixOpArg(a:Float,b:Float,c:Float,d:Float,e:Float,f:Float)
case class MatrixOp(command:String,args:Any) extends TransformCommand

case class TranslateOpArg(tX:Float, tY:Option[Float])
case class TranslateOp(command:String,args:Any) extends TransformCommand

case class ScaleOpArg(sX:Float, sY:Option[Float])
case class ScaleOp(command:String, args:Any) extends TransformCommand

case class RotateOpArg(rAngle:Float, cX:Option[Float], cYOption:Option[Float])
case class RotateOp(command:String,args:Any) extends TransformCommand

case class SkewXOpArg(skAngle:Float)
case class SkewXOp(command:String,args:Any) extends TransformCommand

case class SkewYOpArg(skAngle:Float)
case class SkewYOp(command:String, args:Any) extends TransformCommand