package edu.ist.psu.sagnik.research.inkscapesvgprocessing.impl

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.SVGGroup
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.reader.XMLReader
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.impl.TransformParser

/**
 * Created by sagnik on 11/11/15.
 */
object GroupExtract {
  def apply(fileLoc:String)=getGroups(XMLReader(fileLoc))

  def getGroups(xmlContent:scala.xml.Elem):Seq[SVGGroup]=
    (xmlContent \\ "g").map{
      x=>
        SVGGroup(
          id= x \@ "id",
          gtContent = x \@ "transform",
          gContent= x.toString,
          transformOps = TransformParser(x \@ "transform")
        )
    }

  def main(args: Array[String]):Unit={
    val loc="src/test/resources/pg_0006.svg"
    val groups=GroupExtract(loc)
    groups.foreach(a=>println(s"[group id]: ${a.id} [tramsformops]: ${a.transformOps}"))
  }
}
