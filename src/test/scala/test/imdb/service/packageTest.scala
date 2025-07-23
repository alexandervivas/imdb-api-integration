package test.imdb.service


import org.scalatest.funsuite.AnyFunSuiteLike
import test.imdb.client.response.SearchTitleResponse
import test.imdb.client.response.SearchTitleResponse.ImdbTitle
import test.imdb.model.MovieType

class packageTest extends AnyFunSuiteLike {

  test("ImdbTitleOps.toMovie should correctly map all fields") {
    val imdbTitle = ImdbTitle(
      id = "tt1234567",
      `type` = "movie",
      primaryTitle = "Test Primary",
      originalTitle = "Test Original",
      genres = Some(List("Action", "Comedy")),
      startYear = Some(2024),
      runtimeSeconds = Some(7200),
      rating = Some(SearchTitleResponse.ImdbRating(aggregateRating = 7.8, voteCount = 1234)),
      primaryImage = None,
      endYear = None,
      metacritic = Some(SearchTitleResponse.ImdbMetacritic("http://meta", 88, 100)),
      plot = Some("Sample plot"),
      originCountries = Some(List(SearchTitleResponse.ImdbCountry("US", "United States"))),
      spokenLanguages = Some(List(SearchTitleResponse.ImdbLanguage("en", "English")))
    )

    val movie = imdbTitle.toMovie

    assert(movie.`type` == MovieType.Movie)
    assert(movie.primaryTitle == "Test Primary")
    assert(movie.originalTitle == "Test Original")
    assert(movie.genres == List("Action", "Comedy"))
    assert(movie.rating.contains(7.8))
    assert(movie.startYear.contains(2024))
    assert(movie.runtimeInSeconds.contains(7200))
    assert(movie.actors.isEmpty)
    assert(movie.directors.isEmpty)
    assert(movie.writers.isEmpty)
    assert(movie.cast.isEmpty)
  }

  test("ImdbTitleOps.toMovie should default to Unknown MovieType for invalid types") {
    val imdbTitle = ImdbTitle(
      id = "tt7654321",
      `type` = "unknown_type",
      primaryTitle = "Unknown Type",
      originalTitle = "Original Unknown",
      genres = None,
      startYear = None,
      runtimeSeconds = None,
      rating = None,
      primaryImage = None,
      endYear = None,
      metacritic = None,
      plot = None,
      originCountries = None,
      spokenLanguages = None
    )

    val movie = imdbTitle.toMovie

    assert(movie.`type` == MovieType.Unknown)
    assert(movie.genres == Nil)
    assert(movie.rating.isEmpty)
    assert(movie.startYear.isEmpty)
    assert(movie.runtimeInSeconds.isEmpty)
  }
}
