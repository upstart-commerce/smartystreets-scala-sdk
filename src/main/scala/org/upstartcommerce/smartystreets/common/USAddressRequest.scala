package org.upstartcommerce.smartystreets.common

import org.upstartcommerce.smartystreets.common.Match.Match
import org.upstartcommerce.smartystreets.common.util.Utils.JsonSnakeCase
import play.api.libs.json._

import scala.language.implicitConversions

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
  * Describes the US address verification request
  *
  * @param inputId      A unique identifier for this address, will be copied into the `inputId` field of the response
  * @param street       The street line of the address, or the entire address ("freeform" input). Freeform inputs should NOT include any form of country information
  * @param street2      Any extra address information (e.g., Leave it on the front porch.)
  * @param secondary    Apartment, suite, or office number
  * @param city         The city name
  * @param state        The state name or abbreviation
  * @param zipcode      The ZIP Code
  * @param lastline     City, state, and ZIP Code combined
  * @param addressee    The name of the recipient, firm, or company at this address
  * @param urbanization Only used with Puerto Rico
  * @param candidates   The maximum number of valid addresses returned when the input is ambiguous
  * @param `match`      The match output strategy to be employed for this lookup (strict by default). See [[Match]]
  *
  * @author Yan Doroshenko
  */
case class USAddressRequest(
                             inputId: Option[String] = None,
                             street: Option[String] = None,
                             street2: Option[String] = None,
                             secondary: Option[String] = None,
                             city: Option[String] = None,
                             state: Option[String] = None,
                             zipcode: Option[String] = None,
                             lastline: Option[String] = None,
                             addressee: Option[String] = None,
                             urbanization: Option[String] = None,
                             candidates: Int = 1,
                             `match`: Option[Match] = None
                           )

object USAddressRequest extends JsonSnakeCase {
  implicit val f: Format[USAddressRequest] = Json.format
}

/**
  * Enumerates the possible match values
  *    - '''Strict''' — The API will ONLY return candidates that are valid USPS addresses
  *    - '''Range''' — The API will return candidates that are valid USPS addresses, as well as invalid addresses with primary numbers that fall within a valid range for the street
  *    - '''Invalid''' — The API will return a single candidate for every properly submitted address, even if invalid or ambiguous
  *
  * @author Yan Doroshenko
  */
object Match extends Enumeration {
  type Match = Value

  protected case class Val(value: String) extends super.Val

  val Strict = Val("strict")
  val Range = Val("range")
  val Invalid = Val("invalid")

  implicit def valueToVal(v: Value): Val = v.asInstanceOf[Val]

  implicit val f: Format[Value] = new Format[Value] {
    override def writes(o: Match.Value): JsValue = JsString(o.value)

    override def reads(json: JsValue): JsResult[Match.Value] =
      JsSuccess(values.find(_.value == json.as[String]).get)
  }
}
