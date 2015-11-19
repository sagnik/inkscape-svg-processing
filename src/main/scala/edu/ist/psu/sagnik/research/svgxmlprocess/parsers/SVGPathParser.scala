package edu.ist.psu.sagnik.research.svgxmlprocess.parsers

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.RegexParsers
/**
 * Created by sagnik on 11/12/15.
 * ? => opt
 * * => rep
 * */

class SVGPathParser extends RegexParsers {

  override def skipWhitespace = false

  def wsp: Parser[String]= "\\s".r ^^ {_.toString}

  def digit:Parser[String]="""0|1|2|3|4|5|6|7|8|9""".r ^^ {_.toString}

  def digit_sequence: Parser[String]= rep(digit) ^^{_.mkString("")} //digit~digit_sequence ^^{_.toString}|digit^^{_.toString}

  def sign:Parser[String]="""\+|\-""".r ^^{_.toString}

  def exponent:Parser[Double]="""e|E""".r~opt(sign)~digit_sequence ^^ { //TODO: Not handling cases such as e!234, which are definitely wrong
    case ep~Some(sign)~digitSeq =>
      if ("-".equals(sign)) scala.math.exp(-1*digitSeq.toDouble) else scala.math.exp(digitSeq.toDouble)
    case _ => 1d
  }

  def fractional_constant:Parser[String]=
    opt(digit_sequence)~""".""".r~digit_sequence ^^{
      case Some(ds1)~dot~ds2 => ds1+"."+ds2
      case None~dot~ds2=> ds2
      case _ => ""
    } |
      digit_sequence~""".""".r  ^^{
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

  def comma_wsp: Parser[String] = wsp~rep(wsp)~opt(comma)~rep(wsp)^^ { case _ => ","} | comma~rep(wsp)^^{case _ => ","}

  def flag:Parser[String] = """0|1""".r ^^{_.toString}

  def number:Parser[Double]=
    opt(sign)~floating_point_constant^^{
      case Some(s)~fc => if ("-".equals(s)) ("-"+fc).toDouble else fc.toDouble
      case None~fc => fc.toDouble
    } |
      opt(sign)~integer_constant^^{
        case Some(s)~ic => if ("-".equals(s)) ("-"+ic).toDouble else ic.toDouble
        case None~ic => ic.toDouble
      }

  def nonnegative_number:Parser[Double]=
    floating_point_constant^^{_.toDouble}|integer_constant^^{_.toDouble}

  def coordinate:Parser[Double] = number ^^{println(s"[matched c]"); _.toDouble}

  def coordinate_pair:Parser[(Double,Double)]=coordinate~comma_wsp~coordinate^^{
    case cr1~cm~cr2=>(cr1,cr2)
    //case cr1~Some(cm)~cr2=> (cr1,cr2)
    //case cr1~None~cr2 => (cr1,cr2)
  }



  /*
  def nonnegative_number: Parser[String]=
    integer-constant
  | floating-point-constant
  number:
    sign? integer-constant
  | sign? floating-point-constant
  */
}

object TestSVGPathParser extends SVGPathParser{
  def main(args: Array[String]) = {
    //parse(comma_wsp, ",") match {
    parse(coordinate_pair, "12, 13") match {
      case Success(matched,_) => println(s"[matched]: ${matched}")
      case Failure(msg,_) => println("FAILURE: " + msg)
      case Error(msg,_) => println("ERROR: " + msg)
    }
  }
}
