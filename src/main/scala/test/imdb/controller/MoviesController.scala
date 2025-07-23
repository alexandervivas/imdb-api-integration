package test.imdb.controller

import com.google.inject.{Inject, Singleton}
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future
import io.circe.generic.auto._
import io.circe.syntax._
import test.imdb.service.MoviesService

@Singleton
class MoviesController @Inject()(movieService: MoviesService) extends Service[Request, Response] {

  override def apply(req: Request): Future[Response] = req.path match {
    case "/movies" => handleMovieRequest(req)
    case _ => notFound(req.path)
  }

  private def handleMovieRequest(req: Request): Future[Response] = {
    val title = req.params.get("titleName")
    val threshold = req.params.get("parentalRatingThreshold").flatMap(t => scala.util.Try(t.toInt).toOption)

    title match {
      case Some(titleName) =>
        movieService.getMoviesByTitle(titleName, threshold).map {
          case Nil =>
            val response = Response(Status.NotFound)
            response.contentString = "Movie not found or filtered out"
            response
          case movies =>
            val response = Response()
            response.contentString = movies.asJson.noSpaces
            response.contentType = "application/json"
            response.status = Status.Ok
            response
        }

      case None =>
        val response = Response(Status.BadRequest)
        response.contentString = "Missing required query param: titleName"
        Future.value(response)
    }
  }

  private def notFound(path: String): Future[Response] = {
    val response = Response(Status.NotFound)
    response.contentString = s"No handler for path: $path"
    Future.value(response)
  }
}
