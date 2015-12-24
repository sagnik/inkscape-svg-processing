package edu.ist.psu.sagnik.research.inkscapesvgprocessing.rasterparser.model

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.{Rectangle, SVGGroup}
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.model.TransformCommand

/**
 * Created by sagnik on 12/24/15.
 */
case class SVGRasterIm(id:String, imageDString:String,
                          groups:Seq[SVGGroup], transformOps:Seq[TransformCommand],
                          x:Float, y: Float, width:Float, height:Float
                      )

case class SVGRaster(id:String, imageString:String, bb:Rectangle)