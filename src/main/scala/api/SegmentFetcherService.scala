package api

import akka.actor.{Actor, Props}
import service.AppnexusSegmentService
import spray.http.MediaTypes._
import spray.routing._
import util.{ServicesConfig, Logging}

class SegmentFetcherActor extends Actor with SegmentFetcherService {
  def actorRefFactory = context
  def receive = runRoute(segmentFetcherRoute)
}


trait SegmentFetcherService extends HttpService with Logging {

  val segmentFetcherRoute =
    path("segments") {
      //requestContext =>
        //respondWithMediaType(`application/json`)
        //val appnexusSegmentService = actorRefFactory.actorOf(Props[AppnexusSegmentService])
        //val appnexusSegmentService = actorRefFactory.actorOf(Props(new AppnexusSegmentService(requestContext)))
        //appnexusSegmentService ! AppnexusSegmentService.appnexusSegments()
        val segmentsFile = ServicesConfig.appnexusConfig("segments.output.file.path")
        getFromFile(segmentsFile)
    }
}