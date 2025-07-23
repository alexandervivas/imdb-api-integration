package test.imdb

import com.google.inject.Guice
import com.twitter.finagle.Http
import com.twitter.util.Await
import test.imdb.controller.MoviesController
import test.imdb.module.AppModule

object Main extends App {

  private val injector = Guice.createInjector(new AppModule())

  val service = injector.getInstance(classOf[MoviesController])

  private val server = Http.serve(":8080", service)

  println("Server running at http://localhost:8080/")
  Await.ready(server)
}
