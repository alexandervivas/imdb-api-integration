package test.imdb

import com.twitter.util.{Future => TwitterFuture}

import scala.concurrent.{Promise, Future => ScalaFuture}

package object client {

  implicit class TwitterFutureOps[T](twitterFuture: TwitterFuture[T]) {

    def toScala: ScalaFuture[T] = {
      val promise = Promise[T]()
      twitterFuture.respond {
        case com.twitter.util.Return(value) => promise.success(value)
        case com.twitter.util.Throw(e) => promise.failure(e)
      }
      promise.future
    }

  }

}
