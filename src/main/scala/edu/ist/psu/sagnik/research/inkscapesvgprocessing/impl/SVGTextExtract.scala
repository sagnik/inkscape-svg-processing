package edu.ist.psu.sagnik.research.inkscapesvgprocessing.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.{TextGroups, PathGroups, SVGPath, SVGGroup}
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.pathparser.impl.SVGPathfromDString
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.reader.XMLReader
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.impl.SVGCharFactory
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.textparser.model.{TextPath, SVGChar}
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.impl.TransformParser

import scala.xml.NodeSeq

/**
 * Created by szr163 on 12/13/15.
 */
object SVGTextExtract {

  def apply(fileLoc:String)=getTexts(XMLReader(fileLoc),GroupExtract.apply(fileLoc))

  def getTexts(xmlContent:scala.xml.Elem, svgGroups:Seq[SVGGroup]):Seq[SVGChar]= {

    val topLevelTextPaths = xmlContent \ "text"
    val gIdMap=getGroupParents((xmlContent \ "g"),(xmlContent \\ "g").map(a=>a \@ "id").groupBy(x=>x).map{case (k,v) => (k,Seq.empty[String]) })
    val lowerLevelTextPaths=iterateOverGroups(xmlContent \ "g",Seq.empty[TextGroups],gIdMap)

    println(topLevelTextPaths.length,lowerLevelTextPaths.length)

    val topLevelTextNodePaths=
      topLevelTextPaths.map(x =>
        TextPath(
          id=x.attribute("id") match {
            case Some(idExists) => idExists.text
            case _ => "noID"
          },
          styleString = x \@ "style", //x.attribute("style") match {case Some(con)=>con.text case _ => ""},
          transformOps=TransformParser(x \@ "transform"),
          groups = Seq.empty[SVGGroup],
          tPContent = x.toString
        )
      )

    val lowerLevelTextNodepaths=
      lowerLevelTextPaths.map(x =>
        TextPath(
          id=x.textNode.attribute("id") match {
            case Some(idExists) => idExists.text
            case _ => "noID"
          },
          styleString= x.textNode \@ "style",
          transformOps=TransformParser(x.textNode \@ "transform"),
          groups = svgGroups.filter(a=>x.gIds.contains(a.id)),
          tPContent=x.textNode.toString
          )
      )

    (topLevelTextNodePaths++lowerLevelTextNodepaths).map(x=>SVGCharFactory(x))
    Seq.empty[SVGChar]
  }

  def iterateOverGroups(tlGs:NodeSeq,textPathGIDs:Seq[TextGroups],gIdMap:Map[String,Seq[String]]):Seq[TextGroups]=
    if (tlGs.length==0) textPathGIDs
    else{
      val newGId=tlGs.head \@ "id" //TODO: What happens if the group doesn't have an ID?
      val parentGids=gIdMap.get(newGId) match{
          case(Some(a))=> a
          case None => Seq.empty[String]
        }

      val thisTLGpathGIDs=textPathGIDs ++ (tlGs.head \ "text").map(x=>TextGroups(x,newGId +: parentGids))
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
            case _ => List(v)
          }
          )
      }
      getGroupParents(tlGs.tail ++ newtlGs, newParentMap)
    }


}
