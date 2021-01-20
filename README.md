# SmartyStreets Scala SDK
[![Build Status](https://travis-ci.org/upstart-commerce/smartystreets-scala-sdk.svg?branch=master)](https://travis-ci.org/upstart-commerce/smartystreets-scala-sdk)
 [ ![Download](https://api.bintray.com/packages/upstartcommerce/generic/smartystreets-scala-sdk/images/download.svg) ](https://bintray.com/upstartcommerce/generic/smartystreets-scala-sdk/_latestVersion)

Scala library for interaction with SmartyStreets REST API.

### Installation

Add following to your build.sbt
```sbtshell
resolvers in Global += Resolver.url("upstartcommerce", url("https://upstartcommerce.bintray.com/generic"))(Resolver.ivyStylePatterns)

libraryDependencies += "org.upstartcommerce" %% "smartystreets-scala-sdk" % "0.0.2" // or whatever latest version is
```

### Usage

Basic usage is:
```scala
import org.upstartcommerce.smartystreets.SmartyStreetsIntegration
import org.upstartcommerce.smartystreets.common._

class MyAddressVerificationService extends SmartyStreetsIntegration {

  override val smartyStreetsConfig = SmartyStreetsConfig(
  authId = "myAuthId",
  authToken = "myAuthToken"
  )

}

object Main {
  def main(args: Array[String]): Unit = {
    val service = new MyAddressVerificationService
    val address = USAddressRequest(
        street = Some("60 Wall St"),
        city = Some("New York"),
        state = Some("NY"),
        zipcode = Some("10005")
    )
    service.verifyAddress(address).map { resp =>
        println(resp) // Process response
    }
  }
}
```
>**Note**: Implicit `ActorSystem`, `ActorMaterializer` and `ExecutionContext` are required

See tests for more examples.
