package test.imdb.client.response

import io.circe.Decoder
import io.circe.generic.semiauto._

final case class RpcStatus(code: Int, message: String)

object RpcStatus {
  implicit val decoder: Decoder[RpcStatus] = deriveDecoder
}
