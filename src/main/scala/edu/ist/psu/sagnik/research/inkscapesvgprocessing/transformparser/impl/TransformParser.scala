package edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.impl

/**
 * Created by sagnik on 12/9/15.
 */

import breeze.linalg._
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.model._
import scala.util.parsing.combinator.RegexParsers

class TransformParser extends RegexParsers {

  override def skipWhitespace = false

  def wsp: Parser[String]= "\\s|\\n".r ^^ {_.toString} //TODO: this needs to be changed to accomodate #x20, #xD, #xA, #x9
  //see http://www.xmlplease.com/normalized for an example.

  def digit:Parser[String]="""0|1|2|3|4|5|6|7|8|9""".r ^^ {_.toString}

  def digit_sequence: Parser[String]=digit~rep(digit) ^^{case d~ds => if (ds.isEmpty) d else  d+ds.mkString("")} // digit~digit_sequence ^^{case d~ds => println(s"digit ${d} digit sequence: ${ds}"); ds.toString}|digit^^{_.toString} //

  def sign:Parser[String]="""\+|\-""".r ^^{_.toString}

  def exponent:Parser[Double]="""e|E""".r~opt(sign)~digit_sequence ^^ { //TODO: Not handling cases such as e!234, which are definitely wrong
    case ep~Some(sign)~digitSeq =>
      if ("-".equals(sign)) scala.math.exp(-1*digitSeq.toDouble) else scala.math.exp(digitSeq.toDouble)
    case _ => 1d
  }

  def fractional_constant:Parser[String]=
    opt(digit_sequence)~"""\.""".r~digit_sequence ^^{
      case Some(ds1)~dot~ds2 => ds1+"."+ds2
      case None~dot~ds2=> ds2
      case _ => ""
    } |
      digit_sequence~"""\.""".r  ^^{
        case ds~dot=>ds.toString
        case _ => ""
      }

  def floating_point_constant: Parser[String]=
    fractional_constant~opt(exponent)^^{
      case fc~Some(ep)=>(fc.toDouble*ep).toString
      case fc~None=>fc
    } |
      digit_sequence~exponent ^^{
        case ds~ep=>(ds.toDouble*ep).toString
      }

  def integer_constant:Parser[String]= digit_sequence^^{_.toString}

  def comma: Parser[String] = """,""".r ^^ {_.toString}

  def comma_wsp: Parser[String] = wsp~rep(wsp)~opt(comma)~rep(wsp)^^ { _.toString} | comma~rep(wsp)^^{_.toString}

  def flag:Parser[Double] = """0|1""".r ^^{_.toDouble}

  def number:Parser[Double]=
    opt(sign)~floating_point_constant^^{
      case Some(s)~fc => if ("-".equals(s)) ("-"+fc).toDouble else fc.toDouble
      case None~fc => fc.toDouble
    } |
      opt(sign)~integer_constant^^{
        case Some(s)~ic => if ("-".equals(s)) ("-"+ic).toDouble else ic.toDouble
        case None~ic => {ic.toDouble}
      }

  //TODO: to check why can't avoid case statement
  def skewY:Parser[SkewYOp]=
    """skewY""".r~rep(wsp)~"""\(""".r~rep(wsp)~number~rep(wsp)~"""\)""".r^^{
      case c~ws1~lb~ws2~angle~ws3~rb => SkewYOp("skewY",SkewYOpArg(angle.toFloat))
    }

  def skewX:Parser[SkewXOp]=
    """skewX""".r~rep(wsp)~"""\(""".r~rep(wsp)~number~rep(wsp)~"""\)""".r^^{
      case c~ws1~lb~ws2~angle~ws3~rb => SkewXOp("skewY",SkewXOpArg(angle.toFloat))
    }

  def rotate:Parser[RotateOp]=
    """rotate""".r~rep(wsp)~"""\(""".r~rep(wsp)~number~opt(comma_wsp~number~comma_wsp~number)~rep(wsp)~"""\)""".r^^{
      case c~ws1~lb~ws2~angle~Some(cw1~cx~cw2~cy)~ws3~rb=>RotateOp("rotate",RotateOpArg(angle.toFloat,Some(cx.toFloat,cy.toFloat)))
      case c~ws1~lb~ws2~angle~None~ws3~rb=>RotateOp("rotate",RotateOpArg(angle.toFloat,None))
    }

  def scale:Parser[ScaleOp]=
    """scale""".r~rep(wsp)~"""\(""".r~rep(wsp)~number~opt(comma_wsp~number)~rep(wsp)~"""\)""".r^^{
      case c~ws1~lb~ws2~sx~Some(cw~sy)~ws3~rb => ScaleOp("scale",ScaleOpArg(sx.toFloat,Some(sy.toFloat)))
      case c~ws1~lb~ws2~sx~None~ws3~rb => ScaleOp("scale",ScaleOpArg(sx.toFloat,None))
    }

  def translate:Parser[TranslateOp]=
    """translate""".r~rep(wsp)~"""\(""".r~rep(wsp)~number~opt(comma_wsp~number)~rep(wsp)~"""\)""".r^^{
      case c~ws1~lb~ws2~tx~Some(cw~ty)~ws3~rb => TranslateOp("translate",TranslateOpArg(tx.toFloat,Some(ty.toFloat)))
      case c~ws1~lb~ws2~tx~None~ws3~rb =>  TranslateOp("translate",TranslateOpArg(tx.toFloat,None))
    }

