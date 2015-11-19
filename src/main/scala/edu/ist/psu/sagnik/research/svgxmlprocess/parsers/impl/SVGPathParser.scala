package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.impl

import edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model.{EllipseCommand, EllipsePath}

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

  def digit_sequence: Parser[String]=digit~rep(digit) ^^{case d~ds => if (ds.isEmpty) d else  d+ds.mkString("")} // digit~digit_sequence ^^{case d~ds => println(s"digit ${d} digit sequence: ${ds}"); ds.toString}|digit^^{_.toString} //

  /*this is for testing*/
/*
  def number_sequence:Parser[Seq[String]]=digit_sequence~opt(comma_wsp)~rep(digit_sequence)^^
    {
    case n~cw~ns=>if (ns.isEmpty) List(n).toIndexedSeq else ns:+n
    }|
    digit_sequence^^{List(_).toIndexedSeq}
*/
/*********************************/

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

  def nonnegative_number:Parser[Double]=
    floating_point_constant^^{_.toDouble}|integer_constant^^{_.toDouble}

  def coordinate:Parser[Double] = number ^^{ _.toDouble}

  def coordinate_pair:Parser[(Double,Double)]=coordinate~opt(comma_wsp)~coordinate^^{
    case cr1~cm~cr2=> (cr1,cr2)
  }

  implicit def D2B(d:Double):Boolean=if (d==1.0) true else false

  def elliptical_arc_argument:Parser[EllipsePath]=
    nonnegative_number~opt(comma_wsp)~nonnegative_number~opt(comma_wsp)~
      number~(comma_wsp)~flag~opt(comma_wsp)~flag~opt(comma_wsp)~(coordinate_pair)^^{
      case rx~cw1~ry~cw2~rot~cw3~laf~cw4~sf~comma_wsp~cp=>
        EllipsePath(rx,ry,rot,laf,sf,cp._1,cp._2)
    }

  def elliptical_arc_argument_sequence:Parser[Seq[EllipsePath]]=
     elliptical_arc_argument~opt(comma_wsp)~rep(elliptical_arc_argument) ^^{
        case ea~cw1~eas=>if (eas.isEmpty) List(ea).toIndexedSeq else ea+:eas.toIndexedSeq
      } |
       elliptical_arc_argument^^{List(_).toIndexedSeq}

  def elliptical_arc: Parser[EllipseCommand]="""A|a""".r~rep(wsp)~elliptical_arc_argument_sequence^^{
    case c~ws~paths=> if ("a".equals("c")) EllipseCommand(false,paths) else EllipseCommand(true,paths)
  }

  

}

object TestSVGPathParser extends SVGPathParser{
  def main(args: Array[String]) = {
//    parse(number_sequence, "1") match {
//    parse(digit_sequence, "1234") match {
//    parse(coordinate_pair, "12    20") match {
    parse(elliptical_arc, "a25,25 -30 0,1 50,-25 l 50,-25") match {
      case Success(matched,_) => println(s"[matched]: ${matched}")
      case Failure(msg,_) => println("FAILURE: " + msg)
      case Error(msg,_) => println("ERROR: " + msg)
    }
  }
}
