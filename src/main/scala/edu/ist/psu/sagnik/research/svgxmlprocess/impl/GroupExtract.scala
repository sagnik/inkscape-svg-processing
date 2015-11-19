package edu.ist.psu.sagnik.research.svgxmlprocess.impl

import edu.ist.psu.sagnik.research.svgxmlprocess.model.{groupTransformOp, SVGGroup, SVGPath}
import edu.ist.psu.sagnik.research.svgxmlprocess.reader.XMLReader

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
          transformOps = Seq.empty[groupTransformOp]
        )
    }

}
