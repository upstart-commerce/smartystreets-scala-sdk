package org.upstartcommerce.smartystreets.common

import org.upstartcommerce.smartystreets.common.DPVFootnote.DPVFootnote
import org.upstartcommerce.smartystreets.common.DPVMatchCode.DPVMatchCode
import org.upstartcommerce.smartystreets.common.ELOTSort.ELOTSort
import org.upstartcommerce.smartystreets.common.Footnote.Footnote
import org.upstartcommerce.smartystreets.common.LACSLinkCode.LACSLinkCode
import org.upstartcommerce.smartystreets.common.LACSLinkIndicator.LACSLinkIndicator
import org.upstartcommerce.smartystreets.common.Precision.Precision
import org.upstartcommerce.smartystreets.common.RecordType.RecordType
import org.upstartcommerce.smartystreets.common.ResidentialDeliveryIndicator.ResidentialDeliveryIndicator
import org.upstartcommerce.smartystreets.common.ZipType.ZipType
import org.upstartcommerce.smartystreets.common.util.Utils.CharBoolean.CharBoolean
import org.upstartcommerce.smartystreets.common.util.Utils.JsonSnakeCase
import play.api.libs.json._

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

/** Describes the response structure, returned by the SmartyStreets API
  *
  * @param inputId
  *   `inputId` provided in the request object.
  * @param inputIndex
  *   The order in which the request object was submitted with the others (0 if alone)
  * @param candidateIndex
  *   A request object can match multiple valid addresses. This ties the candidates to the input index
  * @param addressee
  *   Intended recipient at an address
  * @param deliveryLine1
  *   The first delivery line (usually the street address). This can include any of the following:
  *   - Primary Number
  *   - Street Name
  *   - Street Predirection
  *   - Street Postdirection
  *   - Street Suffix
  *   - Secondary Number
  *   - Secondary Designator
  *   - PMB Designator
  *   - PMB Number
  * @param deliveryLine2
  *   The second delivery line (if needed). It is common for this field to remain empty, but it may contain a private mailbox number
  * @param lastLine
  *   City, state, and ZIP Code combined
  * @param deliveryPointBarcode
  *   12-digit POSTNET™ barcode; consists of 5-digit ZIP Code, 4-digit add-on code, 2-digit delivery point, and 1-digit check digit
  * @param components
  *   Address components, see [[Components]]
  * @param metadata
  *   Response metadata, see [[Metadata]]
  * @param analysis
  *   Analytics data, see [[Analysis]]
  *
  * @author
  *   Yan Doroshenko
  */
case class MatchedAddress(
    inputId: Option[String],
    inputIndex: Int,
    candidateIndex: Int,
    addressee: Option[String],
    deliveryLine1: Option[String],
    deliveryLine2: Option[String],
    lastLine: Option[String],
    deliveryPointBarcode: Option[String],
    components: Components,
    metadata: Metadata,
    analysis: Analysis
)

object MatchedAddress extends JsonSnakeCase {
  implicit val f: Format[MatchedAddress] = Json.format
}

/** Describes the components of the address matched by SmartyStreets
  *
  * @param urbanization
  *   Primarily for Puerto Rican addresses; a very important component which contains area, sector, or development within a geographic area;
  *   should be included after the name of the recipient
  * @param primaryNumber
  *   The house, PO Box, or building number
  * @param streetName
  *   The name of the street
  * @param streetPredirection
  *   Directional information before a street name (N, SW, etc.)
  * @param streetPostdirection
  *   Directional information after a street name (N, SW, etc.)
  * @param streetSuffix
  *   Abbreviated value describing the street (St, Ave, Blvd, etc.)
  * @param secondaryNumber
  *   Apartment or suite number, if any
  * @param secondaryDesignator
  *   Describes location within a complex/building (Ste, Apt, etc.)
  * @param extraSecondaryNumber
  *   Descriptive information about the location of a building within a campus
  * @param extraSecondaryDesignator
  *   Description of the location type within a campus
  * @param pmbDesignator
  *   Private mailbox unit designator (assigned by a CMRA)
  * @param pmbNumber
  *   The private mailbox number, assigned by a CMRA
  * @param cityName
  *   The USPS-preferred city name for this particular address, or an acceptable alternate if provided by the user
  * @param defaultCityName
  *   The default city name for this 5-digit ZIP Code
  * @param stateAbbreviation
  *   The two-letter state abbreviation
  * @param zipcode
  *   The 5-digit ZIP Code
  * @param plus4Code
  *   The 4-digit add-on code (more specific than 5-digit ZIP)
  * @param deliveryPoint
  *   The last two digits of the house/box number, unless an "H" record is matched, in which case this is the secondary unit number
  *   representing the delivery point information to form the delivery point barcode (DPBC)
  * @param deliveryPointCheckDigit
  *   Correction character, or check digit, for the 11-digit barcode
  *
  * @author
  *   Yan Doroshenko
  */
case class Components(
    urbanization: Option[String],
    primaryNumber: Option[String],
    streetName: Option[String],
    streetPredirection: Option[String],
    streetPostdirection: Option[String],
    streetSuffix: Option[String],
    secondaryNumber: Option[String],
    secondaryDesignator: Option[String],
    extraSecondaryNumber: Option[String],
    extraSecondaryDesignator: Option[String],
    pmbDesignator: Option[String],
    pmbNumber: Option[String],
    cityName: Option[String],
    defaultCityName: Option[String],
    stateAbbreviation: Option[String],
    zipcode: Option[String],
    plus4Code: Option[String],
    deliveryPoint: Option[String],
    deliveryPointCheckDigit: Option[String]
)

