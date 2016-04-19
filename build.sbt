enablePlugins(JavaAppPackaging)

organization := "com.singhaiuklimited"

name := "akka-http-circe-geoip-heroku"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV       = "2.4.4"
  val circeV      = "0.4.0"
  val scalaTestV  = "3.0.0-M16-SNAP3"
  val Test = "test"
  Seq(
    "ch.qos.logback"     % "logback-classic"        %  "1.1.3",
    "com.typesafe.akka" %% "akka-slf4j"             %  akkaV,

    "com.typesafe.akka" %% "akka-actor"             % akkaV,
    "com.typesafe.akka" %% "akka-http-core"         % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-stream"            % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,

    "io.circe"          %% "circe-core"             % circeV,
    "io.circe"          %% "circe-generic"          % circeV,
    "io.circe"          %% "circe-parser"           % circeV,


    "com.typesafe.akka" %% "akka-testkit"           % akkaV      % Test,
    "com.typesafe.akka" %% "akka-http-testkit"      % akkaV      % Test,

    "org.scalatest"     %% "scalatest"              % scalaTestV % Test
  )
}

fork in run := true

assemblyJarName in assembly := "akka-http-circe-geoip-heroku.jar"

test in assembly := {}

mainClass in assembly := Some("com.singhaiuklimited.Boot")

resolvers ++= Seq(
  "Cloudera repos"                   at "https://repository.cloudera.com/artifactory/cloudera-repos/",
  "Maven ES"                         at "https://maven.elasticsearch.org/artifactory/repo/",
  "Maven ES releases"                at "https://maven.elasticsearch.org/releases",
  "Typesafe repository snapshots"    at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe repository releases"     at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype repo"                    at "https://oss.sonatype.org/content/groups/scala-tools/",
  "Sonatype releases"                at "https://oss.sonatype.org/content/repositories/releases",
  "Sonatype snapshots"               at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype staging"                 at "http://oss.sonatype.org/content/repositories/staging",
  "Java.net Maven2 Repository"       at "http://download.java.net/maven/2/",
  "Twitter Repository"               at "http://maven.twttr.com",
  "spray repo"                       at "http://repo.spray.io",
  "softprops-maven"                  at "http://dl.bintray.com/content/softprops/maven"
)

assemblyMergeStrategy in assembly := {
  case PathList(ps @ _*) if ps.last endsWith "BaseDateTime.class" => MergeStrategy.first
  case "application.conf"                                         => MergeStrategy.concat
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

Revolver.settings
