package edu.ist.psu.sagnik.research.svgxmlprocess.parsers

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.RegexParsers
/**
 * Created by sagnik on 11/12/15.
 */

class SVGPathParser extends RegexParsers {
  def wsp: Parser[String]= """ """.r ^^ {_.toString}
  def digit:Parser[String]="""0|1|2|3|4|5|6|7|8|9""".r ^^ {_.toString}
  def digit_sequence: Parser[String]=rep(digit) ^^{_.mkString("")}
  def sign:Parser[String]="""\+|\-""".r ^^{_.toString}
  def exponent:Parser[String]="""e|E""".r~opt(sign)~digit_sequence ^^ { //TODO: Not handling cases such as e!234, which are definitely wrong
    case exponent~Some(sign)~digitSeq =>
      if ("-".equals(sign)) scala.math.exp(-1*digitSeq.toDouble).toString else scala.math.exp(digitSeq.toDouble).toString
    case _ => ""
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
  //TODO: Why is this ever used?
  def floating_point_constant: Parser[String]=
    fractional_constant~opt(exponent)^^{
      case fc~Some(ep)=>fc+ep
    } |
  digit_sequence~exponent ^^{
    case ds~ep=>ds+ep
  }


  /*
  def digit_sequence_rec: Parser[Option[String]]=digit|digit~digit_sequence_rec ^^ {
    case Some(oneDigit) | None => Some(oneDigit.toString)
    case Some(digit1)|Some(digit2)~None => Some(digit1.toString+digit2.toString)
    case Some(digit1)|Some(digit2)~Some(digitSeq) => Some(digit1.toString+digit2.toString+digitSeq.toString)
  }
  */

}

object TestSVGPathParser extends SVGPathParser{
  def main(args: Array[String]) = {
    parse(fractional_constant, "1.234") match {
      case Success(matched,_) => println(matched)
      case Failure(msg,_) => println("FAILURE: " + msg)
      case Error(msg,_) => println("ERROR: " + msg)
    }
  }
}
