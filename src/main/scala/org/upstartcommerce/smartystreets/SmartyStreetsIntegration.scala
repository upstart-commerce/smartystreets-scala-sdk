package org.upstartcommerce.smartystreets

import com.github.pjfanning.pekkohttpplayjson.PlayJsonSupport
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.model._
import org.apache.pekko.http.scaladsl.server.RejectionError
import org.apache.pekko.http.scaladsl.unmarshalling.Unmarshal
import org.apache.pekko.stream.Materializer
import org.apache.pekko.util.ByteString
import org.upstartcommerce.smartystreets.common.exception._
import org.upstartcommerce.smartystreets.common.{MatchedAddress, SmartyStreetsConfig, USAddressRequest}
import play.api.libs.json.{JsArray, Json}

import scala.concurrent.{ExecutionContext, Future}

/** Provides integration with SmartyStreets address verification REST API
  *
  * @author
  *   Yan Doroshenko
  */
trait SmartyStreetsIntegration extends PlayJsonSupport {
  val smartyStreetsConfig: SmartyStreetsConfig

  /** Verifies a single address
    *
    * @param address
    *   Address to verify
    *
    * @return
    *   Future, containing matched addresses if any
    */
  def verifyAddress(
      address: USAddressRequest
  )(implicit ec: ExecutionContext, as: ActorSystem, am: Materializer): Future[Seq[MatchedAddress]] = {
    verifyAddresses(Seq(address)).map(_.head._2)
  }

  /** Verifies a list of addresses using the SmartyStreets address verification REST API
    *
    * @param addresses
    *   Addresses to verify
    *
    * @return
    *   Future, containing verirication data grouped by the address
    */
  def verifyAddresses(
      addresses: Seq[USAddressRequest]
  )(implicit ec: ExecutionContext, as: ActorSystem, am: Materializer): Future[Seq[(USAddressRequest, Seq[MatchedAddress])]] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(
      HttpRequest()
        .withMethod(HttpMethods.POST)
        .withUri(
          s"${smartyStreetsConfig.endpoint}/street-address?auth-id=${smartyStreetsConfig.authId}&auth-token=${smartyStreetsConfig.authToken}"
        )
        .withHeaders(
          headers.Host("us-street.api.smartystreets.com")
        )
        .withEntity(
          HttpEntity(
            ContentType(MediaTypes.`application/json`),
            Json.stringify(JsArray(addresses.map(USAddressRequest.f.writes)))
          )
        )
    )

    import StatusCodes._
    responseFuture
      .flatMap {
        case HttpResponse(Unauthorized, _, HttpEntity.Strict(_, data), _) => Future.failed(UnauthorizedException(decodeString(data)))
        case HttpResponse(PaymentRequired, _, HttpEntity.Strict(_, data), _) => Future.failed(PaymentRequiredException(decodeString(data)))
        case HttpResponse(ContentTooLarge, _, HttpEntity.Strict(_, data), _) =>
          Future.failed(RequestBodyTooLargeException(decodeString(data)))
        case HttpResponse(TooManyRequests, _, HttpEntity.Strict(_, data), _) => Future.failed(TooManyRequestsException(decodeString(data)))
        case HttpResponse(BadRequest, _, entity, _) =>
          val str = entity.dataBytes.runFold("")((l, r) => l + decodeString(r))
          str.flatMap {
            v =>
              Future.failed(BadRequestException(v))
          }
        case HttpResponse(OK, _, entity, _) =>
          val r = Unmarshal(entity)
          r.to[Seq[MatchedAddress]]
            .recoverWith {
              case t: RejectionError => Future.failed(DeserializationException(t.rejection.toString, t))
            }
            .map(merge(addresses, _))
      }
  }

  /** Decodes ByteString into String with UTF-8 encoding
    *
    * @param bs
    *   ByteString
    *
    * @return
    *   Decoded string
    */
  private def decodeString(bs: ByteString) = bs.decodeString("utf-8")

  /** Groups response data by the input addresses
    *
    * @param addresses
    *   Input addresses
    * @param responses
    *   Response data
    *
    * @return
    *   Response data grouped by input address
    */
  private def merge(addresses: Seq[USAddressRequest], responses: Seq[MatchedAddress]): Seq[(USAddressRequest, Seq[MatchedAddress])] = {
    addresses.indices.map {
      i =>
        addresses(i) -> responses.filter(_.inputIndex == i)
    }
  }

}
