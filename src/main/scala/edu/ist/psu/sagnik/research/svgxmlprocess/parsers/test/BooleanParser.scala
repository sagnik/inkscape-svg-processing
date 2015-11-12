package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.test

import scala.util.parsing.combinator.RegexParsers

/**
 * Created by sagnik on 11/12/15.
 */
class BooleanParser extends RegexParsers {
  def boole: Parser[Boolean] = ("true" | "false") ^^
    {
      case tree => tree.toBoolean
      case _ => throw new Exception("Invalid Boolean")
    }
  def conjunction: Parser[Boolean] = boole ~ opt("&&" ~ boole) ^^
    {
      case tree1 ~ None => tree1
      case tree1 ~ Some("&&" ~ tree2) => tree1 && tree2
    }
  def disjunction: Parser[Boolean] =  boole ~ rep("||" ~> boole) ^^
    {
      case tree1 ~ Nil => tree1
      case tree1 ~ booles => tree1 || booles.reduce(_||_)
    }
}

object BooleanParser {

  val parsers = new BooleanParser

  def main(args: Array[String]): Unit = {

    var exp = "true"
    var tree = parsers.parseAll(parsers.boole, exp)
    println(exp + " => " + tree.get)

    tree = parsers.parseAll(parsers.conjunction, exp)
    println(exp + " => " + tree.get)

    exp = "true && false"
    tree = parsers.parseAll(parsers.conjunction, exp)
    println(exp + " => " + tree.get)

    exp = "true"
    tree = parsers.parseAll(parsers.disjunction, exp)
    println(exp + " => " + tree.get)

    exp = "true || false || true"
    tree = parsers.parseAll(parsers.disjunction, exp)
    println(exp + " => " + tree.get)
  }
}