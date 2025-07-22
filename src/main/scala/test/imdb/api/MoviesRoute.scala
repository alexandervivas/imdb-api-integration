package test.imdb.api

import com.google.inject.{Inject, Singleton}
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future
import io.circe.generic.auto._
import io.circe.syntax._
import test.imdb.service.MoviesService

@Singleton
class MoviesRoute @Inject()(movieService: MoviesService) extends Service[Request, Response] {

  override def apply(req: Request): Future[Response] = {
    val title = req.params.get("titleName")
    val threshold = req.params.get("parentalRatingThreshold").flatMap(t => scala.util.Try(t.toInt).toOption)

    title match {
      case Some(t) =>
        movieService.getMoviesByTitle(t, threshold).map {
          case Some(movie) =>
            val response = Response()
            response.contentString = movie.asJson.noSpaces
            response.contentType = "application/json"
            response.status = Status.Ok
            response
          case None =>
            val response = Response(Status.NotFound)
            response.contentString = "Movie not found or filtered out"
            response
        }

      case None =>
        val response = Response(Status.BadRequest)
        response.contentString = "Missing titleName parameter"
        Future.value(response)
    }
  }
}
