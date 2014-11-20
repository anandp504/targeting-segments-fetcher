package service

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import api.SegmentFetcherActor
import spray.can.Http
import akka.util.Timeout
import util.ServicesConfig
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Boot extends App {

  implicit val system = ActorSystem("on-spray-can")
  val segmentFetcherService = system.actorOf(Props[SegmentFetcherActor], "segment-fetcher-service")
  implicit val timeout = Timeout(50.seconds)
  IO(Http) ! Http.Bind(segmentFetcherService, interface = "localhost", port = 8080)

  val scheduler = system.scheduler
  val schedulerInterval = ServicesConfig.scheduledConfig("segment.fetcher.schedule").toInt
  scheduler.scheduleOnce(schedulerInterval.minutes) {
    val appnexusSegmentService = system.actorOf(Props[AppnexusSegmentService])
    appnexusSegmentService ! AppnexusSegmentService.appnexusSegments()
  }

}
