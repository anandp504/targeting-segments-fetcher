package util

import com.fasterxml.jackson.databind.JsonMappingException
import org.json4s.JsonAST._
import org.json4s.jackson.JsonMethods._

/**
 * Created by anand on 12/11/14.
 */
object JsonUtils extends Logging {

  implicit lazy val jsonFormats = org.json4s.DefaultFormats
  /**
   * parse the error_id from Appnexus JSON response
   * @param response - HttpResponse string from any Appnexus service HttpRequest
   * @return Option[String] - Returns an error_id if found in the HttpResponse, else returns None
   */
  def parseErrorId(response: String): Option[String] = {
    val parsedResponse: JValue = parse(response)
    (parsedResponse \ "response" \ "error_id").extractOpt[String]
  }

  /**
   *
   * @param response - HttpResponse string from Appnexus advertiser service HttpRequest
   * @return List[BigInt] - Returns a List of Advertiser IDs from the Appnexus HttpResponse
   */
  def parseAdvertiserIds(response: String): List[BigInt] = {
    try{
      val parsedResponse = parse(response)
      val advertisersNode = compact(render(parsedResponse \ "response" \ "advertisers"))
      val advertiserIdList = for {
        JObject(advertisers) <- parse(advertisersNode)
        JField("id", JInt(advertiserId)) <- advertisers
      } yield advertiserId
      advertiserIdList
    } catch {
        case jsonMapex: JsonMappingException => {
          log.error("Parsing AdvertiserIDs failed: ", jsonMapex)
          List()
        }
    }
  }

  /**
   * Profile IDs are associated at the campaign level. Profiles basically specify a set of rules
   * associated with a campaign. The Segments to be targeted are specified in a Profile
   * @param response - HttpResponse string from Appnexus campaign service HttpRequest
   * @return List[BigInt] - Returns a List of Profile IDs from the Appnexus HttpResponse
   */
  def parseProfileIds(response: String): List[BigInt] = {
    try {
      val parsedResponse = parse(response)
      for {
        JObject(campaigns) <- parsedResponse
        JField("profile_id", JInt(profileId)) <- campaigns
      } yield profileId
    } catch {
      case ex: JsonMappingException => {
        log.error("Parsing ProfileIds failed: ", ex)
        List()
      }
    }
  }

  /**
   * Parse the Segment Names from the segment_group_targets node in the Profile Object
   * @param response - HttpResponse string from Appnexus profile service HttpRequest
   * @return List[String] - Returns a List of Segment Strings from the Appnexus HttpResponse
   */
  def parseSegmentNames(response: String): List[String] = {

    val parsedResponse = parse(response)
    val segmentGroup = compact(render(parsedResponse \ "response" \ "profile" \"segment_group_targets"))
    //log.info("Segment Group = " + segmentGroup)
    var segments = List[String]()
    Option(segmentGroup) match {
      case Some(s) => {
        segments = for {
          JObject(profile) <- parse(segmentGroup)
          JField("name", JString(segmentName)) <- profile
        } yield segmentName
      }
      case None => log.debug("No segments found")
    }
    segments
  }

}
