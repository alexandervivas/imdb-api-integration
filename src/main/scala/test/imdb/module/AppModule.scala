package test.imdb.module

import com.google.inject.{AbstractModule, Provides, Singleton}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import test.imdb.client.{ImdbService, ImdbServiceImpl}
import test.imdb.config.ImdbConfig

import java.util.Properties

class AppModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[ImdbService]).to(classOf[ImdbServiceImpl])
  }

  @Provides
  @Singleton
  def provideImdbHttpService(): Service[Request, Response] = {
    Http.client.newService("imdbapi.dev:443")
  }

  @Provides
  @Singleton
  def provideImdbConfig(): ImdbConfig = {
    val props = new Properties()
    val stream = getClass.getClassLoader.getResourceAsStream("imdb.properties")
    if (stream == null) {
      throw new RuntimeException("Missing imdb.properties in resources")
    }
    props.load(stream)

    ImdbConfig(
      host = props.getProperty("imdb.api.host"),
      endpoint = props.getProperty("imdb.api.endpoint")
    )
  }
}
