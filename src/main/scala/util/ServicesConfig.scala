package util

import java.util.{HashMap => JHashMap}

import com.typesafe.config.ConfigFactory

/**
 * Created by anand on 03/11/14.
 */
object ServicesConfig {

  def appnexusConfig: Map[String, String] = {
    segmentFetcherConfig.get("appnexus").getOrElse(Map.empty)
  }

  def scheduledConfig: Map[String, String] = {
    segmentFetcherConfig.get("scheduler").getOrElse(Map.empty)
  }

  private[this] final lazy val segmentFetcherConfig: Map[String, Map[String, String]] = {
    import scala.collection.JavaConversions._
    ConfigFactory.load("segment-services.conf").root.unwrapped.asInstanceOf[JHashMap[String, JHashMap[String, String]]].toMap.mapValues(_.toMap[String, String])
  }
}