object Components extends JsonSnakeCase {
  implicit val f: Format[Components] = Json.format
}

/** Describes the response metadata
  *
  * @param recordType
  *   Indicates the type of record that was matched. Only given if a DPV match is made. See [[RecordType]]
  * @param zipType
  *   Indicates the type of the ZIP Code for the address that was matched. Only given if a 5-digit match is made. See [[ZipType]]
  * @param countyFips
  *   The 5-digit county FIPS (Federal Information Processing Standards) code. It is a combination of a 2-digit state FIPS code and a
  *   3-digit county code assigned by the NIST (National Institute of Standards and Technology).
  * @param countyName
  *   The name of the county the address is in
  * @param ewsMatch
  *   Early warning system flag; a positive result indicates the street of the address is not yet ready for mail delivery and that the
  *   address will soon be added to the master ZIP+4 file in the coming weeks or months. This commonly occurs for new streets or streets
  *   undergoing a name change.
  *
  *   - '''true''' — The address was flagged by EWS, preventing a ZIP+4 match
  *   - '''[empty]''' — Address was not flagged by EWS
  * @param carrierRoute
  *   The postal carrier route for the address
  * @param congressionalDistrict
  *   The congressional district to which the address belongs. Output will be two digits from 01 - 53 or "AL." "AL" means that the entire
  *   state (or territory) is covered by a single congressional district. These include Alaska, Delaware, Montana, North Dakota, South
  *   Dakota, Vermont, Wyoming, Washington DC, Virgin Islands, and other territories
  * @param buildingDefaultIndicator
  *   Indicates whether the address is the "default" address for a building; for example, the main lobby. See [[util.Utils.CharBoolean]]
  * @param rdi
  *   Residential Delivery Indicator (residential or commercial). See [[ResidentialDeliveryIndicator]]
  * @param elotSequence
  *   [[https://ribbs.usps.gov/index.cfm?page=elot eLOT]] (Enhanced Line of Travel) 4-digit sequence number
  * @param elotSort
  *   [[https://ribbs.usps.gov/index.cfm?page=elot eLOT]] (Enhanced Line of Travel) product was developed to give mailers the ability to
  *   sort their mailings by line of travel sequence. See [[ELOTSort]]. Empty means address was not submitted for eLOT
  * @param latitude
  *   The horizontal component used for geographic positioning. Empty for military or diplomatic addresses
  * @param longitude
  *   The vertical component used for geographic positioning. Empty for military or diplomatic addresses
  * @param precision
  *   Indicates the precision of the latitude and longitude values. See [[Precision]]
  *
  * '''Note''': Concerning addresses for which the ZIP9 precision is not available, the ZIP# precision is interpolated based on neighboring
  * addresses. Thus, ZIP7 is an average of all the lat/long coordinates of nearby ZIP Codes that share those first 7 digits.
  * @param timeZone
  *   Indicates the common name of the time zone associated with the address
  * @param utcOffset
  *   Indicates the number of hours the time zone is offset from Universal Time Coordinated (UTC). Empty for military or diplomatic
  *   addresses
  * @param dst
  *   Indicates whether the timezone uses daylight savings time
  *   - '''true''' — Timezone uses DST
  *   - '''[empty]'''- Timezone doesn't use DST
  *
  * @author
  *   Yan Doroshenko
  */
case class Metadata(
    recordType: Option[RecordType],
    zipType: Option[ZipType],
    countyFips: Option[String],
    countyName: Option[String],
    ewsMatch: Option[Boolean],
    carrierRoute: Option[String],
    congressionalDistrict: Option[String],
    buildingDefaultIndicator: Option[CharBoolean],
    rdi: Option[ResidentialDeliveryIndicator],
    elotSequence: Option[String],
    elotSort: Option[ELOTSort],
    latitude: Option[Double],
    longitude: Option[Double],
    precision: Precision,
    timeZone: Option[String],
    utcOffset: Option[Double],
    dst: Option[Boolean]
)

object Metadata extends JsonSnakeCase {
  implicit val f: Format[Metadata] = Json.format
}

/** Describes the type of an address record that was matched
  *   - '''Firm''' — Firm; the finest level of match available for an address
  *   - '''GeneralDelivery''' — General Delivery; for mail to be held at local post offices
  *   - '''HighRise''' — High-rise; address contains apartment or building sub-units
  *   - '''POBox''' — Post Office box; address is a PO Box record type
  *   - '''RuralRoute''' — Rural Route or Highway Contract; may have box number ranges
  *   - '''Street''' — Street; address contains a valid primary number range
  *
  * @author
  *   Yan Doroshenko
  */
object RecordType extends Enumeration {
  type RecordType = Value

  protected case class Member(code: Char) extends super.Val

  val Firm = Member('F')
  val GeneralDelivery = Member('G')
  val HighRise = Member('H')
  val POBox = Member('P')
  val RuralRoute = Member('R')
  val Street = Member('S')

  implicit def valueToVal(v: Value): Member = v.asInstanceOf[Member]

