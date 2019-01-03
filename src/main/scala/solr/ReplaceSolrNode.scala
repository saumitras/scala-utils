package solr

import org.apache.solr.client.solrj.impl.CloudSolrClient
import scala.collection.JavaConversions._
import sys.process._
import java.io._


object ReplaceSolrNode  {

  val SOLR_DATA_DIR = "/home/gbp/solr/data/"
  val ZK_HOST = "localhost:2181/explorer"
  val NODE_TO_BE_REPLACED = "solr-35"

  val client = new CloudSolrClient(ZK_HOST)
  client.connect()

  val zk = client.getZkStateReader.getClusterState
  val collections = zk.getCollections.toList

  for(c <- collections) {

    val slices = zk.getCollection(c).getSlices.toList
    for(s <- slices) {
      for (r <- s.getReplicas) {
        val coreUrl = r.getCoreUrl
        val nodeName = r.getNodeName
        val name = r.getName
        val state = r.getState.toString

        if(coreUrl.contains(NODE_TO_BE_REPLACED)) {
          println(s"Creating directory for nodeName:$nodeName, name:$name, state:$state, coreUrl:$coreUrl")
          val shardName = coreUrl.replaceAll(".*solr/","").replaceAll("/","")
          val content = "numShards=4\n" +
            "name=" + shardName  + "\n" +
            "shard=" + coreUrl.replaceAll(".*_shard","shard").replaceAll("_.*","") + "\n" +
            s"collection=$c\n" +
            s"coreNodeName=$name"

          val cmd = Seq("mkdir", "-p", s"$SOLR_DATA_DIR/$shardName/data/")
          val resp = try {
            cmd.!
          } catch {
            case ex:Exception =>
              ex.printStackTrace()
              -1
          }

          if(resp == 0) {
            val file = s"$SOLR_DATA_DIR/$shardName/core.properties"
            val pw = new PrintWriter(new File(file))
            pw.write(content)
            pw.close()
          } else {
            println(s"Failed to create shard directory for $shardName. Wont proceed with creating core.properties")
          }

        }

      }

    }

  }

  client.close()



}


