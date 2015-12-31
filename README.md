## InkScapeSVGtoPDFObjects

PDF and SVG are both vector graphics, with considerable differences. PDF doesn't have a proper **object oriented representation** making it extremely hard to process. This repository contains Scala code for generating such a representation from PDFs. We expect an SVG file as the input. This SVG is produced by InkScape by processing a page of a PDF (see src/test/resources/pg_0006.svg for an example). Then each path, text character and image in the SVG is converted into an object. For example, each _text path_ in SVG is converted into a stream of character objects with bounding box and font information. For more details about the data models, see _models_ directory in _pathparser, textparser and rasterparser_ packages. 

###TODO 

More README to follow, possibly a wiki.
                   

    