  implicit val f: Format[Value] =
    new Format[Value] {
      override def writes(o: Value): JsValue = JsString(o.code.toString)

      override def reads(json: JsValue): JsResult[Value] = JsSuccess(values.find(_.code == json.as[String].head).get)
    }

}

/** Indicates the type of the ZIP Code for the address that was matched
  *
  *   - '''Unique''' — The ZIP Code consists of a single delivery point, pertaining to a United States Postal Service customer (like a large
  *     business or government agency) that routes all of its own mail internally
  *   - '''Military''' — The ZIP Code pertains to military units and diplomatic organizations, often in foreign locations
  *   - '''POBox''' — The ZIP Code is assigned to a collection of Post Office Boxes
  *   - '''Standard''' — The ZIP Code does not pertain to any of the above categories
  *
  * @author
  *   Yan Doroshenko
  */
object ZipType extends Enumeration {
  type ZipType = Value

  protected case class Member(value: String) extends super.Val

  val Unique: Member = Member("Unique")
  val Military: Member = Member("Military")
  val POBox: Member = Member("POBox")
  val Standard: Member = Member("Standard")

  implicit def valueToVal(v: Value): Member = v.asInstanceOf[Member]

  implicit val f: Format[Value] =
    new Format[Value] {
      override def writes(o: Value): JsValue = JsString(o.value)

      override def reads(json: JsValue): JsResult[Value] = JsSuccess(values.find(_.value == json.as[String]).get)
    }

}

/** Enumerates possible values for Residential Delivery Indicator (residential or commercial)
  *   - '''Residential''' — The address is a residential address
  *   - '''Commercial''' — The address is a commercial address
  *
  * @author
  *   Yan Doroshenko
  */
object ResidentialDeliveryIndicator extends Enumeration {
  type ResidentialDeliveryIndicator = Value

  protected case class Member(value: String) extends super.Val

  val Residential: Member = Member("Residential")
  val Commercial: Member = Member("Commercial")

  implicit def valueToVal(v: Value): Member = v.asInstanceOf[Member]

  implicit val f: Format[Value] =
    new Format[Value] {
      override def writes(o: Value): JsValue = JsString(o.value)

      override def reads(json: JsValue): JsResult[Value] = JsSuccess(values.find(_.value == json.as[String]).get)
    }

}

/** Describes the possible [[https://ribbs.usps.gov/index.cfm?page=elot eLOT]] sort values
  *   - '''Ascending''' — Ascending
  *   - '''Descending''' — Descending
  *
  * @author
  *   Yan Doroshenko
  */
object ELOTSort extends Enumeration {
  type ELOTSort = Value

  protected case class Member(code: Char) extends super.Val

  val Ascending: Member = Member('A')
  val Descending: Member = Member('D')

  implicit def valueToVal(v: Value): Member = v.asInstanceOf[Member]

  implicit val f: Format[Value] =
    new Format[Value] {
      override def writes(o: Value): JsValue = JsString(o.code.toString)

      override def reads(json: JsValue): JsResult[Value] = JsSuccess(values.find(_.code == json.as[String].head).get)
    }

}

/** Describes the precision of the latitude and longitude values
  *
  *   - '''Unknown''' — Coordinates not known. Reasons could include: address is invalid, military address (APO or FPO), lat/lon coordinates
  *     not available.
  *   - '''Zip5''' — Accurate to a 5-digit ZIP Code level (least precise)
  *   - '''Zip6''' — Accurate to a 6-digit ZIP Code level
  *   - '''Zip7''' — Accurate to a 7-digit ZIP Code level
  *   - '''Zip8''' — Accurate to an 8-digit ZIP Code level
  *   - '''Zip9''' — Accurate to a 9-digit ZIP Code level (most precise but NOT rooftop level)
  *
  * @author
  *   Yan Doroshenko
  */
object Precision extends Enumeration {
  type Precision = Value

  protected case class Member(code: String) extends super.Val

  val Unknown: Member = Member("Unknown")
  val Zip5: Member = Member("Zip5")
  val Zip6: Member = Member("Zip6")
  val Zip7: Member = Member("Zip7")
  val Zip8: Member = Member("Zip8")
  val Zip9: Member = Member("Zip9")

  implicit def valueToVal(v: Value): Member = v.asInstanceOf[Member]

  implicit val f: Format[Value] =
    new Format[Value] {
      override def writes(o: Value): JsValue = JsString(o.code)

      override def reads(json: JsValue): JsResult[Value] = JsSuccess(values.find(_.code == json.as[String]).get)
    }

}

