package solr



class MetricsExample() extends Instrumented {

  private[this] val loading = metrics.timer("loading")

  def loadStuff() = loading.time {
    val nums = 1 to 10000
    nums.map(x => x * x)
  }

  def workerThreadIsActive() = {
    true
  }
}

