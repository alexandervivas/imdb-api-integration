package test.imdb.client.model

case class ImdbResponse(titles: Seq[ImdbResponse.ImdbTitle])

object ImdbResponse {
  case class ImdbTitle(
                        `type`: String,
                        primaryTitle: String,
                        originalTitle: String,
                        genres: List[String],
                        rating: Option[Double],
                        startYear: Option[Int],
                        runtimeInSeconds: Option[Int],
                        actors: List[String],
                        directors: List[String],
                        writers: List[String],
                        cast: List[String]
                      )
}
