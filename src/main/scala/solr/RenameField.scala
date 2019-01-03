package solr

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.CloudSolrClient
import org.apache.solr.client.solrj.util.ClientUtils

object RenameField  {

/*  val sourceZk = "localhost:2181"
  val sourceCollection = "di-di-podv1"

  val targetZk = "localhost:2181"
  val targetCollection = "di-di-podv2"

  val sourceClient = new CloudSolrClient(sourceZk)
  sourceClient.setDefaultCollection(sourceCollection)

  val targetClient = new CloudSolrClient(targetZk)
  targetClient.setDefaultCollection(targetCollection)

  val query = new SolrQuery()
  query.setParam("q","*:*")
  query.setRows(20000)

  val resp = sourceClient.query(query)
  val docs = resp.getResults.iterator()

  while(docs.hasNext) {
    val doc = docs.next()
    doc.removeFields("_version_")

    val sysid = doc.getFieldValue("sysid")
    doc.removeFields("sysid")
    doc.setField("sysid1", sysid)

    targetClient.add(ClientUtils.toSolrInputDocument(doc))

  }

  targetClient.commit()

  targetClient.close()
  sourceClient.close()*/


}
