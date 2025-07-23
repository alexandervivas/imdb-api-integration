package test.imdb.client.response

final case class SearchTitleResponse(titles: Seq[SearchTitleResponse.ImdbTitle])

object SearchTitleResponse {
  final case class ImdbTitle(
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

  final case class ImdbImage(
                        url: String,
                        width: Int,
                        height: Int
                      )

  final case class ImdbRating(
                         aggregateRating: Double,
                         voteCount: Int
                       )

  final case class ImdbMetacritic(
                             url: String,
                             score: Int,
                             reviewCount: Int
                           )

  final case class ImdbCountry(
                          code: String,
                          name: String
                        )

  final case class ImdbLanguage(
                           code: String,
                           name: String
                         )
}
