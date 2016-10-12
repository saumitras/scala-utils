package solr

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.{HttpClientBuilder, DefaultHttpClient}
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.CloudSolrClient
import scala.collection.JavaConversions._

object Delete {

  val zkHost = "localhost:2181"
  val SOLR_HOST = "localhost"
  val SOLR_PORT = 8983

  val mode = "regex"  //mode can be zero/all/regex
  val DELETE_ALL_REGEX = "collection1"

  val client = new CloudSolrClient(zkHost)
  client.connect()

  val cols = client.getZkStateReader.getClusterState.getCollections.toList
  println("Collections count = " + cols.length)

  //delete all collect
  if(mode.toUpperCase == "ALL")
    cols.foreach(delete(_))

  //delete collections which matches a regex
  if(mode.toUpperCase == "REGEX")
    cols.filter(_.matches(s".*$DELETE_ALL_REGEX.*")).foreach(delete(_))

  //delete all collections with Zero docs
  if(mode.toUpperCase == "ZERO") {
    val zeroCols = cols.filter(getNumFound(_) == 0)
    zeroCols.foreach(client.commit(_))
    zeroCols.filter(getNumFound(_) == 0).foreach(delete(_))
  }


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

  def delete(c:String) = {
    println(s"Deleting collection $c...")
    val url = s"http://$SOLR_HOST:$SOLR_PORT/solr/admin/collections?action=delete&name=$c"
    val client = HttpClientBuilder.create().build()
    val request: HttpGet = new HttpGet(url)
    val response: HttpResponse = client.execute(request)
    val code = response.getStatusLine().getStatusCode()
    println(code)
  }

  client.close()

}
