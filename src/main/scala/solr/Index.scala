package solr

import org.apache.solr.client.solrj.impl.CloudSolrClient
import org.apache.solr.common.SolrInputDocument;



object Index extends App {

	val zkHostString = "localhost:2181"
	val RANGE = 30
	val solr = new CloudSolrClient.Builder().withZkHost(zkHostString).build()
	solr.setDefaultCollection("a1")
	
	val r = scala.util.Random


	try {
		for(i <- 1 to 50000) {
			println(i)
			val document = new SolrInputDocument();
			document.addField("id", System.currentTimeMillis + "" + i)
			document.addField("value1_tl", r.nextInt(RANGE));
			//document.addField("value1_tl", 70)

			solr.add(document);

		}
	} catch {
		case ex:Exception => 
			println(ex.getMessage());
	}
		
	solr.commit()
	solr.close()
	
	
}
