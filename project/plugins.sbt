// to build zip
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.10.+")

// git versioning
// yields customized sbt-git-versioning
Global / resolvers += Resolver.url("upstartcommerce-public", url("https://upstartcommerce.jfrog.io/artifactory/generic"))(
  Resolver.ivyStylePatterns
)
addSbtPlugin("com.upstartcommerce.sbt" % "sbt-git-versioning" % "1.3.0")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.+")