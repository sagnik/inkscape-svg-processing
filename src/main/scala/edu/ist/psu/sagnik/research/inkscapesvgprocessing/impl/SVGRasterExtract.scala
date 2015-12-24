package edu.ist.psu.sagnik.research.inkscapesvgprocessing.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.{ImageGroups, SVGGroup}
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.rasterparser.model.{SVGRaster}
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.reader.XMLReader
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.impl.TransformParser

import scala.xml.NodeSeq

/**
 * Created by sagnik on 12/24/15.
 */
object SVGRasterExtract {
  def apply(fileLoc:String)=getRasters(XMLReader(fileLoc),GroupExtract.apply(fileLoc))

  def getRasters(xmlContent:scala.xml.Elem, svgGroups:Seq[SVGGroup]):Seq[SVGRaster]= {

    val topLevelImagePaths = xmlContent \ "image"
    val gIdMap=getGroupParents((xmlContent \ "g"),(xmlContent \\ "g").map(a=>a \@ "id").groupBy(x=>x).map{case (k,v) => (k,Seq.empty[String]) })
    val lowerLevelImagePaths=iterateOverGroups(xmlContent \ "g",Seq.empty[ImageGroups],gIdMap)

    println(topLevelImagePaths.length,lowerLevelImagePaths.length)

    val topLevelRasterGraphicsPaths=
      topLevelImagePaths.map(x =>
        SVGRaster(
          id=x.attribute("id") match {
            case Some(idExists) => idExists.text
            case _ => "noID"
          },
          transformOps=TransformParser(x \@ "transform"),
          groups = Seq.empty[SVGGroup],
          imageString = x.toString,
          imageDString = x \@ "xlink-href",
          x= if ((x \@ "x").length>0) (x \@ "x").toFloat else 0f,
          y= if ((x \@ "y").length>0) (x \@ "y").toFloat else 0f,
          width = if ((x \@ "width").length>0) (x \@ "width").toFloat else 1f,
          height=if ((x \@ "height").length>0) (x \@ "height").toFloat else 1f
        )
      )

    val lowerLevelRastergGraphicsPaths=
      lowerLevelImagePaths.map(x =>
        SVGRaster(
          id=x.raster.attribute("id") match {
            case Some(idExists) => idExists.text
            case _ => "noID"
          },
          transformOps=TransformParser(x.raster \@ "transform"),
          groups = svgGroups.filter(a=>x.gIds.contains(a.id)),
          imageString=x.raster.toString,
          imageDString = x.raster \@ "xlink-href",
          x= if ((x.raster \@ "x").length>0) (x.raster \@ "x").toFloat else 0f,
          y= if ((x.raster \@ "y").length>0) (x.raster \@ "y").toFloat else 0f,
          width = if ((x.raster \@ "width").length>0) (x.raster \@ "width").toFloat else 1f,
          height= if ((x.raster \@ "height").length>0) (x.raster \@ "height").toFloat else 1f
        )
      )


    (topLevelRasterGraphicsPaths++lowerLevelRastergGraphicsPaths).foreach(x=>println(x.id,x.groups.map(a=>a.id)))
    Seq.empty[SVGRaster]

  }

  def iterateOverGroups(tlGs:NodeSeq,imageGIDs:Seq[ImageGroups],gIdMap:Map[String,Seq[String]]):Seq[ImageGroups]=
    if (tlGs.length==0) imageGIDs
    else{
      val newGId=tlGs.head \@ "id" //TODO: What happens if the group doesn't have an ID?
      val parentGids=gIdMap.get(newGId) match{
          case(Some(a))=> a
          case None => Seq.empty[String]
        }

      val thisImageGIDs=imageGIDs ++ (tlGs.head \ "text").map(x=>ImageGroups(x,newGId +: parentGids))
      iterateOverGroups(tlGs.tail ++ tlGs.head \"g",thisImageGIDs, gIdMap)

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
