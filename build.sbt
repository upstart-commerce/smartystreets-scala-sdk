ThisBuild / scalaVersion := "2.13.15"
ThisBuild / organization := "com.upstartcommerce"
name := "smartystreets-scala-sdk"
val pekkoHttpPlayJson = "com.github.pjfanning" %% "pekko-http-play-json" % "3.0.0"
val pekkoHttp = "org.apache.pekko"             %% "pekko-http"           % "1.1.+"
val pekkoActor = "org.apache.pekko"            %% "pekko-actor"          % "1.1.1"
val pekkoStream = "org.apache.pekko"           %% "pekko-stream"         % "1.1.1"
val scalatest = "org.scalatest"                %% "scalatest"            % "3.2.+" % Test

libraryDependencies ++= Seq(
  pekkoActor,
  pekkoHttpPlayJson,
  pekkoHttp,
  pekkoStream,
  scalatest
)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

val artifactoryResolver =
  Resolver.url("upstartcommerce", url("https://upstartcommerce.jfrog.io/artifactory/nochannel"))(Resolver.ivyStylePatterns)

Global / resolvers += artifactoryResolver
publishTo := Some(artifactoryResolver)
credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
publishMavenStyle := false

scalacOptions := scalacOptionsVersion(scalaVersion.value)

def scalacOptionsVersion(scalaVersion: String) = Seq(
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
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
  "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
  "-Xlint:infer-any" // Warn when a type argument is inferred to be `Any`.
)