/** Describes analytics data, contained in the SmartyStreets response
  *
  * @param dpvMatchCode
  *   Status of the Delivery Point Validation (DPV). This indicates whether the USPS delivers mail to the address. Empty means the address
  *   was not submitted for DPV. See [[DPVMatchCode]]
  * @param dpvFootnotes
  *   Indicates why the address was given its DPV value and potentially the type of ZIP Code that was matched. See [[DPVFootnotes]]
  * @param dpvCmra
  *   Indicates whether the address is associated with a Commercial Mail Receiving Agency (CMRA), also known as a private mailbox (PMB)
  *   operator. A CMRA is a business through which USPS mail may be sent or received, for example the UPS Store and Mailboxes Etc.
  *
  *   - '''true''' — Address is associated with a valid CMRA.
  *   - '''false''' — Address is not associated with a valid CMRA.
  *   - '''[empty]''' — Address was not submitted for CMRA verification.
  * @param dpvVacant
  *   Indicates that a delivery point was active in the past but is currently vacant (in most cases, unoccupied over 90 days) and is not
  *   receiving deliveries. This status is often obtained when mail receptacles aren't being emptied and are filling up, so mail is held at
  *   the post office for a certain number of days before the delivery point is marked vacant
  *
  *   - '''Y''' — Address is vacant
  *   - '''N''' — Address is not vacant
  *   - '''[empty]''' — Address was not submitted for vacancy verification
  * @param dpvActive
  *   Indicates whether the address is active, or "in-service" according to the USPS
  *
  *   - '''true''' — Address is active
  *   - '''false''' — Address is inactive
  *   - '''[empty]''' — Activity status is not known by the USPS
  * @param footnotes
  *   Footnotes of the changes made to the input address. See [[Footnotes]]
  * @param lacslinkCode
  *   The reason for the LACSLink indication that was given. See [[LACSLinkCode]]
  * @param lacslinkIndicator
  *   Indicates whether there is an address match in the LACSLink database. See [[LACSLinkIndicator]]
  * @param suitelinkMatch
  *   Indicates a match (or not) to the USPS SuiteLink data. SuiteLink attempts to provide secondary information such as "suite" or
  *   "apartment" whenever there is a match based on address and Firm Name (Company) inpu
  *
  * @author
  *   Yan Doroshenko
  */
case class Analysis(
    dpvMatchCode: Option[DPVMatchCode],
    dpvFootnotes: DPVFootnotes,
    dpvCmra: Option[CharBoolean],
    dpvVacant: Option[CharBoolean],
    dpvActive: Option[CharBoolean],
    footnotes: Option[Footnotes],
    lacslinkCode: Option[LACSLinkCode],
    lacslinkIndicator: Option[LACSLinkIndicator],
    suitelinkMatch: Option[Boolean]
)

object Analysis extends JsonSnakeCase {
  implicit val f: Format[Analysis] = Json.format
}

/** Enumerates possible DPV match code values
  *
  *   - '''Confirmed''' — Confirmed; entire address was DPV confirmed deliverable.
  *   - '''NotConfirmed''' — Not Confirmed; address could not be DPV confirmed as deliverable.
  *   - '''SecondaryDropped''' — Confirmed By Dropping Secondary; address was DPV confirmed by dropping all or part of the secondary info
  *     (apartment, suite, etc.).
  *   - '''SecondaryMissing''' — Confirmed - Missing Secondary Info; the address was DPV confirmed, but it is missing secondary information
  *     (apartment, suite, etc.).
  *
  * @author
  *   Yan Doroshenko
  */
object DPVMatchCode extends Enumeration {
  type DPVMatchCode = Value

  protected case class Member(code: Char) extends super.Val

  val Confirmed: Member = Member('Y')
  val NotConfirmed: Member = Member('N')
  val SecondaryDropped: Member = Member('S')
  val SecondaryMissing: Member = Member('D')

  implicit def valueToVal(v: Value): Member = v.asInstanceOf[Member]

  implicit val f: Format[Value] =
    new Format[Value] {
      override def writes(o: Value): JsValue = JsString(o.code.toString)

      override def reads(json: JsValue): JsResult[Value] = JsSuccess(values.find(_.code == json.as[String].head).get)
    }

}

/** Provides transformation between a DPV footnotes string provided by the SmartyStreets and the list of [[DPVFootnote]] values
  *
  * @param footnotes
  *   List of DPV footnotes for a given response
  *
  * @author
  *   Yan Doroshenko
  */
case class DPVFootnotes(footnotes: Seq[DPVFootnote])

object DPVFootnotes {

  implicit val f: Format[DPVFootnotes] =
    new Format[DPVFootnotes] {
      override def writes(o: DPVFootnotes): JsValue = JsString(o.footnotes.map(_.code).mkString)

      override def reads(json: JsValue): JsResult[DPVFootnotes] = JsSuccess(
        DPVFootnotes(json.as[String].sliding(2, 2).map { v => DPVFootnote(v) }.toSeq)
      )
    }

}

/** Enumerates possible DPV footnote values
  *   - '''AllValid''' — City/state/ZIP + street are all valid
  *   - '''PlusFourNotMatched''' — ZIP+4 not matched; address is invalid. (City/state/ZIP + street don't match.)
  *   - '''Confirmed''' — ZIP+4 matched; confirmed entire address; address is valid
  *   - '''SecondaryDropped''' — Confirmed address by dropping secondary (apartment, suite, etc.) information
  *   - '''MilitaryDiplomatic''' — Matched to military or diplomatic address
  *   - '''GeneralDelivery''' — Matched to general delivery address
  *   - '''PrimaryNumberMissing''' — Primary number (e.g., house number) is missing
  *   - '''PrimaryNumberInvalid''' — Primary number (e.g., house number) is invalid
  *   - '''SecondaryMissing''' — Confirmed with missing secondary information; address is valid but it also needs a secondary number
  *     (apartment, suite, etc.)
  *   - '''POBox''' — Confirmed as a PO BOX street style address
  *   - '''BoxNumberMissing''' — PO, RR, or HC box number is missing
  *   - '''BoxNumberInvalid''' — PO, RR, or HC box number is invalid
  *   - '''PrivateMailbox''' — Confirmed address with private mailbox (PMB) info
  *   - '''NoPrivateMailbox''' — Confirmed address without private mailbox (PMB) info
  *   - '''NoStreetDelivery''' — Confirmed as a valid address that doesn't currently receive US Postal Service street delivery
  *   - '''UniqueZipCode''' — Matched a unique ZIP Code
  *
  * @author
  *   Yan Doroshenko
  */
