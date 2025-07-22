package test.imdb.client

import com.google.inject.Inject
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future
import io.circe.generic.auto._
import io.circe.parser.decode
import test.imdb.client.response.SearchTitleResponse
import test.imdb.config.ImdbConfig

class ImdbServiceImpl @Inject()(
                                 httpService: Service[Request, Response],
                                 config: ImdbConfig
                               ) extends ImdbService {

  override def getMoviesByTitle(title: String): Future[Option[SearchTitleResponse]] =
    httpService(
      buildRequest(
        config,
        "/search/titles",
        Map("query" -> title)
      )
    ).map { response => decode[SearchTitleResponse](response.contentString).toOption }
}
