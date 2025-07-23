package test.imdb

import test.imdb.client.response.SearchTitleResponse.ImdbTitle
import test.imdb.model.{Movie, MovieType}

package object service {

  implicit class ImdbTitleOps(imdbTitle: ImdbTitle) {

    def toMovie: Movie = Movie(
      `type` = MovieType.withNameInsensitiveOption(imdbTitle.`type`).getOrElse(MovieType.Unknown),
      primaryTitle = imdbTitle.primaryTitle,
      originalTitle = imdbTitle.originalTitle,
      genres = imdbTitle.genres.getOrElse(Nil),
      rating = imdbTitle.rating.map(_.aggregateRating),
      startYear = imdbTitle.startYear,
      runtimeInSeconds = imdbTitle.runtimeSeconds,
      actors = Nil,
      directors = Nil,
      writers = Nil,
      cast = Nil
    )

  }

}
