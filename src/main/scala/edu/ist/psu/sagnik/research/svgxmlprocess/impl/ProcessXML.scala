package edu.ist.psu.sagnik.research.svgxmlprocess.impl

import edu.ist.psu.sagnik.research.svgxmlprocess.model.{pathOp, SVGPath}
import edu.ist.psu.sagnik.research.svgxmlprocess.reader.XMLReader

/**
 * Created by szr163 on 11/8/15.
 */
object ProcessXML {
  def apply(fileLoc:String)=getPaths(XMLReader(fileLoc))

  def getPaths(xmlContent:scala.xml.Elem):Seq[SVGPath]= {
    val topLevelpaths = xmlContent \ "path"
    topLevelpaths.map(x =>
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
  }

}