object DPVFootnote extends Enumeration {
  type DPVFootnote = Value

  def apply(code: String): Member =
    code match {
      case "AA" => AllValid
      case "A1" => PlusFourNotMatched
      case "BB" => Confirmed
      case "CC" => SecondaryDropped
      case "F1" => MilitaryDiplomatic
      case "G1" => GeneralDelivery
      case "M1" => PrimaryNumberMissing
      case "M3" => PrimaryNumberInvalid
      case "N1" => SecondaryMissing
      case "PB" => POBox
      case "P1" => BoxNumberMissing
      case "P3" => BoxNumberInvalid
      case "RR" => PrivateMailbox
      case "R1" => NoPrivateMailbox
      case "R7" => NoStreetDelivery
      case "U1" => UniqueZipCode
    }

  protected case class Member(code: String, description: String) extends super.Val

  val AllValid: Member = Member("AA", "City/state/ZIP + street are all valid")
  val PlusFourNotMatched: Member = Member("A1", "ZIP+4 not matched; address is invalid. (City/state/ZIP + street don't match.)")
  val Confirmed: Member = Member("BB", "ZIP+4 matched; confirmed entire address; address is valid")
  val SecondaryDropped: Member = Member("CC", "Confirmed address by dropping secondary (apartment, suite, etc.) information")
  val MilitaryDiplomatic: Member = Member("F1", "Matched to military or diplomatic address")
  val GeneralDelivery: Member = Member("G1", "Matched to general delivery address")
  val PrimaryNumberMissing: Member = Member("M1", "Primary number (e.g., house number) is missing")
  val PrimaryNumberInvalid: Member = Member("M3", "Primary number (e.g., house number) is invalid")

  val SecondaryMissing: Member = Member(
    "N1",
    "Confirmed with missing secondary information; address is valid but it also needs a secondary number (apartment, suite, etc.)"
  )

  val POBox: Member = Member("PB", "Confirmed as a PO BOX street style address")
  val BoxNumberMissing: Member = Member("P1", "PO, RR, or HC box number is missing")
  val BoxNumberInvalid: Member = Member("P3", "PO, RR, or HC box number is invalid")
  val PrivateMailbox: Member = Member("RR", "Confirmed address with private mailbox (PMB) info")
  val NoPrivateMailbox: Member = Member("R1", "Confirmed address without private mailbox (PMB) info")

  val NoStreetDelivery: Member = Member("R7",
                                        "Confirmed as a valid address that doesn't currently receive US Postal Service street delivery"
                                       )

  val UniqueZipCode: Member = Member("U1", "Matched a unique ZIP Code")

  implicit def valueToVal(v: Value): Member = v.asInstanceOf[Member]
}

/** Enumerates possible LACSLink codes
  *   - '''Match''' — Match: Address provided. LACSLink record match was found, and a converted address was provided
  *   - '''NoMatch''' — No Match. No converted address
  *   - '''NoNewAddress''' — Match: No new address. LACSLink matched an input address to an old address which is a "high-rise default"
  *     address; no new address was provided
  *   - '''NoConversion''' — Match: No conversion. Found a LACSLink record, but couldn't convert the data to a deliverable address
  *   - '''SecondaryDropped''' — Match: Dropped secondary number. LACSLink record was matched after dropping the secondary number from input
  *
  * @author
  *   Yan Doroshenko
  */
object LACSLinkCode extends Enumeration {
  type LACSLinkCode = Value

  protected case class Member(code: String) extends super.Val

  val Match: Member = Member("A")
  val NoMatch: Member = Member("00")
  val NoNewAddress: Member = Member("09")
  val NoConversion: Member = Member("14")
  val SecondaryDropped: Member = Member("92")

  implicit def valueToVal(v: Value): Member = v.asInstanceOf[Member]

  implicit val f: Format[Value] =
    new Format[Value] {
      override def writes(o: Value): JsValue = JsString(o.code)

      override def reads(json: JsValue): JsResult[Value] = JsSuccess(values.find(_.code == json.as[String]).get)
    }

}

/** Indicates whether there is an address match in the LACSLink database
  *   - '''Match''' — LACS record match; a new address could be furnished because the input record matched a record in the master file
  *   - '''SecondaryNumberDropped''' — LACS record - secondary number dropped; the record is a ZIP+4 street level or high-rise match. The
  *     input record matched a master file record, but the input address had a secondary number and the master file record did not
  *   - '''NoMatch''' — No match; a new address could not be furnished; the input record could not be matched to a record in the master file
  *   - '''FalsePositive''' — False positive; a false positive record was detected
  *
  * @author
  *   Yan Doroshenko
  */
object LACSLinkIndicator extends Enumeration {
  type LACSLinkIndicator = Value

  protected case class Member(code: Char) extends super.Val

  val Match: Member = Member('Y')
  val SecondaryNumberDropped: Member = Member('S')
  val NoMatch: Member = Member('N')
  val FalsePositive: Member = Member('F')

