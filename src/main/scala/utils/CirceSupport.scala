package utils

import akka.http.scaladsl.marshalling.{ Marshaller, ToEntityMarshaller }
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.unmarshalling.{ FromEntityUnmarshaller, Unmarshaller }
import io.circe.{ Decoder, Encoder, Json, Printer, jawn }

/**
  * Automatic to and from JSON marshalling/unmarshalling using an in-scope *Circe* protocol.
  *
  * To use automatic codec derivation, user need to import `circe.generic.auto._`.
  */
object CirceSupport extends CirceSupport

/**
  * JSON marshalling/unmarshalling using an in-scope *Circe* protocol.
  *
  * To use automatic codec derivation, user need to import `io.circe.generic.auto._`
  */
trait CirceSupport {

  /**
    * HTTP entity => `A`
    *
    * @param decoder decoder for `A`, probably created by `circe.generic`
    * @tparam A type to decode
    * @return unmarshaller for `A`
    */
  implicit def circeUnmarshaller[A](implicit decoder: Decoder[A]): FromEntityUnmarshaller[A] =
    Unmarshaller
      .byteStringUnmarshaller
      .forContentTypes(`application/json`)
      .mapWithCharset((data, charset) => jawn.decode(data.decodeString(charset.nioCharset.name)).valueOr(throw _))

  /**
    * `A` => HTTP entity
    *
    * @param encoder encoder for `A`, probably created by `circe.generic`
    * @param printer pretty printer function
    * @tparam A type to encode
    * @return marshaller for any `A` value
    */
  implicit def circeToEntityMarshaller[A](implicit encoder: Encoder[A], printer: Json => String = Printer.noSpaces.pretty): ToEntityMarshaller[A] =
    Marshaller.StringMarshaller.wrap(`application/json`)(printer).compose(encoder.apply)
}