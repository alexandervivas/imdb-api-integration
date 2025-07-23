package test.imdb.model

import enumeratum._

sealed trait MovieType extends EnumEntry

object MovieType extends Enum[MovieType] {
  val values: IndexedSeq[MovieType] = findValues

  case object Movie extends MovieType

  case object TvSeries extends MovieType

  case object Short extends MovieType

  case object Documentary extends MovieType

  case object Video extends MovieType

  case object TvMovie extends MovieType

  case object TvMiniSeries extends MovieType

  case object TvSpecial extends MovieType

  case object VideoGame extends MovieType

  case object Unknown extends MovieType
}
