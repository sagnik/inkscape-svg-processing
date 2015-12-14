package edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.Rectangle
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.model._


/**
 * Created by sagnik on 12/4/15.
 */
object SVGPathfromDString extends SVGPathParser{

  def getPathCommands(command:String):Seq[PathCommand]=
    parse(svg_path,command) match {
      case Success(matched,_) => {
        val pathElems = matched
        if (pathElems.isEmpty) Seq.empty[PathCommand]
        else if (!pathElems(0).isInstanceOf[Move])
          Seq.empty[PathCommand]
        else {
          val pathElemsMod = pathElems.map(x => parseMoveCommand(x)).flatten.zipWithIndex.map(x => {
            if (x._2 == 0)
              x._1.asInstanceOf[Move].copy(isAbsolute = true)
            else x._1

          })
          pathElemsMod.zipWithIndex.map(x => {
            if (x._2 == (pathElemsMod.length-1) && x._1.isInstanceOf[Close])
              pathElemsMod(0)
            else x._1

          })

        }
      }
      case Failure(msg,_) => {println(s"couldn't parse the command ${msg}"); sys.exit(1);}
      case Error(msg,_) => {{println(s"couldn't parse the command, exceptions: ${msg}"); sys.exit(1);}}
    }


  def parseMoveCommand(x:PathCommand):Seq[PathCommand]=
    if (!x.isInstanceOf[Move]) List(x)
    else if (x.args.length==1) List(x)
    else{ //Move command with multiple arguments, needs to be converted into line commands
    val moveArg=if (x.args(0).isInstanceOf[MovePath]) x.args(0).asInstanceOf[MovePath] else MovePath(CordPair(0,0)) //TODO: possible problem
    val lineCargs=x.args.slice(1,x.args.length).filter(a=>a.isInstanceOf[MovePath]).map(a=>a.asInstanceOf[MovePath])
      List(Move(x.isAbsolute,List(moveArg)),Line(x.isAbsolute,lineCargs.map(b=>LinePath(CordPair(b.eP.x,b.eP.y)))))
    }

  def getPathBB(pathElems:Seq[PathCommand],bb:Rectangle,lep:CordPair):Rectangle= //{
    //println("[bb]: "+bb+" [lep]: "+lep)
    pathElems match {
      case Nil => bb
      case pathElem :: Nil =>
        Rectangle.rectMerge(bb,
          pathElem.getBoundingBox[pathElem.type]
          (lep,pathElem.isAbsolute,pathElem.args))
      case pathElem :: rest => {
        val lastEndPoint = pathElem.getEndPoint[pathElem.type ](lep,pathElem.isAbsolute,pathElem.args)
        val latestBB=Rectangle.rectMerge(bb,pathElem.getBoundingBox[pathElem.type](lep,pathElem.isAbsolute,pathElem.args))
        getPathBB(rest, latestBB, lastEndPoint)
      }
    }
  //}
  def main(args: Array[String]):Unit={
    //val command="m 3964.54,3342.8 251.35,0 m -251.35,17.43 0,-34.86 m 251.35,34.86 0,-34.86 m -1742.01,88.84 264.28,637.09 528.57,5.62 528.56,-301.39 264.28,-66.92 m -1585.69,-324.44 0,99.52 m -17.43,-99.52 34.86,0 m -34.86,99.52 34.86,0 m 246.85,498.2 0,178.25 m -17.43,-178.25 34.86,0 m -34.86,178.25 34.86,0 m 511.14,-149.57 0,132.7 m -17.43,-132.7 34.86,0 m -34.86,132.7 34.86,0 m 511.13,-414.41 0,93.9 m -17.43,-93.9 34.86,0 m -34.86,93.9 34.86,0 m 246.85,-144.51 0,60.17 m -17.43,-60.17 34.86,0 m -34.86,60.17 34.86,0"
    val command="m 3964.54,3342.8 251.35,0 m -251.35,17.43 0,-34.86 m 251.35,34.86 0,-34.86"+
      "m -1742.01,88.84 264.28,637.09 528.57,5.6String2 528.56,-301.39 264.28,-66.92"+
      "m -1585.69,-324.44 0,99.52 m -17.43,-99.52 34.86,0 m -34.86,99.52 34.86,0" +
      "m 246.85,498.2 0,178.25 m -17.43,-178.25 34.86,0 m -34.86,178.25 34.86,0" +
      "m 511.14,-149.57 0,132.7 m -17.43,-132.7 34.86,0 m -34.86,132.7 34.86,0" +
      "m 511.13,-414.41 0,93.9 m -17.43,-93.9 34.86,0 m -34.86,93.9 34.86,0" +
      "m 246.85,-144.51 0,60.17 m -17.43,-60.17 34.86,0 m -34.86,60.17 34.86,0"
    //val command="M10 10 C 20 20, 40 20, 50 10 70 20, 120 20, 120 10 120 20, 180 20, 170 10"
    //val command="M10 20 L 100 200"
    val pathCommands=getPathCommands(command)
    if (pathCommands.nonEmpty) {
//      val pathBB=getPathBB(
//        Vector(Move(true,Vector(MovePath(CordPair(10,20)))),Line(true,Vector(LinePath(CordPair(100,200))))),
//          Rectangle(0,0,0,0),
//          CordPair(0,0)
//        )
      //pathCommands.foreach(println)
      val pathBB=getPathBB(
        pathCommands.slice(1,pathCommands.length),Rectangle(0,0,0,0),
        CordPair(pathCommands(0).args(0).asInstanceOf[MovePath].eP.x,pathCommands(0).args(0).asInstanceOf[MovePath].eP.y)
      )
      println(s"x1: ${pathBB.x1} y1: ${pathBB.y1} width: ${pathBB.x2 - pathBB.x1} height: ${pathBB.y2-pathBB.y1}")
    }
    //pathCommands.foreach(println)
  }

}
