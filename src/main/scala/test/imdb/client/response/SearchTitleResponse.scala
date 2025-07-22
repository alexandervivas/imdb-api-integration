package test.imdb.client.response

case class SearchTitleResponse(titles: Seq[SearchTitleResponse.ImdbTitle])

object SearchTitleResponse {
  case class ImdbTitle(
                        id: String,
                        `type`: String,
                        primaryTitle: String,
                        originalTitle: String,
                        primaryImage: Option[ImdbImage],
                        startYear: Option[Int],
                        endYear: Option[Int],
                        runtimeSeconds: Option[Int],
                        genres: Option[List[String]],
                        rating: Option[ImdbRating],
                        metacritic: Option[ImdbMetacritic],
                        plot: Option[String],
                        originCountries: Option[List[ImdbCountry]],
                        spokenLanguages: Option[List[ImdbLanguage]]
                      )

  case class ImdbImage(
                        url: String,
                        width: Int,
                        height: Int
                      )

  case class ImdbRating(
                         aggregateRating: Double,
                         voteCount: Int
                       )

  case class ImdbMetacritic(
                             url: String,
                             score: Int,
                             reviewCount: Int
                           )

  case class ImdbCountry(
                          code: String,
                          name: String
                        )

  case class ImdbLanguage(
                           code: String,
                           name: String
                         )
}