  def matrix:Parser[MatrixOp]=
    """matrix""".r~rep(wsp)~"""\(""".r~rep(wsp)~number~comma_wsp~number~comma_wsp~number~comma_wsp~number~comma_wsp~number~comma_wsp~number~rep(wsp)~"""\)""".r^^{
      case m~ws1~lb~ws2~a~cw1~b~cw2~c~cw3~d~cw4~e~cw5~f~ws3~rb =>
        MatrixOp("matrix",MatrixOpArg(a.toFloat,b.toFloat,c.toFloat,d.toFloat,e.toFloat,f.toFloat))
    }

  def transform:Parser[TransformCommand]=
    matrix^^{a=>TransformCommand(a.command,getMatrix[MatrixOp](a))}|
      translate^^{a=>TransformCommand(a.command,getMatrix[TranslateOp](a))}|
      scale^^{a=>TransformCommand(a.command,getMatrix[ScaleOp](a))}|
      rotate^^{a=>TransformCommand(a.command,getMatrix[RotateOp](a))}|
      skewX^^{a=>TransformCommand(a.command,getMatrix[SkewXOp](a))}|
      skewY^^{a=>TransformCommand(a.command,getMatrix[SkewYOp](a))}

  def transforms:Parser[Seq[TransformCommand]]=
    transform~comma_wsp~rep(comma_wsp)~transforms^^{
      case t~cw1~cw2~ts=> if (ts.isEmpty) List(t) else t+:ts
    }|
      transform^^{a=>List(a)}


  def transform_list:Parser[Option[Seq[TransformCommand]]]=
    rep(wsp)~opt(transforms)~rep(wsp)^^{
      case ws1~Some(ts)~ws2 => Some(ts)
      case ws1~None~ws2 => None
    }

  def digitReduce(d:Double)=BigDecimal(d).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  def D2R(deg:Double)=deg*(scala.math.Pi/180)
  def R2D(deg:Double)=deg/(scala.math.Pi/180)
  def cosine(deg:Double)=digitReduce(scala.math.cos(D2R(deg)))
  def sine(deg:Double)=digitReduce(scala.math.sin(D2R(deg)))
  def tan(deg:Double)=digitReduce(scala.math.tan(D2R(deg)))

  //see http://www.w3.org/TR/SVG/coords.html#TransformMatrixDefined
  def getMatrix[A](a:A):DenseMatrix[Float]=
    a match{
      case a:MatrixOp => {
        val margs=a.args
        val arr=Array[Float](
          margs.a,margs.b,0f,
          margs.c,margs.d,0f,
          margs.e,margs.f,1f
          )
        new DenseMatrix[Float](3,3,arr)
      }
      case a:TranslateOp=>{
        val margs=a.args
        val tY= margs.tY match{ case Some(tY) => tY case _ => 0f}
        val arr=Array[Float](
         1f,0f,0f,
         0f,1f,0f,
         margs.tX, tY,1
         )
        new DenseMatrix[Float](3,3,arr)
      }
      case a:ScaleOp=>{
        val margs=a.args
        val sY= margs.sY match{ case Some(sY) => sY case _ => margs.sX}
        val arr=Array[Float](
         margs.sX,0f,0f,
         0f,sY,0f,
         0f,0f,1f
       )
        new DenseMatrix[Float](3,3,arr)
      }
      case a:RotateOp=>{
        val margs=a.args
        val translteArgs= margs.translateArg match{ case Some((cX,cY)) => (cX,cY) case _ => (0f,0f)}
        val arr=Array[Float](
          cosine(margs.rAngle).toFloat,sine(margs.rAngle).toFloat,0f,
          -sine(margs.rAngle).toFloat,cosine(margs.rAngle).toFloat,0f,
          0f,0f,1f
       )
        if (translteArgs._1==0 && translteArgs._2==0)
          new DenseMatrix[Float](3,3,arr)
        else{
          val tX=translteArgs._1
          val tY= translteArgs._2
          val translatePositiveArr=Array[Float](
           1f,0f,0f,
           0f,1f,0f,
           tX, tY,1
           )
          val translateNegativeArr=Array[Float](
           1f,0f,0f,
           0f,1f,0f,
           -tX, -tY,1
           )
          new DenseMatrix[Float](3,3, translatePositiveArr)*
            new DenseMatrix[Float](3,3,arr)*
            new DenseMatrix[Float](3,3, translateNegativeArr)
        }
      }
      case a:SkewXOp=>{
        val margs=a.args
        val arr=Array[Float](
         1f,0f,0f,
         tan(margs.skAngle).toFloat,1f,0f,
         0f,0f,1f
        )
        new DenseMatrix[Float](3,3,arr)
      }
      case a:SkewYOp=>{
        val margs=a.args
        val arr=Array[Float](
        1f,tan(margs.skAngle).toFloat,0f,
        0f,1f,0f,
        0f,0f,1f
       )
        new DenseMatrix[Float](3,3,arr)
      }

    }

}

object TransformParser extends TransformParser{
 def apply(command:String)=parse(transform_list,command) match {
   case Success(matched,_) => matched match { case Some(commands) => commands; case _ => Seq.empty[TransformCommand]}
   case Failure(msg,_) => {println("FAILURE: " + msg); Seq.empty[TransformCommand]}
   case Error(msg,_) => {println("ERROR: " + msg); Seq.empty[TransformCommand]}
 }
}

object TestTransformParser extends TransformParser{
  def main(args: Array[String]) = {
    val command="translate(-10,-20) scale(2) rotate(45) translate(5,10)"
    parse(transform_list,command) match {
      case Success(matched,_) => matched match { case Some(commands) => commands.foreach(println); case _ => println("None")}
      case Failure(msg,_) => println("FAILURE: " + msg)
      case Error(msg,_) => println("ERROR: " + msg)
    }
  }
}

