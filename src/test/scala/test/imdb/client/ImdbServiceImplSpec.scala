package test.imdb.client

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.{Await, Future => TwitterFuture}
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalatest.funsuite.AnyFunSuiteLike
import test.imdb.client.response.SearchTitleResponse
import test.imdb.client.response.SearchTitleResponse.ImdbTitle
import test.imdb.config.ImdbConfig

class ImdbServiceImplSpec extends AnyFunSuiteLike {

  test("should parse a valid JSON response correctly") {
    val imdbConfig = ImdbConfig(host = "testhost.com", endpoint = "/test-endpoint")

    val dummyResponse = SearchTitleResponse(
      titles = List(
        ImdbTitle(
          id = "tt1234567",
          `type` = "movie",
          primaryTitle = "Test Title",
          originalTitle = "Test Title",
          primaryImage = Some(SearchTitleResponse.ImdbImage("http://image.url", 100, 200)),
          startYear = Some(2020),
          endYear = Some(2021),
          runtimeSeconds = Some(7200),
          genres = Some(List("Drama")),
          rating = Some(SearchTitleResponse.ImdbRating(8.5, 10000)),
          metacritic = Some(SearchTitleResponse.ImdbMetacritic("https://metacritic.com", 80, 120)),
          plot = Some("A movie plot."),
          originCountries = Some(List(SearchTitleResponse.ImdbCountry("US", "United States"))),
          spokenLanguages = Some(List(SearchTitleResponse.ImdbLanguage("en", "English")))
        )
      )
    )

    //noinspection ScalaUnusedSymbol
    val mockHttpService: Service[Request, Response] = (request: Request) => {
      val response = Response(Status.Ok)
      response.setContentString(dummyResponse.asJson.noSpaces)
      TwitterFuture.value(response)
    }

    val service = new ImdbServiceImpl(mockHttpService, imdbConfig)
    val result = Await.result(service.getMoviesByTitle("some-title"))

    assert(result.isDefined)
    assert(result.get.titles.head.primaryTitle == "Test Title")
  }

  test("should return None when JSON is invalid") {
    val imdbConfig = ImdbConfig(host = "testhost.com", endpoint = "/test-endpoint")

    //noinspection ScalaUnusedSymbol
    val mockHttpService: Service[Request, Response] = (request: Request) => {
      val response = Response(Status.Ok)
      response.setContentString("not a valid json")
      TwitterFuture.value(response)
    }

    val service = new ImdbServiceImpl(mockHttpService, imdbConfig)
    val result = Await.result(service.getMoviesByTitle("invalid-json"))

    assert(result.isEmpty)
  }

  test("should build the correct request path and host") {
    var capturedRequest: Option[Request] = None
    val imdbConfig = ImdbConfig(host = "expected.host", endpoint = "/expected-endpoint")

    //noinspection ScalaUnusedSymbol
    val mockHttpService: Service[Request, Response] = (request: Request) => {
      capturedRequest = Some(request)
      val response = Response(Status.Ok)
      response.setContentString("""{"titles": []}""")
      TwitterFuture.value(response)
    }

    val service = new ImdbServiceImpl(mockHttpService, imdbConfig)
    Await.result(service.getMoviesByTitle("testing"))

    assert(capturedRequest.isDefined)
    assert(capturedRequest.get.uri == "/expected-endpoint/search/titles?query=testing")
    assert(capturedRequest.get.host.contains("expected.host"))
  }

  test("should parse a rich JSON response with multiple titles") {
    val jsonResponse =
      """{
        |  "titles": [
        |    {
        |      "id": "tt10676052",
        |      "type": "movie",
        |      "primaryTitle": "The Fantastic Four: First Steps",
        |      "originalTitle": "The Fantastic Four: First Steps",
        |      "primaryImage": {
        |        "url": "https://m.media-amazon.com/images/M/MV5BOGM5MzA3MDAtYmEwMi00ZDNiLTg4MDgtMTZjOTc0ZGMyNTIwXkEyXkFqcGc@._V1_.jpg",
        |        "width": 1086,
        |        "height": 1609
        |      },
        |      "startYear": 2025
        |    },
        |    {
        |      "id": "tt30826447",
        |      "type": "tvSeries",
        |      "primaryTitle": "The Four Seasons",
        |      "originalTitle": "The Four Seasons",
        |      "primaryImage": {
        |        "url": "https://m.media-amazon.com/images/M/MV5BYjljNDUyMDAtOTg5Mi00Njc4LWFhNTgtOGU0NThmMzE0N2JjXkEyXkFqcGc@._V1_.jpg",
        |        "width": 1080,
        |        "height": 1350
        |      },
        |      "startYear": 2025,
        |      "endYear": 2026,
        |      "rating": {
        |        "aggregateRating": 7.2,
        |        "voteCount": 29163
        |      }
        |    }
        |  ]
        |}""".stripMargin

    val imdbConfig = ImdbConfig(host = "api.imdbapi.dev", endpoint = "/search")

    //noinspection ScalaUnusedSymbol
    val mockHttpService: Service[Request, Response] = (request: Request) => {
      val response = Response(Status.Ok)
      response.setContentString(jsonResponse)
      TwitterFuture.value(response)
    }

    val service = new ImdbServiceImpl(mockHttpService, imdbConfig)
    val result = Await.result(service.getMoviesByTitle("four"))

    assert(result.nonEmpty)
    assert(result.exists(_.titles.exists(_.id == "tt10676052")))
    assert(result.exists(_.titles.exists(_.id == "tt30826447")))
    assert(result.flatMap(_.titles.find(_.id == "tt30826447").flatMap(_.rating.map(_.aggregateRating))).contains(7.2))
  }

  test("should log and return None when response is error with valid RpcStatus payload") {
    val imdbConfig = ImdbConfig(host = "api.imdbapi.dev", endpoint = "/search")

    val errorJson =
      """{
        |  "code": 7,
        |  "message": "Permission denied",
        |  "details": []
        |}""".stripMargin

    val mockHttpService: Service[Request, Response] = (_: Request) => {
      val response = Response(Status.Forbidden)
      response.setContentString(errorJson)
      TwitterFuture.value(response)
    }

    val service = new ImdbServiceImpl(mockHttpService, imdbConfig)
    val result = Await.result(service.getMoviesByTitle("forbidden-query"))

    assert(result.isEmpty)
  }

  test("should log and return None when response is error with invalid RpcStatus payload") {
    val imdbConfig = ImdbConfig(host = "api.imdbapi.dev", endpoint = "/search")

    val invalidErrorJson = """{"unexpected": "format"}"""

    val mockHttpService: Service[Request, Response] = (_: Request) => {
      val response = Response(Status.BadRequest)
      response.setContentString(invalidErrorJson)
      TwitterFuture.value(response)
    }

    val service = new ImdbServiceImpl(mockHttpService, imdbConfig)
    val result = Await.result(service.getMoviesByTitle("bad-query"))

    assert(result.isEmpty)
  }

  test("should return None when status is OK but SearchTitleResponse is invalid") {
    val imdbConfig = ImdbConfig(host = "api.imdbapi.dev", endpoint = "/search")

    val invalidJson = """{"not": "a valid SearchTitleResponse"}"""

    val mockHttpService: Service[Request, Response] = (_: Request) => {
      val response = Response(Status.Ok)
      response.setContentString(invalidJson)
      TwitterFuture.value(response)
    }

    val service = new ImdbServiceImpl(mockHttpService, imdbConfig)
    val result = Await.result(service.getMoviesByTitle("invalid-content"))

    assert(result.isEmpty)
  }
}
