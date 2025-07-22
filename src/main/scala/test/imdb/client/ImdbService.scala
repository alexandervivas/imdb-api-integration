package test.imdb.client

import com.twitter.util.Future
import test.imdb.client.model.ImdbResponse

trait ImdbService {

  def getMoviesByTitle(title: String): Future[Option[ImdbResponse]]

}
