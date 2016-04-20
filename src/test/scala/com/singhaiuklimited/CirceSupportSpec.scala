package com.singhaiuklimited

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.RequestEntity
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.singhaiuklimited.models._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, DurationInt}

object CirceSupportSpec {

  case class Foo(bar: String)

}

class CirceSupportSpec extends WordSpec with Matchers with BeforeAndAfterAll {

  import CirceSupportSpec._
  import com.singhaiuklimited.utils.CirceSupport._

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  val foo = Foo("bar")

  val ip1Info = IpInfo(Some("AS15169 Google Inc."),
    Some("Mountain View"),
    Some("United States"),
    Some("US"),
    Some("Level 3 Communications"),
    Some(37.386),
    Some(-122.0838),
    None,
    Some("Level 3 Communications"),
    None,
    "8.8.8.8",
    Some("CA"),
    Some("California"),
    None,
    "Success",
    Some("America/Los_Angeles"),
    Some("94040"))

  val ip2Info = IpInfo(Some("AS15169 Google Inc."),
    Some("Mountain View"),
    Some("United States"),
    Some("US"),
    None,
    Some(38.0),
    Some(-97.0),
    None,
    None,
    None,
    "8.8.4.4",
    Some("CA"),
    Some("California"),
    None,
    "Success",
    Some("America/Los_Angeles"),
    None)

  val ipPairSummary = IpPairSummary(Some(12), ip1Info, ip2Info)

  "CirceSupport" should {
    import system.dispatcher

    "enable marshalling and unmarshalling objects for generic derivation for simple objects" in {
      import io.circe.generic.auto._
      val entity = Await.result(Marshal(foo).to[RequestEntity], 100.millis)
      Await.result(Unmarshal(entity).to[Foo], 100.millis) shouldBe foo
    }

    "enable marshalling and unmarshalling objects for generic derivation for simple objects with Options" in {
      import io.circe.generic.auto._
      val entity = Await.result(Marshal(ip1Info).to[RequestEntity], 100.millis)
      Await.result(Unmarshal(entity).to[IpInfo], 100.millis) shouldBe ip1Info
    }

    "enable marshalling and unmarshalling objects for generic derivation for complex objects with Options" in {
      import io.circe.generic.auto._
      val entity = Await.result(Marshal(ipPairSummary).to[RequestEntity], 100.millis)
      Await.result(Unmarshal(entity).to[IpPairSummary], 100.millis) shouldBe ipPairSummary
    }
  }

  override protected def afterAll() = {
    Await.ready(system.terminate(), Duration.Inf)
    super.afterAll()
  }
}