  implicit def valueToVal(v: Value): Member = v.asInstanceOf[Member]

  implicit val f: Format[Value] =
    new Format[Value] {
      override def writes(o: Value): JsValue = JsString(o.code.toString)

      override def reads(json: JsValue): JsResult[Value] = JsSuccess(values.find(_.code == json.as[String].head).get)
    }

}

/** Provides transformation between a footnotes string provided by the SmartyStreets and the list of [[Footnote]] values
  *
  * @param footnotes
  *   List of footnotes for a given response
  *
  * @author
  *   Yan Doroshenko
  */
case class Footnotes(footnotes: Seq[Footnote])

object Footnotes {

  implicit val f: Format[Footnotes] =
    new Format[Footnotes] {
      override def writes(o: Footnotes): JsValue = JsString(o.footnotes.map(_.code).mkString)

      override def reads(json: JsValue): JsResult[Footnotes] = {
        val s = json.as[String]
        val lacsPresent = s.indexOf("LI#") != -1 || s.indexOf("LL#") != -1
        val ss = s.replaceAll("LI#", "").replaceAll("LL#", "").sliding(2, 2).map { v => Footnote(v) }.toSeq
        JsSuccess(Footnotes(ss ++ (if (lacsPresent) {
                                     Seq(Footnote.FlaggedForLACSLink)
                                   } else {
                                     Seq.empty[Footnote]
                                   })))
      }
    }

}

/** Enumerates possible footnote values
  *   - '''CorrectedZipCode''' — The address was found to have a different 5-digit ZIP Code than given in the submitted list. The correct
  *     ZIP Code is shown in the ZIP Code field
  *   - '''FixedCityStateSpelling''' — The spelling of the city name and/or state abbreviation in the submitted address was found to be
  *     different than the standard spelling. The standard spelling of the city name and state abbreviation is shown in the City and State
  *     fields
  *   - '''InvalidCityStateZip''' — The ZIP Code in the submitted address could not be found because neither a valid city and state, nor
  *     valid 5-digit ZIP Code was present. SmartyStreets recommends that the customer check the accuracy of the submitted address
  *   - '''NoPlus4Assigned''' — This is a record listed by the United States Postal Service as a non-deliverable location. SmartyStreets
  *     recommends that the customer check the accuracy of the submitted address
  *   - '''SameZipForMultiple''' — Multiple records were returned, but each shares the same 5-digit ZIP Code
  *   - '''AddressNotFound''' — The address, exactly as submitted, could not be found in the city, state, or ZIP Code provided. Many factors
  *     contribute to this; either the primary number is missing, the street is missing, or the street is too horribly misspelled to
  *     understand
  *   - '''UsedFirmData''' — Information in the firm line was determined to be a part of the address. It was moved out of the firm line and
  *     incorporated into the address line
  *   - '''MissingSecondaryNumber''' — ZIP+4 information indicates that this address is a building. The address as submitted does not
  *     contain a secondary (apartment, suite, etc.) number. SmartyStreets recommends that the customer check the accuracy of the submitted
  *     address and add the missing secondary number to ensure the correct Delivery Point Barcode (DPBC)
  *   - '''IncorrectAddressData''' — More than one ZIP+4 Code was found to satisfy the address as submitted. The submitted address did not
  *     contain sufficiently complete or correct data to determine a single ZIP+4 Code. SmartyStreets recommends that the customer check the
  *     accuracy and completeness of the submitted address. For example, a street may have a similar address at both the north and south
  *     ends of the street
  *   - '''DualAddress''' — The input contained two addresses. For example: 123 MAIN ST PO BOX 99
  *   - '''CardinalRuleMatch''' — Although the address as submitted is not valid, we were able to find a match by changing the cardinal
  *     direction (North, South, East, West). The cardinal direction we used to find a match is found in the components
  *   - '''ChangedAddressComponent''' — An address component (i.e., directional or suffix only) was added, changed, or deleted in order to
  *     achieve a match
  *   - '''FlaggedForLACSLink''' — The input address matched a record that was LACS-indicated, that was submitted to LACSLink for
  *     processing. This does not mean that the address was converted; it only means that the address was submitted to LACSLink because the
  *     input address had the LACS indicator set
  *   - '''FixedStreetSpelling''' — The spelling of the street name was changed in order to achieve a match
  *   - '''FixedAbbreviations''' — The delivery address was standardized. For example, if STREET was in the delivery address, SmartyStreets
  *     will return ST as its standard spelling
  *   - '''MultiplePlus4''' — More than one ZIP+4 Code was found to satisfy the address as submitted. The lowest ZIP+4 add-on may be used to
  *     break the tie between the records
  *   - '''BetterAddressExists''' — The delivery address is matchable, but it is known by another (preferred) name. For example, in New
  *     York, NY, AVENUE OF THE AMERICAS is also known as 6TH AVE. An inquiry using a delivery address of 39 6th Avenue would be flagged
  *     with Footnote P
  *   - '''UniqueZipMatch''' — Match to an address with a unique ZIP Code
  *   - '''EWSMatchSoon''' — The delivery address is not yet matchable, but the Early Warning System file indicates that an exact match will
  *     be available soon
  *   - '''BadSecondaryAddress''' — The secondary information (apartment, suite, etc.) does not match that on the national ZIP+4 file. The
  *     secondary information, although present on the input address, was not valid in the range found on the national ZIP+4 file
  *   - '''MultipleResponse''' — The search resulted in a single response; however, the record matched was flagged as having magnet street
  *     syndrome, and the input street name components (pre-directional, primary street name, post-directional, and suffix) did not exactly
  *     match those of the record. A "magnet street" is one having a primary street name that is also a suffix or directional word, having
  *     either a post-directional or a suffix (i.e., 2220 PARK MEMPHIS TN logically matches to a ZIP+4 record 2200-2258 PARK AVE MEMPHIS TN
  *     38114-6610), but the input address lacks the suffix "AVE" which is present on the ZIP+4 record. The primary street name "PARK" is a
  *     suffix word. The record has either a suffix or a post-directional present. Therefore, in accordance with CASS requirements, a ZIP+4
  *     Code must not be returned. The multiple response return code is given since a "no match" would prevent the best candidate
  *   - '''UnofficialPostOfficeName''' — The city or post office name in the submitted address is not recognized by the United States Postal
  *     Service as an official last line name (preferred city name), and is not acceptable as an alternate name. The preferred city name is
  *     included in the City field
  *   - '''UnverifiableCityState''' — The city and state in the submitted address could not be verified as corresponding to the given
  *     5-digit ZIP Code. This comment does not necessarily denote an error; however, SmartyStreets recommends that the customer check the
  *     accuracy of the city and state in the submitted address
  *   - '''InvalidDeliveryAddress''' — The input address record contains a delivery address other than a PO Box, General Delivery, or
  *     Postmaster 5-digit ZIP Code that is identified as a "small town default." The USPS does not provide street delivery service for this
  *     ZIP Code. The USPS requires the use of a PO Box, General Delivery, or Postmaster for delivery within this ZIP Code
  *   - '''UniqueZipCode''' — Default match inside a unique ZIP Code
  *   - '''MilitaryMatch''' — Match made to a record with a military or diplomatic ZIP Code
  *   - '''MatchedWithZipMove''' — The ZIPMOVE product shows which ZIP+4 records have moved from one ZIP Code to another. If an input
  *     address matches a ZIP+4 record which the ZIPMOVE product indicates has moved, the search is performed again in the new ZIP Code
  *
  * @author
  *   Yan Doroshenko
  */
