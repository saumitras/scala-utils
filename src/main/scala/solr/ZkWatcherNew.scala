package solr

import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode
import org.apache.curator.framework.state.{ConnectionState, ConnectionStateListener}
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.framework.recipes.cache.{PathChildrenCacheEvent, PathChildrenCacheListener, PathChildrenCache}
import org.apache.curator.retry.ExponentialBackoffRetry
import scala.collection.JavaConversions._

object ZkWatcherNew {
  private val retryPolicy = new ExponentialBackoffRetry(1000, 3)
  private val collectionsZNode = "/collections"
}

class ZkWatcherNew(zkHost:String, startWatcher:Boolean) {
  import ZkWatcherNew._

  private val curator = CuratorFrameworkFactory.newClient(zkHost, retryPolicy)
  private val collectionCache = new PathChildrenCache(curator, collectionsZNode, true)

  if(startWatcher) initWatcher()

  def registerListeners() = {
    collectionCache.getListenable.addListener(new PathChildrenCacheListener {
      override def childEvent(client: CuratorFramework, event: PathChildrenCacheEvent): Unit = {
        println("Change in path " + event.getData.getPath)
        println("Current collections: " + getAllCollections)
      }
    })

    curator.getConnectionStateListenable.addListener(new ConnectionStateListener {
      override def stateChanged(client: CuratorFramework, newState: ConnectionState): Unit = {
        println("Curator client state changed. New state: " + newState)
        if (newState.toString == "LOST") {
          println(s"Zookeeper client has lost connection. Manual intervention required. zkHost:$zkHost")
        }
      }
    })
  }

  def initWatcher() = {
    registerListeners()

    curator.start()
    collectionCache.start(StartMode.BUILD_INITIAL_CACHE)
  }

  def getAllCollections:List[String] = collectionCache.getCurrentData.iterator().toList.map(_.getPath.replaceAll(".*/",""))


}
