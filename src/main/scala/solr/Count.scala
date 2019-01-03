package solr

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.CloudSolrClient
import scala.collection.JavaConversions._


object Count extends App {


  val zkHost = "198.46.63.233:2186"
  val SOLR_HOST = "198.46.63.233"
  val SOLR_PORT = 8984


  val client = new CloudSolrClient(zkHost)
  client.connect()

  println("connected")
  val cols = client.getZkStateReader.getClusterState.getCollections.toList
  println(cols)
  cols.foreach(getNumFound)

  def getNumFound(c:String) = {
    val query = new SolrQuery()
    query.setParam("q","*:*")
    query.setParam("rows","0")
    query.setParam("shards.tolerant","true")

    client.setDefaultCollection(c)

    val response = client.query(query)
    val numFound = response.getResults.getNumFound
    println(s"$c $numFound")
    numFound
  }
}
