package solr

object YourApplication {
  val metricRegistry = new com.codahale.metrics.MetricRegistry()

  val a1 = new MetricsExample()
  a1.loadStuff()
  a1.loadStuff()
  a1.loadStuff()

  println(metricRegistry.getMetrics.get("solr.MetricsExample.loading"))
  println(metricRegistry.getTimers().get("solr.MetricsExample.loading").getCount)
  println(metricRegistry.getTimers().get("solr.MetricsExample.loading").getMeanRate)


}

trait Instrumented extends nl.grons.metrics.scala.InstrumentedBuilder {
  val metricRegistry = YourApplication.metricRegistry
}