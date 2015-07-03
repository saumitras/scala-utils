package collection

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.CloudSolrClient
import scala.collection.JavaConversions._

object DeleteZeroCollection extends App {

  val zkHost = "localhost:2181"
  val SOLR_HOST = "localhost"
  val SOLR_PORT = 8983

  val client = new CloudSolrClient(zkHost)
  client.connect()

  val cols = client.getZkStateReader.getClusterState.getCollections.toList
  for(c <- cols) {
    println(s"\nChecking $c")
    client.setDefaultCollection(c)
    val numFound = getNumFound(c)
    if(numFound == 0) {
      println(s"Commiting $c")
      client.commit(c)
      Thread.sleep(100)
      if(getNumFound(c) == 0) {
        delete(c)
      }
    }
  }

  def getNumFound(c:String) = {
    val query = new SolrQuery()
    query.setParam("q","*:*")
    query.setParam("rows","0")
    query.setParam("shards.tolerant","true")

    val response = client.query(query)
    val numFound = response.getResults.getNumFound
    println(s"NumFound for $c = $numFound")
    numFound
  }

  def delete(c:String) = {
    println(s"Deleting collection $c...")
    val url = s"http://$SOLR_HOST:$SOLR_PORT/solr/admin/collections?action=delete&name=$c"
    val client = new DefaultHttpClient()
    val request: HttpGet = new HttpGet(url)
    val response: HttpResponse = client.execute(request)
    val code = response.getStatusLine().getStatusCode()
    println(code)
  }

  client.close()

}
