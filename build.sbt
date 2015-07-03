name := "SolrUtils"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.apache.solr" % "solr-solrj" % "5.2.1" withSources(),
  "commons-logging" % "commons-logging" % "1.1.1"
)
