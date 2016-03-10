## InkScapeSVGtoPDFObjects

PDF and SVG are both vector graphics, with considerable differences. PDF doesn't have a flat **object oriented representation** making it extremely hard to process. This repository contains Scala code for generating such a representation from PDFs. We expect an SVG file as the input. This SVG is produced by InkScape by processing a page of a PDF (see src/test/resources/pg_0006.svg for an example). 

SVG produced by InkScape contains many information such as grouping elements, multiple transformation opertaion such as ``rotate", "scale" etc. This is a fairly complictaed hierarchical representation, as commonly found in most XML files. For most purposes, we need bounding boxes for paths (vector graphics), characters and images embedded in the PDF. 

This repository contains SVG parsers (SVG operations follow EBNF syntax) that take the an InkScape SVG and converts each graphics path, text path and image to an object. The parsers are written using Scala as it offers excellent libraries for parser combinatory. 
While the parsers are _fairly_ generic, I have only tested them on SVG produced by InkScape, hence the name. For example, each _text path_ in SVG is converted into a stream of character objects with bounding box and font information. Similarly, each graphics path is converted into an object with commands, transformation matrix and a bounding box. For more details about the data models, see _models_ directory in _pathparser, textparser and rasterparser_ packages. 

#### Application and Test

An application of this package is https://github.com/sagnik/svgimagesfromallenaipdffigures. **System dependencies for this repository are: 1. InkScape (version 0.91, tested on Ubuntu and version 0.47, Mar 4 2015, tested on RedHat) and 2. Pdftoolkit (https://www.pdflabs.com/tools/pdftk-server/), both available for Windows, Mac and Linux systems.** Input for this package is a PDF file containing some figures and tables and a directory containing JSON files with their locations (page number, bounding box in the page). This PDF is split in one page PDFs (using pdftk) and converted to SVG using InkScape. Then, these images are processed  to produce the object oriented representation. Finally, we create SVG files for the figures and the tables in the PDF. There are multiple benfits of creating such SVG files. For an example, see our ongoing work on natural language summary generation for scholarly line graphs: http://personal.psu.edu/szr163/hassan/hassan-Figure-2.html.  

To see the application, clone the above mentioned repository and follow the directions there.

####TODO 

More README to follow, possibly a wiki.
                  
####NOTE

This is a work in progress and the code will be changed frequently.

    
