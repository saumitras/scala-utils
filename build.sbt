name := "SolrUtils"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.apache.solr" % "solr-solrj" % "6.6.0",
  "commons-logging" % "commons-logging" % "1.1.1",
  "nl.grons" % "metrics-scala_2.11" % "3.5.5",
  "org.apache.curator" % "curator-client" % "2.7.0" withSources(),
  "org.apache.curator" % "curator-framework" % "2.7.0" withSources(),
  "org.apache.curator" % "curator-recipes" % "2.7.0" withSources()
)
