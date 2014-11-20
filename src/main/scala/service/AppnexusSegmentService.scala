package service

import java.io.PrintWriter

import akka.actor.Actor
import akka.util.Timeout
import org.json4s.JsonAST.JValue
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import service.AppnexusSegmentService.appnexusSegments
import spray.http._
import util.{HttpUtils, JsonUtils, Logging, ServicesConfig}

import scala.collection.immutable.StringOps
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
 * Created by anand on 03/11/14.
 */
object AppnexusSegmentService {

  private var authToken = None: Option[String]
  private val AppnexusBaseUrl = ServicesConfig.appnexusConfig("appnexus.host")
  private val AppnexusUserName = ServicesConfig.appnexusConfig("appnexus.username")
  private val AppnexusPassword = ServicesConfig.appnexusConfig("appnexus.password")
  private val AuthJsonPayload = new StringOps("{\"auth\": {\"username\" : \"%s\", \"password\" : \"%s\"}}").format(AppnexusUserName, AppnexusPassword)

  case class appnexusSegments()

}

class AppnexusSegmentService() extends Actor with Logging {

  implicit lazy val jsonFormats = org.json4s.DefaultFormats
  implicit val system = context.system
  system.dispatcher

  implicit val timeout: Timeout = Timeout(50.second)


  def receive = {
    case appnexusSegments() => getAppnexusSegments()
  }

  def getAppnexusSegments() = {

    val advertiserHttpResponse = getAdvertisers()
    val advertiserIds = JsonUtils.parseAdvertiserIds(advertiserHttpResponse.entity.asString)

    val appnexusSegments = {
      val segments = ListBuffer[String]()

      for (advertiserId <- advertiserIds) {
        /*
         * spawn multiple threads to fetch different profiles in the list of campaigns
         * obtained parallely.
         */
        val profileFutureList = appnexusCampaignFuture(advertiserId.intValue()).map {
          profile => profile.map(profileId => appnexusProfileFuture(profileId.intValue()))
        }
        val activeSegments = profileFutureList.flatMap(segmentList => Future.sequence(segmentList))
        Await.result(activeSegments, Duration.Inf)
        activeSegments.foreach(innerList => innerList.foreach(segmentList => segments ++= segmentList))
      }

      //Write the segments fetched from Appnexus to a file
      val segmentsWriter = new PrintWriter(ServicesConfig.appnexusConfig("segments.output.file.path"))
      try {
        val json = ("cm" -> segments.toSet)
        segmentsWriter.print(Serialization.write(json))
      } finally {
        segmentsWriter.close()
      }
    }

    //Fetch all campaigns for an advertiser and parse the profile_ids
    def appnexusCampaignFuture(advertiserId: Int): Future[List[BigInt]] = Future {
      val campaignHttpResponse = getCampaigns(advertiserId.intValue())
      JsonUtils.parseProfileIds(campaignHttpResponse.entity.asString)
    }

    //Fetch profile object and then parse segment names
    def appnexusProfileFuture(profileId: Int): Future[List[String]] = Future {
      val profileHttpResponse = getProfiles(profileId.intValue())
      JsonUtils.parseSegmentNames(profileHttpResponse.entity.asString)
    }
  }

  /**
   * This method authenticates (in case an auth token has expired or has not been set) and sets the
   * auth token to be used for subsequent Appnexus requests
   */
  def auth() = {
    val httpResponse = HttpUtils.post("auth", AppnexusSegmentService.AuthJsonPayload.toString)
    setAuthToken(httpResponse.entity.asString)
  }

  /**
   * Fetches all active advertisers associated with a particular authenticated Appnexus account
   * @return HttpResponse
   */
  private def getAdvertisers(): HttpResponse = {

    val queryParams = Map("state" -> "active")
    var advertiserHttpResponse = HttpUtils.get("advertiser", queryParams, List(HttpHeaders.RawHeader("Authorization", AppnexusSegmentService.authToken.getOrElse(None.toString))))
    log.info("HTTPRESPONSE = " + advertiserHttpResponse.entity.asString)
    val errorId = JsonUtils.parseErrorId(advertiserHttpResponse.entity.asString)
    errorId match {
      case Some("NOAUTH") => {
        auth()
        advertiserHttpResponse = HttpUtils.get("advertiser", queryParams, List(HttpHeaders.RawHeader("Authorization", AppnexusSegmentService.authToken.getOrElse(None.toString))))
      }
      case _ => log.debug("Auth Token was valid. No need to authenticate again...")
    }
    log.info("Advertiser data fetch complete....")
    advertiserHttpResponse
  }

  /**
   * Fetches all the active campaigns for a given advertiser_id
   * @param advertiserId
   * @return HttpResponse
   */
  private def getCampaigns(advertiserId: Int): HttpResponse = {

    val queryParams = Map("advertiser_id" -> advertiserId.toString, "state" -> "active")
    var campaignHttpResponse = HttpUtils.get("campaign", queryParams, List(HttpHeaders.RawHeader("Authorization", AppnexusSegmentService.authToken.getOrElse(None.toString))))
    val errorId = JsonUtils.parseErrorId(campaignHttpResponse.entity.asString)
    errorId match {
      case Some("NOAUTH") => {
        auth()
        campaignHttpResponse = HttpUtils.get("advertiser", queryParams, List(HttpHeaders.RawHeader("Authorization", AppnexusSegmentService.authToken.getOrElse(None.toString))))
      }
      case _ => log.debug("Auth Token was valid. No need to authenticate again...")
    }
    log.info("Campaign data fetch complete for AdvertiserID " + advertiserId)
    campaignHttpResponse
  }

  /**
   * Fetches a Appnexus Profile Object for a given profile_id
   * @param profileId
   * @return HttpResponse
   */
  private def getProfiles(profileId: Int): HttpResponse = {
    val queryParams = Map("id" -> profileId.toString, "state" -> "active")
    var profileHttpResponse = HttpUtils.get("profile", queryParams, List(HttpHeaders.RawHeader("Authorization", AppnexusSegmentService.authToken.getOrElse(None.toString))))
    val errorId = JsonUtils.parseErrorId(profileHttpResponse.entity.asString)
    errorId match {
      case Some("NOAUTH") => {
        auth()
        profileHttpResponse = HttpUtils.get("profile", queryParams, List(HttpHeaders.RawHeader("Authorization", AppnexusSegmentService.authToken.getOrElse(None.toString))))
      }
      case _ => log.debug("Auth Token was valid. No need to authenticate again...")
    }
    log.info("Profile data fetch complete for Profile ID " + profileId)
    profileHttpResponse
  }

  /**
   * Sets the Auth Token to be reused for subsequent requests to Appnexus services
   * @param response - HttpResponse String from Appnexus Authentication request
   */
  private def setAuthToken(response: String) = {
    val parsedResponse: JValue = parse(response)
    AppnexusSegmentService.authToken = (parsedResponse \ "response" \ "token").extractOpt[String]
  }

}


