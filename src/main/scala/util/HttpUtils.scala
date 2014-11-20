package util

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.io.IO
import akka.util.Timeout
import shapeless.~>
import scala.concurrent.duration._
import spray.can.Http
import spray.client.pipelining._
import spray.http.HttpCharsets._
import spray.http.MediaTypes._
import spray.http._

import scala.concurrent.{ExecutionContext, Await, Future}

/**
 * Created by anand on 12/11/14.
 */
object HttpUtils extends Logging {

  val appnexusHost = ServicesConfig.appnexusConfig("appnexus.host")

  def post(requestRelativeUri: String, jsonPayLoad: String)
          (implicit ec: ExecutionContext, actorSystem: ActorSystem) : HttpResponse = {

    implicit val timeout: Timeout = Timeout(20.second)

    val uri: Uri = Uri(requestRelativeUri)
    val request: HttpRequest = Post(uri, jsonPayLoad)

    val pipeline: Future[SendReceive] = {
      for {
        Http.HostConnectorInfo(connector, _) <- IO(Http) ? Http.HostConnectorSetup(appnexusHost)
      } yield sendReceive(connector)
    }
    Await.result(pipeline.flatMap { client => client.apply(request)}, 15.seconds)
  }

  def get(requestRelativeUri: String, queryParams: Map[String, String] = Map(), httpHeaders: List[HttpHeader] = Nil)
         (implicit ec: ExecutionContext, actorSystem: ActorSystem) : HttpResponse = {

    implicit val timeout: Timeout = Timeout(40.second)

    val uri: Uri = Uri(requestRelativeUri).withQuery(queryParams)
    val request: HttpRequest = Get(uri).withHeaders(httpHeaders)
    val pipeline: Future[SendReceive] = {
      for {
        Http.HostConnectorInfo(connector, _) <- IO(Http) ? Http.HostConnectorSetup(appnexusHost)
      } yield sendReceive(connector)
    }
    Await.result(pipeline.flatMap { client => client.apply(request)}, 15.seconds)
  }

}
