package com.upstartcommerce.smartystreets

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.upstartcommerce.smartystreets.common._
import com.upstartcommerce.smartystreets.common.exception.{BadRequestException, RequestBodyTooLargeException, UnauthorizedException}
import org.scalatest.{AsyncWordSpec, Matchers}

import scala.concurrent.ExecutionContext

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
  * Please keep in mind that running these test can very quickly deplete the limited number of lookups your account has
  *
  * @todo Improve tests
  * @author Yan Doroshenko
  */
class SmartyStreetsIntegrationSpec extends AsyncWordSpec with Matchers with SmartyStreetsIntegration {

  implicit val ec: ExecutionContext = ExecutionContext.global
  implicit val as: ActorSystem = ActorSystem()
  implicit val am: ActorMaterializer = ActorMaterializer()

  val authId: String = as.settings.config.getString("auth-id")
  val token: String = as.settings.config.getString("auth-token")
  override val smartyStreetsConfig: SmartyStreetsConfig = SmartyStreetsConfig(
    authId = authId,
    authToken = token
  )

  "SmartyStreets integration" should {
    "succeed on correct address1" in {
      val address = USAddressRequest(
        street = Some("60 Wall St"),
        city = Some("New York"),
        state = Some("NY"),
        zipcode = Some("10005")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.metadata.recordType.contains(RecordType.HighRise) &&
            r.analysis.dpvMatchCode.contains(DPVMatchCode.SecondaryMissing) &&
            r.analysis.footnotes.get.footnotes.contains(Footnote.MissingSecondaryNumber)
        )
      }
    }

