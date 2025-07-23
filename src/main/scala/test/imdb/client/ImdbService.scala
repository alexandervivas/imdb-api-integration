package test.imdb.client

import com.twitter.finagle.http.Request
import com.twitter.util.Future
import test.imdb.client.response.SearchTitleResponse
import test.imdb.config.ImdbConfig

trait ImdbService {

  def getMoviesByTitle(title: String): Future[Option[SearchTitleResponse]]

  protected def buildRequest(imdbConfig: ImdbConfig, path: String, queryParams: Map[String, String]): Request = {
    val query = queryParams.map { case (key, value) =>
      s"${java.net.URLEncoder.encode(key, "UTF-8")}=${java.net.URLEncoder.encode(value, "UTF-8")}"
    }.mkString("&")
    val request = Request(s"${imdbConfig.endpoint}$path?$query")
    request.host(imdbConfig.host)
    request.headerMap.add("accept", "application/json")
    request
  }

}
