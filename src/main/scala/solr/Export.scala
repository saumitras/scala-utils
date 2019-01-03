package solr

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrQuery.{ORDER, SortClause}
import org.apache.solr.client.solrj.impl.{CloudSolrClient, HttpSolrClient}
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.params.CursorMarkParams

object Export extends App {

  exportSolrData()

  def exportSolrData() = {

    val client = new CloudSolrClient("localhost:2181")
    client.setDefaultCollection("e1")

    val some_query = "*:*"
    val r = 2
    val q = (new SolrQuery(some_query))
      .setRows(r)
      .addSort("evt_date", ORDER.asc) //.setParam("sort","evt_date desc, namespace_id desc")
      .addSort("namespace_id", ORDER.asc)
      .setFields("namespace_id")
    var cursorMark = CursorMarkParams.CURSOR_MARK_START
    var done = false
    while (!done) {
      println("cursorMark " + cursorMark)
      q.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark)
      val rsp = client.query(q)
      val nextCursorMark = rsp.getNextCursorMark()
      processResult(rsp)
      if (cursorMark.equals(nextCursorMark)) {
        done = true
      }

      cursorMark = nextCursorMark

    }


  }

  def processResult(rsp: QueryResponse) = {
    println(rsp)
  }



}
