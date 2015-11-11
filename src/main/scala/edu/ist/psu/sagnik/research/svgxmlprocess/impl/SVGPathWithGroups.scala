package edu.ist.psu.sagnik.research.svgxmlprocess.impl

import edu.ist.psu.sagnik.research.svgxmlprocess.model.{PathGroups, pathOp, SVGPath}
import edu.ist.psu.sagnik.research.svgxmlprocess.reader.XMLReader

import scala.xml.{NodeSeq}

/**
 * Created by szr163 on 11/8/15.
 */


object SVGPathWithGroups {
  def apply(fileLoc:String)=getPaths(XMLReader(fileLoc))

  def getPaths(xmlContent:scala.xml.Elem):Seq[SVGPath]= {

    val topLevelPaths = xmlContent \ "path"
    val gIdMap=getGroupParents((xmlContent \ "g"),(xmlContent \\ "g").map(a=>a \@ "id").groupBy(x=>x).map{case (k,v) => (k,Seq.empty[String]) })

    //gIdMap.foreach(x=>println(s"[group number]: ${x._1} [parent group]: ${x._2}"))

    val lowerLevelPaths=iterateOverGroups(xmlContent \ "g",Seq.empty[PathGroups],gIdMap)


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

  def iterateOverGroups(tlGs:NodeSeq,pathGIDs:Seq[PathGroups],gIdMap:Map[String,Seq[String]]):Seq[PathGroups]=
    if (tlGs.length==0) pathGIDs
    else{
      val newGId=tlGs.head \@ "id" //TODO: What happens if the group doesn't have an ID?
      val parentGids=gIdMap.get(newGId) match{
          case(Some(a))=> a
          case None => Seq.empty[String]
        }

      val thisTLGpathGIDs=pathGIDs ++ (tlGs.head \ "path").map(x=>PathGroups(x,newGId +: parentGids))
      iterateOverGroups(tlGs.tail ++ tlGs.head \"g",thisTLGpathGIDs, gIdMap)

    }


  def getGroupParents(tlGs: NodeSeq, parentMap:Map[String,Seq[String]]):Map[String,Seq[String]]=
    if (tlGs.isEmpty) parentMap
    else {
      val newGId = tlGs.head \@ "id" //TODO: What happens if the group doesn't have an ID?
      val newtlGs = tlGs.head \ "g"
      val newParentMap = parentMap ++ newtlGs.map(x => (x \@ "id", newGId)).toMap.map {
        case (k, v) => (k,
          parentMap.get(v) match {
            case Some(a) => v +: a
            case _ => List(v).toIndexedSeq
          }
          )
      }
      getGroupParents(tlGs.tail ++ newtlGs, newParentMap)
    }


}
