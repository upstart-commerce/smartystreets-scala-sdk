package com.upstartcommerce.smartystreets

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.RejectionError
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.upstartcommerce.smartystreets.common.exception._
import com.upstartcommerce.smartystreets.common.{MatchedAddress, SmartyStreetsConfig, USAddressRequest}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.{JsArray, Json}

import scala.concurrent.{ExecutionContext, Future}

/*
Copyright 2019 UpStart Commerce, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

/**
  * Provides integration with SmartyStreets address verification REST API
  *
  * @author Yan Doroshenko
  */
trait SmartyStreetsIntegration extends PlayJsonSupport {
  val smartyStreetsConfig: SmartyStreetsConfig

  /**
    * Verifies a single address
    *
    * @param address Address to verify
    *
    * @return Future, containing matched addresses if any
    */
  def verifyAddress(address: USAddressRequest)
                   (implicit ec: ExecutionContext, as: ActorSystem, am: ActorMaterializer): Future[Seq[MatchedAddress]] = {
    verifyAddresses(Seq(address)).map(_.head._2)
  }

  /**
    * Verifies a list of addresses using the SmartyStreets address verification REST API
    *
    * @param addresses Addresses to verify
    *
    * @return Future, containing verirication data grouped by the address
    */
  def verifyAddresses(addresses: Seq[USAddressRequest])
                     (implicit ec: ExecutionContext, as: ActorSystem, am: ActorMaterializer): Future[Seq[(USAddressRequest, Seq[MatchedAddress])]] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(
      HttpRequest()
        .withMethod(HttpMethods.POST)
        .withUri(s"${smartyStreetsConfig.endpoint}/street-address?auth-id=${smartyStreetsConfig.authId}&auth-token=${smartyStreetsConfig.authToken}")
        .withHeaders(
          headers.Host("us-street.api.smartystreets.com")
        )
        .withEntity(HttpEntity(
          ContentType(MediaTypes.`application/json`),
          Json.stringify(JsArray(addresses.map(USAddressRequest.f.writes)))
        ))
    )

    import StatusCodes._
    responseFuture
      .flatMap {
        case HttpResponse(Unauthorized, _, HttpEntity.Strict(_, data), _) =>
          Future.failed(UnauthorizedException(decodeString(data)))
        case HttpResponse(PaymentRequired, _, HttpEntity.Strict(_, data), _) =>
          Future.failed(PaymentRequiredException(decodeString(data)))
        case HttpResponse(RequestEntityTooLarge, _, HttpEntity.Strict(_, data), _) =>
          Future.failed(RequestBodyTooLargeException(decodeString(data)))
        case HttpResponse(TooManyRequests, _, HttpEntity.Strict(_, data), _) =>
          Future.failed(TooManyRequestsException(decodeString(data)))
        case HttpResponse(BadRequest, _, entity, _) =>
          val str = entity.dataBytes.runFold("")((l, r) => l + decodeString(r))
          str.flatMap { v =>
            Future.failed(BadRequestException(v))
          }
        case HttpResponse(OK, _, entity, _) =>
          val r = Unmarshal(entity)
          r.to[Seq[MatchedAddress]].recoverWith {
            case t: RejectionError =>
              Future.failed(DeserializationException(t.rejection.toString, t))
          }
            .map(merge(addresses, _))
      }
  }

  /**
    * Decodes ByteString into String with UTF-8 encoding
    *
    * @param bs ByteString
    *
    * @return Decoded string
    */
  private def decodeString(bs: ByteString) = bs.decodeString("utf-8")

  /**
    * Groups response data by the input addresses
    *
    * @param addresses Input addresses
    * @param responses Response data
    *
    * @return Response data grouped by input address
    */
  private def merge(addresses: Seq[USAddressRequest], responses: Seq[MatchedAddress]): Seq[(USAddressRequest, Seq[MatchedAddress])] = {
    addresses.indices.map { i =>
      addresses(i) -> responses.filter(_.inputIndex == i)
    }
  }
}
