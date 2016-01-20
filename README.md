## InkScapeSVGtoPDFObjects

PDF and SVG are both vector graphics, with considerable differences. PDF doesn't have a proper **object oriented representation** making it extremely hard to process. This repository contains Scala code for generating such a representation from PDFs. We expect an SVG file as the input. This SVG is produced by InkScape by processing a page of a PDF (see src/test/resources/pg_0006.svg for an example). 

SVG produced by InkScape contains many information such as grouping elements, multiple transformation opertaion such as ``rotate", "scale" etc. This is a fairly complictaed representation. For most purposes, we need bounding boxes for paths (vector graphics), characters and images embedded in the PDF. 

This repository contains SVG parsers (such parsers are available in JavaScript, but not in Scala) that take the an SVG and converts each graphics path, text path and image in that to an object. While the parsers are generic, I have only tested them on SVG produced by InkScape, hence the name. For example, each _text path_ in SVG is converted into a stream of character objects with bounding box and font information. Similarly, each graphics path is converted into an object with commands and transformation matrix. For more details about the data models, see _models_ directory in _pathparser, textparser and rasterparser_ packages. 

#### Application and Test

An application of this package is https://github.com/sagnik/svgimagesfromallenaipdffigures. **System dependencies for this repository are: 1. InkScape (version 0.91, the version is important) and 2. Pdftoolkit (https://www.pdflabs.com/tools/pdftk-server/), both available for Windows, Mac and Linux systems.** Input for this package is a PDF file containing some figures and tables and a directory containing JSON files with their locations (page number, bounding box in the page). This PDF is split in one page PDFs (using pdftk) and converted to SVG using InkScape. Then, these images are processed  to produce the object oriented representation. Finally, we create the SVG files for the figures and the tables in the PDF. There are multiple benfits of creating such SVG files that will be discussed in a paper sometime later.

To see the application, clone the above mentioned repository and follow the directions there.
  
###TODO 

More README to follow, possibly a wiki.
                   

    
