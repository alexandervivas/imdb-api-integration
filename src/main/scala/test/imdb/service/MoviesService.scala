package test.imdb.service

import com.google.inject.{Inject, Singleton}
import com.twitter.util.Future
import test.imdb.client.ImdbService
import test.imdb.model.Movie

@Singleton
class MoviesService @Inject()(imdbService: ImdbService) {

  def getMoviesByTitle(title: String, parentalThreshold: Option[Int]): Future[Seq[Movie]] =
    imdbService.getMoviesByTitle(title).map {
      case Some(response) =>
        response
          .titles
          .map(_.toMovie)
          .filter(validParentalThreshold(parentalThreshold))
      case None => Nil
    }

  private def validParentalThreshold(parentalThreshold: Option[Int])(movie: Movie): Boolean =
    parentalThreshold.forall(threshold => movie.rating.exists(_ >= threshold))
}
