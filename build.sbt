
organization := "edu.ist.psu.sagnik.research"

name := "inkscape-svg-processing"

version := "0.0.1-SNAPSHOT"

javacOptions += "-Xlint:unchecked"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Sonatype Shapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "JAI releases" at "http://maven.geotoolkit.org/"
)


libraryDependencies ++= Seq(
  //jackson for json
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.json4s" %% "json4s-jackson" % "3.2.10",
  // pdf parsing libraries
  "org.apache.pdfbox"    %  "pdfbox"          %  "1.8.7",
  "org.apache.tika"      %  "tika-bundle"     %  "1.6",
  // testing
  "org.scalatest"        %% "scalatest"  %  "2.2.4",
  //log4j
  "log4j" % "log4j" % "1.2.15" excludeAll(
    ExclusionRule(organization = "com.sun.jdmk"),
    ExclusionRule(organization = "com.sun.jmx"),
    ExclusionRule(organization = "javax.jms")
    )
)

libraryDependencies += "javax.media" % "jai_core" % "1.1.3"

libraryDependencies += "commons-collections" % "commons-collections" % "3.2.1"

libraryDependencies += "org.scalanlp" %% "breeze" % "0.11.2"

libraryDependencies += "org.scalanlp" %% "breeze-natives" % "0.11.2"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")

fork := true

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

testOptions in Test += Tests.Argument("-oF")

fork in Test := false

parallelExecution in Test := false
