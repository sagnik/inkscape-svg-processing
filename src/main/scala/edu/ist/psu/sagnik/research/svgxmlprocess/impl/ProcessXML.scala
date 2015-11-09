package edu.ist.psu.sagnik.research.svgxmlprocess.impl

import edu.ist.psu.sagnik.research.svgxmlprocess.model.{pathOp, SVGPath}
import edu.ist.psu.sagnik.research.svgxmlprocess.reader.XMLReader

import scala.xml.{Elem, Node, NodeSeq}

/**
 * Created by szr163 on 11/8/15.
 */

case class PathGroups(path:Node,gIds:Seq[String])

object ProcessXML {
  def apply(fileLoc:String)=getPaths(XMLReader(fileLoc))

  def getPaths(xmlContent:scala.xml.Elem):Seq[SVGPath]= {

    val topLevelPaths = xmlContent \ "path"
    val lowerLevelPaths=iterateOverGroups(xmlContent \ "g",Seq.empty[PathGroups],Seq.empty[String])

    val gIdMap=getGroupParents((xmlContent \\ "g"),(xmlContent \\ "g").map(a=>a \@ "id").groupBy(x=>x))

    val topLevelSVGPaths=
      topLevelPaths.map(x =>
      SVGPath(
        x.attribute("id") match {
          case Some(idExists) => idExists.text
          case _ => "noID"
        },
        pdContent = x.attribute("d") match {case Some(con)=>con.text case _ => ""},
        pContent=x.toString(),
        pOps = Seq.empty[pathOp],
        gIds = Seq.empty[String]
      )
      )

    val lowerLevelSVGpaths=
    lowerLevelPaths.map(x =>
      SVGPath(
        x.path.attribute("id") match {
          case Some(idExists) => idExists.text
          case _ => "noID"
        },
        pdContent = x.path.attribute("d") match {
          case Some(con) => con.text
          case _ => ""
        },
        pContent = x.path.toString(),
        pOps = Seq.empty[pathOp],
        gIds = x.gIds
      )
      )
    topLevelSVGPaths ++ lowerLevelSVGpaths
  }

  def iterateOverGroups(tlGs:NodeSeq,pathGIDs:Seq[PathGroups],gIdsSofar:Seq[String]):Seq[PathGroups]=
    if (tlGs.length==0) pathGIDs
    else{
      val newGId=tlGs.head \@ "id" //TODO: What happens if the group doesn't have an ID?
      val thisTLGpathGIDs=(tlGs.head \ "path").map(x=>PathGroups(x,gIdsSofar:+newGId))
      iterateOverGroups(tlGs.head \"g",pathGIDs++thisTLGpathGIDs,gIdsSofar:+newGId)

    }

  def getGroupParents(tlGs: NodeSeq, parentMap:Map[String,Seq[String]])={
    //parentMap.foreach{x=>println(s"[key]: ${x._1} [values]: ${x._2}")}
    if (tlGs.isEmpty)
  }

}
