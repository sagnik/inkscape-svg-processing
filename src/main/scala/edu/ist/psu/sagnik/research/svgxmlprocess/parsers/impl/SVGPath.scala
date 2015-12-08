package edu.ist.psu.sagnik.research.svgxmlprocess.parsers.impl

import edu.ist.psu.sagnik.research.svgxmlprocess.model.Rectangle
import edu.ist.psu.sagnik.research.svgxmlprocess.parsers.model._


/**
 * Created by sagnik on 12/4/15.
 */
object SVGPath extends SVGPathParser{

  def getPathComponents(command:String):Seq[PathCommand]=
    parse(svg_path,command) match {
      case Success(matched,_) => matched
      case Failure(msg,_) => {println(s"couldn't parse the command ${msg}"); sys.exit(1);}
      case Error(msg,_) => {{println(s"couldn't parse the command, exceptions: ${msg}"); sys.exit(1);}}
    }

  def pathCommands(pathElems:Seq[PathCommand]):Seq[Rectangle]=
    if (pathElems.isEmpty) Seq.empty[Rectangle]
    else if (!pathElems(0).isInstanceOf[Move])
      Seq.empty[Rectangle]
    else{
      val pathElemsMod=pathElems.map(x=>parseMoveCommand(x)).flatten.zipWithIndex.map(x=>{
        if (x._2==0)
          x._1.asInstanceOf[Move].copy(isAbsolute = true)
        else x._1
      })
      val firstMoveCommand=pathElems(0).asInstanceOf[Move]
      pathElemsMod.foreach{println}
      Seq.empty[Rectangle]
    }

  def parseMoveCommand(x:PathCommand):Seq[PathCommand]=
    if (!x.isInstanceOf[Move]) List(x)
    else if (x.args.length==1) List(x)
    else{ //Move command with multiple arguments, needs to be converted into line commands
    val moveArg=if (x.args(0).isInstanceOf[CordPair]) MovePath(x.args(0).asInstanceOf[CordPair]) else MovePath(CordPair(0,0)) //TODO: possible problem
    val lineCargs=x.args.slice(1,x.args.length).filter(a=>a.isInstanceOf[CordPair]).map(a=>a.asInstanceOf[CordPair])
      List(Move(x.isAbsolute,List(moveArg)),Line(x.isAbsolute,lineCargs.map(b=>LinePath(b))))
    }

  def getPathBB(pathElems:Seq[PathCommand],bb:Rectangle,lep:CordPair):Rectangle=
    pathElems match{
      case Nil => bb
      case pathElem::Nil =>
        Rectangle.rectMerge(bb,
          pathElem.getBoundingBox[pathElem.type]
          (lep,pathElem.isAbsolute,pathElem.args,Rectangle(0,0,0,0)))
      case pathElem::rest=>{
        val lastEndPoint=pathElem.getEndPoint[pathElem.type ](lep,pathElem.isAbsolute,pathElem.args)
        getPathBB(rest,bb,lastEndPoint)
      }
    }

  /*
  def getPathElementBB(p:PathCommand,lep:CordPair):Rectangle=
    p match{
      case p:QBC => p.getBoundingBox(lep,p.isAbsolute,p.args,Rectangle(0,0,0,0))
      case p:EllipseCommand => getBB[EllipseCommand](p.asInstanceOf[EllipseCommand],lep)
      case p: Line => getBB[Line](p.asInstanceOf[Line],lep)
      case p: HL => getBB[HL](p.asInstanceOf[HL],lep)
      case p: VL => getBB[VL](p.asInstanceOf[VL],lep)
      case p: SMC => getBB[SMC](p.asInstanceOf[SMC],lep)
      case p:SmQBC => getBB[SmQBC](p.asInstanceOf[SmQBC],lep)
      case _ => ???

    }
  def getLastEndPoint(p:PathCommand,lep:CordPair):CordPair={
    p match{
      case p:QBC => new RecursiveEP[QBCPath].getEndPoint(lep,p.isAbsolute,p.args)
      case p:EllipseCommand => new RecursiveEP[EllipsePath].getEndPoint(lep,p.isAbsolute,p.args)
      case p: Line => new RecursiveEP[LinePath].getEndPoint(lep,p.isAbsolute,p.args)
      case p: HL => new RecursiveEP[HLPath].getEndPoint(lep,p.isAbsolute,p.args)
      case p: VL => new RecursiveEP[VLPath].getEndPoint(lep,p.isAbsolute,p.args)
      case p: SMC => new RecursiveEP[SMCPath].getEndPoint(lep,p.isAbsolute,p.args)
      case p:SmQBC => new RecursiveEP[SmQBCPath].getEndPoint(lep,p.isAbsolute,p.args)
      case _ => ???
    }
  }


  def getBB[A<: PathCommand](p:A,lep:CordPair):Rectangle=
    p match {
      case p: QBC => p.getBoundingBox[QBCPath](lep, p.isAbsolute, p.args,Rectangle (0, 0, 0, 0))
      case p: EllipseCommand => p.getBoundingBox[EllipsePath](lep, p.isAbsolute, p.args,Rectangle (0, 0, 0, 0))
      case p: Line => p.getBoundingBox[LinePath](lep, p.isAbsolute, p.args,Rectangle (0, 0, 0, 0))
      case p: HL => p.getBoundingBox[HLPath](lep, p.isAbsolute, p.args,Rectangle (0, 0, 0, 0))
      case p: VL => p.getBoundingBox[VLPath](lep, p.isAbsolute, p.args,Rectangle (0, 0, 0, 0))
      case p: SMC => p.getBoundingBox[SMCPath](lep, p.isAbsolute, p.args,Rectangle (0, 0, 0, 0))
      case p: SmQBC => p.getBoundingBox[SmQBCPath](lep, p.isAbsolute, p.args,Rectangle (0, 0, 0, 0))
      case _ => ???
    }
  */
  def main(args: Array[String]):Unit={
    val command="m 3964.54,3342.8 251.35,0 m -251.35,17.43 0,-34.86 m 251.35,34.86 0,-34.86 m -1742.01,88.84 264.28,637.09 528.57,5.62 528.56,-301.39 264.28,-66.92 m -1585.69,-324.44 0,99.52 m -17.43,-99.52 34.86,0 m -34.86,99.52 34.86,0 m 246.85,498.2 0,178.25 m -17.43,-178.25 34.86,0 m -34.86,178.25 34.86,0 m 511.14,-149.57 0,132.7 m -17.43,-132.7 34.86,0 m -34.86,132.7 34.86,0 m 511.13,-414.41 0,93.9 m -17.43,-93.9 34.86,0 m -34.86,93.9 34.86,0 m 246.85,-144.51 0,60.17 m -17.43,-60.17 34.86,0 m -34.86,60.17 34.86,0"
    //val command="M10 10 C 20 20, 40 20, 50 10 70 20, 120 20, 120 10 120 20, 180 20, 170 10"
    pathCommands(getPathComponents(command))
  }

}