object Footnote extends Enumeration {
  type Footnote = Value

  protected case class Member(code: String, description: String) extends super.Val

  def apply(code: String): Member =
    code match {
      case "A#" => CorrectedZipCode
      case "B#" => FixedCityStateSpelling
      case "C#" => InvalidCityStateZip
      case "D#" => NoPlus4Assigned
      case "E#" => SameZipForMultiple
      case "F#" => AddressNotFound
      case "G#" => UsedFirmData
      case "H#" => MissingSecondaryNumber
      case "I#" => IncorrectAddressData
      case "J#" => DualAddress
      case "K#" => CardinalRuleMatch
      case "L#" => ChangedAddressComponent
      case "LI#" => FlaggedForLACSLink
      case "LL#" => FlaggedForLACSLink
      case "M#" => FixedStreetSpelling
      case "N#" => FixedAbbreviations
      case "O#" => MultiplePlus4
      case "P#" => BetterAddressExists
      case "Q#" => UniqueZipMatch
      case "R#" => EWSMatchSoon
      case "S#" => BadSecondaryAddress
      case "T#" => MultipleResponse
      case "U#" => UnofficialPostOfficeName
      case "V#" => UnverifiableCityState
      case "W#" => InvalidDeliveryAddress
      case "X#" => UniqueZipCode
      case "Y#" => MilitaryMatch
      case "Z#" => MatchedWithZipMove
    }

  val CorrectedZipCode: Member = Member(
    "A#",
    "The address was found to have a different 5-digit ZIP Code than given in the submitted list. The correct ZIP Code is shown in the ZIP Code field"
  )

  val FixedCityStateSpelling: Member = Member(
    "B#",
    "The spelling of the city name and/or state abbreviation in the submitted address was found to be different than the standard spelling. The standard spelling of the city name and state abbreviation is shown in the City and State fields"
  )

  val InvalidCityStateZip: Member = Member(
    "C#",
    "The ZIP Code in the submitted address could not be found because neither a valid city and state, nor valid 5-digit ZIP Code was present. SmartyStreets recommends that the customer check the accuracy of the submitted address."
  )

  val NoPlus4Assigned: Member = Member(
    "D#",
    "This is a record listed by the United States Postal Service as a non-deliverable location. SmartyStreets recommends that the customer check the accuracy of the submitted address."
  )

  val SameZipForMultiple: Member = Member("E#", "Multiple records were returned, but each shares the same 5-digit ZIP Code.")

  val AddressNotFound: Member = Member(
    "F#",
    "The address, exactly as submitted, could not be found in the city, state, or ZIP Code provided. Many factors contribute to this; either the primary number is missing, the street is missing, or the street is too horribly misspelled to understand."
  )

  val UsedFirmData: Member = Member(
    "G#",
    "Information in the firm line was determined to be a part of the address. It was moved out of the firm line and incorporated into the address line."
  )

