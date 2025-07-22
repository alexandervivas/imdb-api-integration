package test.imdb.service

import com.google.inject.{Inject, Singleton}
import com.twitter.util.Future
import test.imdb.client.ImdbService
import test.imdb.model.Movie

@Singleton
class MoviesService @Inject()(imdbService: ImdbService) {

  def getMoviesByTitle(title: String, parentalThreshold: Option[Int]): Future[Option[Movie]] = ???
}