    "succeed on correct address2" in {
      val address = USAddressRequest(
        street = Some("1400 Rio Grande St"),
        city = Some("Austin"),
        state = Some("TX"),
        zipcode = Some("78701")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.metadata.recordType.contains(RecordType.HighRise) &&
            r.analysis.dpvMatchCode.contains(DPVMatchCode.SecondaryMissing) &&
            r.analysis.footnotes.get.footnotes.contains(Footnote.MissingSecondaryNumber)
        )
      }
    }

    "succeed on correct address3" in {
      val address = USAddressRequest(
        street = Some("1120 Polk Blvd"),
        city = Some("Des Moines"),
        state = Some("IO"),
        zipcode = Some("50311")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.metadata.recordType.contains(RecordType.HighRise) &&
            r.analysis.dpvMatchCode.contains(DPVMatchCode.SecondaryMissing) &&
            r.analysis.footnotes.get.footnotes.contains(Footnote.MissingSecondaryNumber)
        )
      }
    }

    "succeed on multiple correct addresses" in {
      val address1 = USAddressRequest(
        street = Some("60 Wall St"),
        city = Some("New York"),
        state = Some("NY"),
        zipcode = Some("10005")
      )

      val address2 = USAddressRequest(
        street = Some("1400 Rio Grande St"),
        city = Some("Austin"),
        state = Some("TX"),
        zipcode = Some("78701")
      )

      val address3 = USAddressRequest(
        street = Some("1120 Polk Blvd"),
        city = Some("Des Moines"),
        state = Some("IO"),
        zipcode = Some("50311")
      )

      verifyAddresses(
        Seq(
          address1,
          address2,
          address3
        )
      ).map {
        resp =>
          assert(resp.size == 3)
          assert(resp.forall {
            v =>
              v._2.nonEmpty &&
                v._2.head.metadata.recordType.contains(RecordType.HighRise) &&
                v._2.head.analysis.dpvMatchCode.contains(DPVMatchCode.SecondaryMissing) &&
                v._2.head.analysis.footnotes.get.footnotes.contains(Footnote.MissingSecondaryNumber)
          })
      }
    }

    "respect inputId" in {
      val address = USAddressRequest(
        inputId = Some("inputId"),
        street = Some("60 Wall St"),
        city = Some("New York"),
        state = Some("NY"),
        zipcode = Some("10005")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.inputId == address.inputId
        )
      }
    }

    "respect multiple inputIds" in {
      val address1 = USAddressRequest(
        inputId = Some("inputId1"),
        street = Some("60 Wall St"),
        city = Some("New York"),
        state = Some("NY"),
        zipcode = Some("10005")
      )

      val address2 = USAddressRequest(
        inputId = Some("inputId2"),
        street = Some("1400 Rio Grande St"),
        city = Some("Austin"),
        state = Some("TX"),
        zipcode = Some("78701")
      )

      val address3 = USAddressRequest(
        inputId = Some("inputId3"),
        street = Some("1120 Polk Blvd"),
        city = Some("Des Moines"),
        state = Some("IO"),
        zipcode = Some("50311")
      )

      verifyAddresses(
        Seq(
          address1,
          address2,
          address3
        )
      ).map { resp =>
        assert(resp.nonEmpty)
        assert(
          resp.forall { v =>
            v._2.exists(_.inputId == v._1.inputId)
          }
        )
      }
    }

    "throw UnauthorizedException for incorrect authId" in {
      val address = USAddressRequest(
        inputId = Some("inputId"),
        street = Some("60 Wall St"),
        city = Some("New York"),
        state = Some("NY"),
        zipcode = Some("10005")
      )
      val i = new SmartyStreetsIntegration {
        override val smartyStreetsConfig: SmartyStreetsConfig = SmartyStreetsConfig(authId = "halusky", authToken = token)
      }
      i.verifyAddress(address).recover {
        case e: UnauthorizedException => e
      }.map { v =>
        assert(v.isInstanceOf[UnauthorizedException])
      }
      i.verifyAddresses(Seq(address)).recover {
        case e: UnauthorizedException => e
      }.map { v =>
        assert(v.isInstanceOf[UnauthorizedException])
      }
    }

    "throw UnauthorizedException for incorrect authToken" in {
      val address = USAddressRequest(
        inputId = Some("inputId"),
        street = Some("60 Wall St"),
        city = Some("New York"),
        state = Some("NY"),
        zipcode = Some("10005")
      )
      val i = new SmartyStreetsIntegration {
        override val smartyStreetsConfig: SmartyStreetsConfig = SmartyStreetsConfig(authId = authId, authToken = "halusky")
      }
      i.verifyAddress(address).recover {
        case e: UnauthorizedException => e
      }.map { v =>
        assert(v.isInstanceOf[UnauthorizedException])
      }
      i.verifyAddresses(Seq(address)).recover {
        case e: UnauthorizedException => e
      }.map { v =>
        assert(v.isInstanceOf[UnauthorizedException])
      }
    }

    "throw BadRequestException for more than 100 addresses" in {
      val address = USAddressRequest(
        inputId = Some("inputId"),
        street = Some("60 Wall St"),
        city = Some("New York"),
        state = Some("NY"),
        zipcode = Some("10005")
      )
      verifyAddresses(for (_ <- 1 to 101) yield address).recover {
        case e: BadRequestException => e
      }.map { v =>
        assert(v.isInstanceOf[BadRequestException])
      }
    }

    "throw BodyTooLargeException for body larger than 32K" in {
      val address = USAddressRequest(
        inputId = Some("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
        street = Some("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
        street2 = Some("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
        city = Some("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
        state = Some("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
        zipcode = Some("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
      )
      verifyAddresses(for (_ <- 1 to 100) yield address).recover {
        case e: RequestBodyTooLargeException => e
      }.map { v =>
        assert(v.isInstanceOf[RequestBodyTooLargeException])
      }
    }

    "return firm record type" in {
      val address = USAddressRequest(
        street = Some("11300 Center Ave"),
        city = Some("Gilroy"),
        state = Some("CA"),
        zipcode = Some("95020-9257")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.metadata.recordType.contains(RecordType.Firm)
        )
      }
    }

    "return general delivery record type" in {
      val address = USAddressRequest(
        street = Some("General Delivery"),
        city = Some("Provo"),
        state = Some("UT")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.metadata.recordType.contains(RecordType.GeneralDelivery)
        )
      }
    }

    "return high rise record type" in {
      val address = USAddressRequest(
        street = Some("1600 Pennsylvania ave SE"),
        city = Some("Washington"),
        state = Some("DC"),
        zipcode = Some("20003")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.metadata.recordType.contains(RecordType.HighRise)
        )
      }
    }

    "return PO box record type" in {
      val address = USAddressRequest(
        street = Some("PO box 4735"),
        city = Some("Tulsa"),
        state = Some("OK")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.metadata.recordType.contains(RecordType.POBox)
        )
      }
    }

    "return rural route record type" in {
      val address = USAddressRequest(
        street = Some("RR 2 box 4560"),
        city = Some("Anasco"),
        state = Some("PR")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.metadata.recordType.contains(RecordType.RuralRoute)
        )
      }
    }

    "return street record type" in {
      val address = USAddressRequest(
        street = Some("16990 Monterey Rd"),
        city = Some("Lake Elsinore"),
        state = Some("CA"),
        zipcode = Some("92530")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.metadata.recordType.contains(RecordType.Street)
        )
      }
    }

    "return confirmed DPV match code" in {
      val address = USAddressRequest(
        street = Some("1600 Amphitheatre Pkwy"),
        city = Some("Mountain View"),
        state = Some("CA")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvMatchCode.contains(DPVMatchCode.Confirmed)
        )
      }
    }

    "return dropped secondary DPV match code" in {
      val address = USAddressRequest(
        street = Some("62 Ea Darden Dr"),
        secondary = Some("Apt 298"),
        city = Some("Anniston"),
        state = Some("AL")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvMatchCode.contains(DPVMatchCode.SecondaryDropped)
        )
      }
    }

    "return missing secondary DPV match code" in {
      val address = USAddressRequest(
        street = Some("122 Mast Rd"),
        city = Some("Lee"),
        state = Some("NH")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvMatchCode.contains(DPVMatchCode.SecondaryMissing)
        )
      }
    }

    "return all valid DPV footnote" in {
      val address = USAddressRequest(
        street = Some("2335 S State St"),
        street2 = Some("Ste 300"),
        city = Some("Provo"),
        state = Some("UT")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.AllValid)
        )
      }
    }

    "return plus four not matched DPV footnote" in {
      val address = USAddressRequest(
        street = Some("3214 N University Ave"),
        city = Some("New York"),
        state = Some("NY"),
        `match` = Some(Match.Invalid)
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.PlusFourNotMatched)
        )
      }
    }

    "return confirmed DPV footnote" in {
      val address = USAddressRequest(
        street = Some("2335 S State St"),
        street2 = Some("Ste 300"),
        city = Some("Provo"),
        state = Some("UT")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.Confirmed)
        )
      }
    }

    "return secondary dropped DPV footnote" in {
      val address = USAddressRequest(
        street = Some("2335 S State St"),
        street2 = Some("Ste 500"),
        city = Some("Provo"),
        state = Some("Utah")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.SecondaryDropped)
        )
      }
    }

    "return military or diplomatic DPV footnote" in {
      val address = USAddressRequest(
        street = Some("Unit 2050"),
        street2 = Some("box 4190"),
        city = Some("APO"),
        state = Some("AP")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.AllValid)
        )
      }
    }

    "return general delivery DPV footnote" in {
      val address = USAddressRequest(
        street = Some("General delivery"),
        city = Some("Provo"),
        state = Some("UT"),
        zipcode = Some("84601")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.GeneralDelivery)
        )
      }
    }

    "return primary number missing DPV footnote" in {
      val address = USAddressRequest(
        street = Some("N University Ave"),
        city = Some("Provo"),
        state = Some("UT"),
        `match` = Some(Match.Invalid)
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.PrimaryNumberMissing)
        )
      }
    }

    "return primary number invalid DPV footnote" in {
      val address = USAddressRequest(
        street = Some("16 N University Ave"),
        city = Some("Provo"),
        state = Some("UT"),
        `match` = Some(Match.Invalid)
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.PrimaryNumberInvalid)
        )
      }
    }

    "return secondary number missing DPV footnote" in {
      val address = USAddressRequest(
        street = Some("2335 S State St"),
        city = Some("Provo"),
        state = Some("UT")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.SecondaryMissing)
        )
      }
    }

    "return PO box DPV footnote" in {
      val address = USAddressRequest(
        street = Some("555 B B King Blvd"),
        street2 = Some("unit 1"),
        city = Some("Memphis"),
        state = Some("TN")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.POBox)
        )
      }
    }

    "return box number missing DPV footnote" in {
      val address = USAddressRequest(
        street = Some("Dept 126"),
        city = Some("Denver"),
        state = Some("CO"),
        zipcode = Some("802910126"),
        `match` = Some(Match.Invalid)
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.BoxNumberMissing)
        )
      }
    }

    "return box number invalid DPV footnote" in {
      val address = USAddressRequest(
        street = Some("PO Box 60780"),
        city = Some("Fairbanks"),
        state = Some("AK"),
        zipcode = Some("99706"),
        `match` = Some(Match.Invalid)
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.BoxNumberInvalid)
        )
      }
    }

    "return private mailbox DPV footnote" in {
      val address = USAddressRequest(
        street = Some("3214 N University Ave"),
        secondary = Some("#409"),
        city = Some("Provo"),
        state = Some("UT")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.PrivateMailbox)
        )
      }
    }

    "return no private mailbox DPV footnote" in {
      val address = USAddressRequest(
        street = Some("3214 N University Ave"),
        city = Some("Provo"),
        state = Some("UT")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.NoPrivateMailbox)
        )
      }
    }

    "return no street delivery DPV footnote" in {
      val address = USAddressRequest(
        street = Some("6D Cruz Bay"),
        city = Some("St John"),
        state = Some("VI"),
        zipcode = Some("00830")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.NoStreetDelivery)
        )
      }
    }

    "return unique zip code DPV footnote" in {
      val address = USAddressRequest(
        street = Some("100 north happy street"),
        zipcode = Some("12345")
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.analysis.dpvFootnotes.footnotes.contains(DPVFootnote.UniqueZipCode)
        )
      }
    }

    "return multiple candidated" in {
      val address = USAddressRequest(
        street = Some("1 Rosedale Str"),
        city = Some("Baltimore"),
        state = Some("Maryland"),
        candidates = 10
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp
        assert(
          r.size > 1
        )
      }
    }

    "support freeform address" in {
      val address = USAddressRequest(
        street = Some("60 Wall St New York NY 10005"),
      )
      verifyAddress(address).map { resp =>
        assert(resp.nonEmpty)
        val r = resp.head
        assert(
          r.metadata.recordType.contains(RecordType.HighRise) &&
            r.analysis.dpvMatchCode.contains(DPVMatchCode.SecondaryMissing) &&
            r.analysis.footnotes.get.footnotes.contains(Footnote.MissingSecondaryNumber)
        )
      }
    }
  }
}