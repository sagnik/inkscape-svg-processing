package edu.ist.psu.sagnik.research.inkscapesvgprocessing.rasterparser.model

import edu.ist.psu.sagnik.research.inkscapesvgprocessing.model.SVGGroup
import edu.ist.psu.sagnik.research.inkscapesvgprocessing.transformparser.model.TransformCommand

/**
 * Created by sagnik on 12/24/15.
 */
case class SVGRaster(id:String, imageDString:String, imageString:String,
                          groups:Seq[SVGGroup], transformOps:Seq[TransformCommand],
                          x:Float, y: Float, width:Float, height:Float)