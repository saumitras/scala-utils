package solr

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.CloudSolrClient

class Count extends App {


  val zkHost = "localhost:2181"
  val SOLR_HOST = "localhost"
  val SOLR_PORT = 8983


  val client = new CloudSolrClient(zkHost)
  client.connect()

  def getNumFound(c:String) = {
    val query = new SolrQuery()
    query.setParam("q","*:*")
    query.setParam("rows","0")
    query.setParam("shards.tolerant","true")

    client.setDefaultCollection(c)

    val response = client.query(query)
    val numFound = response.getResults.getNumFound
    println(s"NumFound for $c = $numFound")
    numFound
  }
}
