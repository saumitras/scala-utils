package solr

import org.apache.solr.client.solrj.impl.CloudSolrClient
import scala.collection.JavaConversions._

class Commit  {

  val zkHost = "localhost:2181"
  val sleepDuration = 1000 * 5 //milliseconds

  val client = new CloudSolrClient(zkHost)
  client.connect()

  val cols = client.getZkStateReader.getClusterState.getCollections.toList
  println(s"Found ${cols.size} collections")

  cols.zipWithIndex.foreach { case(collection,index) =>
    println(s"[$index] Committing $collection")
    try {
      client.setDefaultCollection(collection)
      client.commit()
      Thread.sleep(sleepDuration)
    } catch {
      case ex:Exception =>
        ex.printStackTrace()
        println(s"Failed to commit $collection")
    }
  }

  println("Done")

}
