package test.imdb.client

import com.google.inject.Inject
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future
import com.typesafe.scalalogging.LazyLogging
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import test.imdb.client.response.{RpcStatus, SearchTitleResponse}
import test.imdb.config.ImdbConfig

class ImdbServiceImpl @Inject()(
                                 httpService: Service[Request, Response],
                                 config: ImdbConfig
                               ) extends ImdbService with LazyLogging {

  override def getMoviesByTitle(title: String): Future[Option[SearchTitleResponse]] =
    httpService(
      buildRequest(
        config,
        "/search/titles",
        Map("query" -> title)
      )
    ).flatMap { response =>
      val responseJson = parse(response.contentString).getOrElse(Json.Null)

      if (response.status != Status.Ok) {
        logger.debug(s"IMDB API response: ${response.contentString}")
        responseJson.as[RpcStatus] match {
          case Right(error) =>
            logger.error(s"IMDB API error: code=${error.code}, message=${error.message}")
          case Left(parsingError) =>
            logger.error(s"Failed to parse IMDB error payload: ${parsingError.getMessage}")
        }
        Future.value(None)
      } else {
        responseJson.as[SearchTitleResponse] match {
          case Right(result) => Future.value(Some(result))
          case Left(error) =>
            logger.error(s"Failed to parse successful IMDB response: ${error.getMessage}")
            Future.value(None)
        }
      }
    }

  private def buildRequest(imdbConfig: ImdbConfig, path: String, queryParams: Map[String, String]): Request = {
    val query = queryParams.map { case (key, value) =>
      s"${java.net.URLEncoder.encode(key, "UTF-8")}=${java.net.URLEncoder.encode(value, "UTF-8")}"
    }.mkString("&")
    val request = Request(s"${imdbConfig.endpoint}$path?$query")
    request.host(imdbConfig.host)
    request.headerMap.add("accept", "application/json")
    logger.debug(s"Request: $request")
    request
  }
}
