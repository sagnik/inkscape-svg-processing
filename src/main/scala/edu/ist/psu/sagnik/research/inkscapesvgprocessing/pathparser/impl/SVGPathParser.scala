package edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.model._

import scala.util.parsing.combinator.RegexParsers
import scala.language.implicitConversions
/**
 * Created by sagnik on 11/12/15.
 * ? => opt
 * * => rep
 * */

class SVGPathParser extends RegexParsers {

  override def skipWhitespace = false

  def wsp: Parser[String]= "\\s|\\n".r ^^ {_.toString} //TODO: this needs to be changed to accomodate #x20, #xD, #xA, #x9
  //see http://www.xmlplease.com/normalized for an example.

  def digit:Parser[String]="""0|1|2|3|4|5|6|7|8|9""".r ^^ {_.toString}

  def digit_sequence: Parser[String]=digit~rep(digit) ^^{case d~ds => if (ds.isEmpty) d else  d+ds.mkString("")} // digit~digit_sequence ^^{case d~ds => println(s"digit ${d} digit sequence: ${ds}"); ds.toString}|digit^^{_.toString} //

  /*this is for testing*/
  /*
    def number_sequence:Parser[Seq[String]]=digit_sequence~opt(comma_wsp)~rep(digit_sequence)^^
      {
      case n~cw~ns=>if (ns.isEmpty) List(n) else ns:+n
      }|
      digit_sequence^^{List(_)}
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

  def coordinate_pair:Parser[CordPair]=coordinate~opt(comma_wsp)~coordinate^^{
    case cr1~cm~cr2=> CordPair(cr1,cr2)
  }

  implicit def D2B(d:Double):Boolean=if (d==1.0) true else false

  def elliptical_arc_argument:Parser[EllipsePath]=
    nonnegative_number~opt(comma_wsp)~nonnegative_number~opt(comma_wsp)~
      number~(comma_wsp)~flag~opt(comma_wsp)~flag~opt(comma_wsp)~(coordinate_pair)^^{
      case rx~cw1~ry~cw2~rot~cw3~laf~cw4~sf~comma_wsp~cp=>
        EllipsePath(rx,ry,rot,laf,sf,cp)
    }

  def elliptical_arc_argument_sequence:Parser[Seq[EllipsePath]]=
    elliptical_arc_argument~opt(comma_wsp)~elliptical_arc_argument_sequence ^^{
      case ea~cw1~eas=>if (eas.isEmpty) List(ea) else ea+:eas
    } |
      elliptical_arc_argument^^{List(_)}

    def elliptical_arc: Parser[EllipseCommand]="""A|a""".r~rep(wsp)~elliptical_arc_argument_sequence^^{
      case c~ws~paths=> if ("a".equals("c")) EllipseCommand(false,paths) else EllipseCommand(true,paths)
    }

  def smooth_quadratic_bezier_curveto_argument_sequence:Parser[Seq[SmQBCPath]]=
    coordinate_pair~opt(comma_wsp)~smooth_quadratic_bezier_curveto_argument_sequence^^{
      case cp~cw~cps=>if (cps.isEmpty) List(SmQBCPath(cp)) else SmQBCPath(cp)+:cps
    }

  def smooth_quadratic_bezier_curveto:Parser[SmQBC]=
    """T|t""".r~rep(wsp)~smooth_quadratic_bezier_curveto_argument_sequence^^{
      case c~cw~argseq=>if ("c".equals(c)) SmQBC(false,argseq) else SmQBC(true,argseq)
    }

  def quadratic_bezier_curveto_argument: Parser[QBCPath]=
    coordinate_pair~opt(comma_wsp)~coordinate_pair^^{case cp1~cws~cp2=>QBCPath(cp1,cp2)}

  def quadratic_bezier_curveto_argument_sequence:Parser[Seq[QBCPath]]=
    quadratic_bezier_curveto_argument~opt(comma_wsp)~quadratic_bezier_curveto_argument_sequence^^{
      case qc~cws~qcs=> if (qcs.isEmpty) List(qc) else qc+:qcs
    } |
      quadratic_bezier_curveto_argument^^{List(_)}

  def quadratic_bezier_curveto:Parser[QBC]=
    """Q|q""".r~rep(wsp)~quadratic_bezier_curveto_argument_sequence^^{
      case c~cw~argseq=>if ("q".equals(c)) QBC(false,argseq) else QBC(true,argseq)
    }

  def smooth_curveto_argument: Parser[SMCPath]=
    coordinate_pair~opt(comma_wsp)~coordinate_pair^^{case cp1~cws~cp2=>SMCPath(cp1,cp2)}

  def smooth_curveto_argument_sequence:Parser[Seq[SMCPath]]=
    smooth_curveto_argument~opt(comma_wsp)~smooth_curveto_argument_sequence^^{
      case qc~cws~qcs=> if (qcs.isEmpty) List(qc) else qc+:qcs
    } |
      smooth_curveto_argument^^{List(_)}

  def  smooth_curveto:Parser[SMC]=
    """S|s""".r~rep(wsp)~smooth_curveto_argument_sequence^^{
      case c~cw~argseq=>if ("s".equals(c)) SMC(false,argseq) else SMC(true,argseq)
    }

  def curveto_argument:Parser[CurvePath]=
    coordinate_pair~opt(comma_wsp)~coordinate_pair~opt(comma_wsp)~coordinate_pair^^{case cw1~cws1~cw2~cws2~cw3 => CurvePath(cw1,cw2,cw3)}

  def curveto_argument_sequence:Parser[Seq[CurvePath]]=
    curveto_argument~opt(comma_wsp)~curveto_argument_sequence^^{
      case qc~cws~qcs=> if (qcs.isEmpty) List(qc) else qc+:qcs
    } |
      curveto_argument^^{List(_)}


  def curveto:Parser[Curve]=
    """C|c""".r~rep(wsp)~curveto_argument_sequence^^{
      case c~cw~argseq=>if ("c".equals(c)) Curve(false,argseq) else Curve(true,argseq)
    }

  def  vertical_lineto_argument_sequence:Parser[Seq[VLPath]]=
    coordinate~opt(comma_wsp)~vertical_lineto_argument_sequence^^{
      case c~cws~cs=> if (cs.isEmpty) List(VLPath(c)) else VLPath(c)+:cs
    } |
      coordinate^^{a=> List(VLPath(a))}

  def vertical_lineto:Parser[VL]=
    """V|v""".r~rep(wsp)~vertical_lineto_argument_sequence^^{
      case c~cw~argseq=>if ("v".equals(c)) VL(false,argseq) else VL(true,argseq)
    }

  def  horizontal_lineto_argument_sequence:Parser[Seq[HLPath]]=
    coordinate~opt(comma_wsp)~horizontal_lineto_argument_sequence^^{
      case c~cws~cs=> if (cs.isEmpty) List(HLPath(c)) else HLPath(c)+:cs
    } |
      coordinate^^{a=>List(HLPath(a))}

  def horizontal_lineto:Parser[HL]=
    """H|h""".r~rep(wsp)~horizontal_lineto_argument_sequence^^{
      case c~cw~argseq=>if ("h".equals(c)) HL(false,argseq) else HL(true,argseq)
    }

  def lineto_argument_sequence:Parser[Seq[LinePath]]=
    coordinate_pair~opt(comma_wsp)~lineto_argument_sequence^^{
      case qc~cws~qcs=> if (qcs.isEmpty) List(LinePath(qc)) else LinePath(qc)+:qcs
    } |
      coordinate_pair^^{a=>List(LinePath(a))}

  def lineto:Parser[Line]=
    """L|l""".r~rep(wsp)~lineto_argument_sequence^^{
      case c~cw~argseq=>if ("l".equals(c)) Line(false,argseq) else Line(true,argseq)
    }

  def closepath:Parser[Close]="""Z|z""".r^^{case _ => Close(true,Seq.empty[Any])}

  def moveto_argument_sequence:Parser[Seq[MovePath]]=
    coordinate_pair~opt(comma_wsp)~moveto_argument_sequence^^{
      case qc~cws~qcs=> if (qcs.isEmpty) List(MovePath(qc)) else MovePath(qc)+:qcs
    } |
      coordinate_pair^^{a=>List(MovePath(a))}

  def moveto:Parser[Move]=
    """M|m""".r~rep(wsp)~moveto_argument_sequence^^{
      case c~cw~argseq=>if ("m".equals(c)) Move(false,argseq) else Move(true,argseq)
    }

  def drawto_command:Parser[PathCommand]=
    closepath^^{a=>a}|
      lineto^^{a=>a} |
      horizontal_lineto^^{a=>a} |
      vertical_lineto^^{a=>a} |
      curveto^^{a=>a} |
      smooth_curveto^^{a=>a} |
      quadratic_bezier_curveto^^{a=>a}|
      smooth_quadratic_bezier_curveto^^{a=>a}|
      elliptical_arc^^{a=>a}

  def drawto_commands:Parser[Seq[PathCommand]]=
    drawto_command~rep(wsp)~rep(drawto_command)^^{
      case dc~ws~dcs => if (dcs.isEmpty) List(dc) else dc+:dcs
    } | drawto_command^^{List(_)}

  def moveto_drawto_command_group:Parser[Seq[PathCommand]]=
    moveto~rep(wsp)~opt(drawto_commands)^^{
      case mt~ws~Some(dt)=>mt+:dt
      case mt~ws~None => List(mt)
    }


  def moveto_drawto_command_groups:Parser[Seq[PathCommand]]=
    moveto_drawto_command_group~rep(wsp)~rep(moveto_drawto_command_group)^^{
      case mdcg~ws~mdcgs => if (mdcgs.isEmpty) mdcg else mdcg ++ mdcgs.flatten
    }|
      moveto_drawto_command_group^^{case a=>a}

  def svg_path:Parser[Seq[PathCommand]]=
    rep(wsp)~opt(moveto_drawto_command_groups)~rep(wsp)^^{
      case ws~Some(mdcgs)~wsp => mdcgs
      case ws~None~wsp => Seq.empty[PathCommand]
    }

}

object TestSVGPathParser extends SVGPathParser{
  def main(args: Array[String]) = {
    val c="m 3964.54,3342.8 251.35,0 m -251.35,17.43 0,-34.86 m 251.35,34.86 0,-34.86 m -1742.01,88.84 264.28,637.09 528.57,5.62 528.56,-301.39 264.28,-66.92 m -1585.69,-324.44 0,99.52 m -17.43,-99.52 34.86,0 m -34.86,99.52 34.86,0 m 246.85,498.2 0,178.25 m -17.43,-178.25 34.86,0 m -34.86,178.25 34.86,0 m 511.14,-149.57 0,132.7 m -17.43,-132.7 34.86,0 m -34.86,132.7 34.86,0 m 511.13,-414.41 0,93.9 m -17.43,-93.9 34.86,0 m -34.86,93.9 34.86,0 m 246.85,-144.51 0,60.17 m -17.43,-60.17 34.86,0 m -34.86,60.17 34.86,0"
    //val c="m 3964.54,3342.8 l -1742.01,88.84 264.28,637.09 528.57,5.62 528.56,-301.39 264.28,-66.92"
    //val c="M10 10 C 20 20, 40 20, 50 10 70 20, 120 20, 120 10 120 20, 180 20, 170 10"
    //parse(digit_sequence, "12345") match {
    //  parse(wsp, " ") match {
    //    parse(coordinate_pair, "12    20") match {
    parse(svg_path, c) match {
      case Success(matched,_) => println(s"[matched]: ${matched}")
      case Failure(msg,_) => println("FAILURE: " + msg)
      case Error(msg,_) => println("ERROR: " + msg)
    }
  }
}
