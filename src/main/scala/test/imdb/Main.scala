package test.imdb

import com.google.inject.Guice
import com.twitter.finagle.Http
import com.twitter.util.Await
import test.imdb.controller.MoviesController
import test.imdb.module.AppModule

object Main extends App {

  // Crear el inyector con el módulo de configuración
  val injector = Guice.createInjector(new AppModule())

  // Obtener el servicio REST (MovieRoute en este caso debe ser un Service[Request, Response])
  val service = injector.getInstance(classOf[MoviesController])

  // Levantar el servidor Finagle
  val server = Http.serve(":8080", service)

  println("Server running at http://localhost:8080/")
  Await.ready(server)
}
