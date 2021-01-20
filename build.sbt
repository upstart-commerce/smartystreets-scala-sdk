organization := "org.upstartcommerce"
name := "smartystreets-scala-sdk"
version := "0.0.2"

licenses += "Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")

// the Scala version that will be used for cross-compiled libraries
val scala_2_12 = "2.12.12"
val scala_2_13 = "2.13.4"

crossScalaVersions in ThisBuild := Seq(scala_2_12, scala_2_13)
// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := scala_2_13

val akkaHttpPlayJson = "de.heikoseeberger" %% "akka-http-play-json" % "1.35.3"
val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.2.3"
val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.6.8"
val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.6.8"
val scalatest = "org.scalatest" %% "scalatest" % "3.0.9" % Test
libraryDependencies ++= Seq(
  akkaActor,
  akkaHttpPlayJson,
  akkaHttp,
  akkaStream,
  scalatest
)


resolvers in Global += Resolver.url("upstartcommerce", url("https://upstartcommerce.bintray.com/nochannel"))(Resolver.ivyStylePatterns)
bintrayOmitLicense := true
bintrayOrganization := Some("upstartcommerce")
bintrayRepository := "generic"
bintrayReleaseOnPublish in ThisBuild := false
publishMavenStyle := false

scalacOptions := scalacOptionsVersion(scalaVersion.value)

def scalacOptionsVersion(scalaVersion: String) = {

  Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "utf-8", // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
    "-language:experimental.macros", // Allow macro definition (besides implementation and application)
    "-language:higherKinds", // Allow higher-kinded types
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
    "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
    "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Xlint:option-implicit", // Option.apply used implicit view.
    "-Xlint:package-object-classes", // Class or object defined in package object.
    "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
    "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
    "-Ywarn-unused:locals", // Warn if a local definition is unused.
    "-Ywarn-unused:params", // Warn if a value parameter is unused.
    "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
    "-Ywarn-unused:privates", // Warn if a private member is unused.
    "-Ywarn-value-discard" // Warn when non-Unit expression results are unused.
  ) ++ {
    if (scalaVersion == scala_2_12)
      Seq(
        "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
        "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
        "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
        //        "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
        "-Ypartial-unification", // Enable partial unification in type constructor inference
        "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
        "-Xlint:unsound-match", // Pattern match may not be typesafe.
        "-Yno-adapted-args" // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
      )
    else if (scalaVersion == scala_2_13)
      Seq(
        //        "-Xfatal-warnings", // Fail the compilation if there are any warnings.
        "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
        "-Xlint:infer-any" // Warn when a type argument is inferred to be `Any`.
        //        "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
        //        "-target:jvm-1.11"
      )
    else Seq.empty
  }
}
