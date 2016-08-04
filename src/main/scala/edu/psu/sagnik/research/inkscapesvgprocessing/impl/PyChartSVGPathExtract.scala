package edu.psu.sagnik.research.inkscapesvgprocessing.impl

import edu.psu.sagnik.research.inkscapesvgprocessing.model.{SVGGroup, SVGPath}
import edu.psu.sagnik.research.inkscapesvgprocessing.pathparser.impl.SVGPathfromDString
import edu.psu.sagnik.research.inkscapesvgprocessing.reader.XMLReader
import edu.psu.sagnik.research.inkscapesvgprocessing.transformparser.impl.TransformParser

import scala.xml.{Node, NodeSeq}

/**
  * Created by sagnik on 7/29/16.
  */
object PyChartSVGPathExtract {

  def apply(fileLoc:String)=getPaths(XMLReader(fileLoc),GroupExtract.apply(fileLoc))

  def getPaths(xmlContent:scala.xml.Elem, svgGroups:Seq[SVGGroup]):Seq[SVGPath]=

   pathGroups(
      xmlContent \ "g",
      Map.empty[SVGPath,Seq[SVGGroup]],
      groupNoIdCounter = 0,
      pathNoIdCounter = 0,
      Map.empty[String,String]
    )
     .map{
      case (path,groups)=>
        path.copy(
          groups=groups
        )
    }.map(
      svgPath=>
        SVGPathBB(svgPath)
    )
     .toSeq


  def pathGroups(tlGs: NodeSeq,
                 parentMap:Map[SVGPath,
                   Seq[SVGGroup]],
                 groupNoIdCounter:Int,
                 pathNoIdCounter:Int,
                 dStringMap:Map[String,String]
                ):Map[SVGPath,Seq[SVGGroup]]=

    if (tlGs.isEmpty) parentMap
    else {
      var gCounter=groupNoIdCounter
      var pCounter=pathNoIdCounter
      var pathsMappedbyDString=dStringMap

      val newGId = tlGs.head.attribute("id") match {
        case Some(idExists) => idExists.text
        case _ =>
          gCounter+=1
          "noID"+gCounter.toString
      }
      val newtlGs = tlGs.head \ "g"
      val parentMapThistlgS =
        newtlGs.map(x => {
          val childId=
            x.attribute("id") match {
              case Some(idExists) => idExists.text
              case _ =>
                gCounter+=1
                "noID" + gCounter.toString
            }

          val group=
            SVGGroup(
              id= childId,
              gtContent = x \@ "transform",
              gContent= x.toString,
              transformOps = TransformParser(x \@ "transform")
            )

          val paths=
            (x \\ "path").map(
              pathNode=>
                SVGPath(
                  pathNode.attribute("id") match {
                    case Some(idExists) =>
                      val pdContent=pathNode.attribute("d") match {case Some(con)=>con.text case _ => ""}
                      pathsMappedbyDString += (pdContent -> idExists.text)
                      idExists.text
                    case _ =>
                      val pdContent=pathNode.attribute("d") match {case Some(con)=>con.text case _ => ""}
                      val pathCounter=
                        dStringMap.get(pdContent) match{
                          case Some(existingPCounter) =>  existingPCounter //this path was seen before
                          case _ =>
                            pCounter+=1
                            pCounter.toString
                        }
                      pathsMappedbyDString += (pdContent -> pCounter.toString)
                      "noID"+pathCounter
                  },
                  pdContent = pathNode.attribute("d") match {case Some(con)=>con.text case _ => ""},
                  pContent=pathNode.toString(),
                  pOps = SVGPathfromDString.getPathCommands(pathNode.attribute("d") match {case Some(con)=>con.text case _ => ""}),
                  groups=List.empty[SVGGroup],
                  transformOps = TransformParser(pathNode \@ "transform"),
                  bb=None
                )
            )

          val combinedMap=
            combineMaps(
              parentMap,
              paths.map(x=>(x,Seq(group))).toMap //this is grouping by id, which is weird.
            )

          combinedMap
        }
        )


      val newParentMap= parentMapThistlgS.foldLeft(parentMap){case (accum,toBeMerged) => combineMaps(accum,toBeMerged)}

      pathGroups(tlGs.tail ++ newtlGs, newParentMap,gCounter,pCounter,pathsMappedbyDString)
    }

  lazy val combineMaps = (accum:Map[SVGPath,Seq[SVGGroup]],toBeMerged:Map[SVGPath,Seq[SVGGroup]]) =>
  {
    val comb=accum.toList ++ toBeMerged.toList
    comb.groupBy(_._1).map{case (k,v) => (k -> v.map(_._2).flatten.distinct) }

  }

}
