package solr

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrQuery.SortClause
import org.apache.solr.client.solrj.impl.HttpSolrClient
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.params.CursorMarkParams

object Export {

  //exportSolrData()

  def exportSolrData() = {

    val client = new HttpSolrClient("http://localhost:8983/solr/collection/")
    val some_query = "*:*"
    val r = 100
    val q = (new SolrQuery(some_query))
      .setRows(r)
      .setSort(SortClause.asc("id"))
      .setFields("id")
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
