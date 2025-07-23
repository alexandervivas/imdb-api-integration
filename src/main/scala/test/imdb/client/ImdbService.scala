package test.imdb.client

import com.twitter.util.Future
import test.imdb.client.response.SearchTitleResponse

trait ImdbService {

  def getMoviesByTitle(title: String): Future[Option[SearchTitleResponse]]

}
