## InkScapeSVG to PDFObjects

PDF and SVG are both vector graphics, with considerable differences. PDF doesn't have a flat **object oriented representation** making it extremely hard to process. This repository contains Scala code for generating such a representation from PDFs. We expect an SVG file as the input. This SVG is produced by InkScape by converting a page of a PDF (see [this](`src/test/resources/pg_0006.svg`) for an example). 

SVG produced by InkScape contains many information such as grouping elements, multiple transformation operations such as "rotate", "scale" etc. This is a fairly complicated hierarchical representation, as commonly found in most XML files. For most purposes, we need the bounding boxes for the paths, characters and images embedded in the PDF. SVG standard doesn't provide such bounding boxes; they must be calculated.

This repository contains parser combinators (SVG operations follow EBNF syntax) that take an InkScape SVG and convert each graphics path, text path and image to an object. The hierarchical tree structure is flattened first; for each path, character and image we find out the groups it belongs to. 

Each _text path_ in the SVG is then converted into a stream of character objects with bounding box and font information, which is inferred from the font. 

Each _graphics path_ is converted into an object with a sequence of path commands, sequence of transformation matrices and a bounding box. The bounding box calculation takes all transform operations (including the ones coming from groups) into consideration. 

For more details about the data models, see _models_ directories in _pathparser, textparser and rasterparser_ packages. Scala offers excellent libraries for writing parser combinators (one of the reasons it is heavily used in DSLs). While the parsers are _fairly_ generic, I have only tested them on SVGs produced by InkScape, hence the name. 


### Application and Test

An application of this package is https://github.com/sagnik/svgimagesfromallenaipdffigures. 

**System dependencies for this repository are: 1. InkScape (version 0.91, tested on Ubuntu and version 0.47, Mar 4 2015, tested on RedHat) and 2. Pdftoolkit (https://www.pdflabs.com/tools/pdftk-server/), both available for Windows, Mac and Linux systems.** Input for this package is a PDF file containing some figures and tables and a directory containing JSON files with their locations (page number, bounding box in the page). This PDF is split in one page PDFs (using pdftk) and converted to SVG using InkScape. Then, these images are processed  to produce the object oriented representation. Finally, we create SVG files for the figures and the tables in the PDF. There are multiple benfits of creating such SVG files. For an example, see our ongoing work on natural language summary generation for scholarly line graphs: http://personal.psu.edu/szr163/hassan/hassan-Figure-2.html.  

To see the application, clone the above mentioned repository and follow the directions there.

You can use this repository by adding the following library dependency `"edu.psu.sagnik.research" %% "inkscapesvgprocessing" % "<current version number from Build.sbt>"`. Make sure you have https://oss.sonatype.org/content/repositories/releases/ in your resolvers.

### TODO 

More README to follow, possibly a wiki.
                  
### NOTE

This is a work in progress and the code will be changed frequently.

    