  val MissingSecondaryNumber: Member = Member(
    "H#",
    "ZIP+4 information indicates that this address is a building. The address as submitted does not contain a secondary (apartment, suite, etc.) number. SmartyStreets recommends that the customer check the accuracy of the submitted address and add the missing secondary number to ensure the correct Delivery Point Barcode (DPBC)."
  )

  val IncorrectAddressData: Member = Member(
    "I#",
    "More than one ZIP+4 Code was found to satisfy the address as submitted. The submitted address did not contain sufficiently complete or correct data to determine a single ZIP+4 Code. SmartyStreets recommends that the customer check the accuracy and completeness of the submitted address. For example, a street may have a similar address at both the north and south ends of the street."
  )

  val DualAddress: Member = Member("J#", "The input contained two addresses. For example: 123 MAIN ST PO BOX 99.")

  val CardinalRuleMatch: Member = Member(
    "K#",
    "Although the address as submitted is not valid, we were able to find a match by changing the cardinal direction (North, South, East, West). The cardinal direction we used to find a match is found in the components."
  )

  val ChangedAddressComponent: Member = Member(
    "L#",
    "An address component (i.e., directional or suffix only) was added, changed, or deleted in order to achieve a match."
  )

  val FlaggedForLACSLink: Member = Member(
    "LI#",
    "The input address matched a record that was LACS-indicated, that was submitted to LACSLink for processing. This does not mean that the address was converted; it only means that the address was submitted to LACSLink because the input address had the LACS indicator set."
  )

  val FixedStreetSpelling: Member = Member("M#", "The spelling of the street name was changed in order to achieve a match.")

  val FixedAbbreviations: Member = Member(
    "N#",
    "The delivery address was standardized. For example, if STREET was in the delivery address, SmartyStreets will return ST as its standard spelling."
  )

  val MultiplePlus4: Member = Member(
    "O#",
    "More than one ZIP+4 Code was found to satisfy the address as submitted. The lowest ZIP+4 add-on may be used to break the tie between the records."
  )

  val BetterAddressExists: Member = Member(
    "P#",
    "The delivery address is matchable, but it is known by another (preferred) name. For example, in New York, NY, AVENUE OF THE AMERICAS is also known as 6TH AVE. An inquiry using a delivery address of 39 6th Avenue would be flagged with Footnote P."
  )

  val UniqueZipMatch: Member = Member("Q#", "Match to an address with a unique ZIP Code")

  val EWSMatchSoon: Member = Member(
    "R#",
    "The delivery address is not yet matchable, but the Early Warning System file indicates that an exact match will be available soon."
  )

  val BadSecondaryAddress: Member = Member(
    "S#",
    "The secondary information (apartment, suite, etc.) does not match that on the national ZIP+4 file. The secondary information, although present on the input address, was not valid in the range found on the national ZIP+4 file."
  )

  val MultipleResponse: Member = Member(
    "T#",
    "The search resulted in a single response; however, the record matched was flagged as having magnet street syndrome, and the input street name components (pre-directional, primary street name, post-directional, and suffix) did not exactly match those of the record. A \"magnet street\" is one having a primary street name that is also a suffix or directional word, having either a post-directional or a suffix (i.e., 2220 PARK MEMPHIS TN logically matches to a ZIP+4 record 2200-2258 PARK AVE MEMPHIS TN 38114-6610), but the input address lacks the suffix \"AVE\" which is present on the ZIP+4 record. The primary street name \"PARK\" is a suffix word. The record has either a suffix or a post-directional present. Therefore, in accordance with CASS requirements, a ZIP+4 Code must not be returned. The multiple response return code is given since a \"no match\" would prevent the best candidate."
  )

  val UnofficialPostOfficeName: Member = Member(
    "U#",
    "The city or post office name in the submitted address is not recognized by the United States Postal Service as an official last line name (preferred city name), and is not acceptable as an alternate name. The preferred city name is included in the City field."
  )

  val UnverifiableCityState: Member = Member(
    "V#",
    "The city and state in the submitted address could not be verified as corresponding to the given 5-digit ZIP Code. This comment does not necessarily denote an error; however, SmartyStreets recommends that the customer check the accuracy of the city and state in the submitted address."
  )

  val InvalidDeliveryAddress: Member = Member(
    "W#",
    "The input address record contains a delivery address other than a PO Box, General Delivery, or Postmaster 5-digit ZIP Code that is identified as a \"small town default.\" The USPS does not provide street delivery service for this ZIP Code. The USPS requires the use of a PO Box, General Delivery, or Postmaster for delivery within this ZIP Code."
  )

  val UniqueZipCode: Member = Member("X#", "Default match inside a unique ZIP Code")
  val MilitaryMatch: Member = Member("Y#", "Match made to a record with a military or diplomatic ZIP Code.")

  val MatchedWithZipMove: Member = Member(
    "Z#",
    "The ZIPMOVE product shows which ZIP+4 records have moved from one ZIP Code to another. If an input address matches a ZIP+4 record which the ZIPMOVE product indicates has moved, the search is performed again in the new ZIP Code."
  )

  implicit def valueToVal(v: Value): Member = v.asInstanceOf[Member]

  implicit val f: Format[Value] =
    new Format[Value] {
      override def writes(o: Value): JsValue = JsString(o.code.toString)

      override def reads(json: JsValue): JsResult[Value] = JsSuccess(values.find(_.code == json.as[String]).get)
    }

}
