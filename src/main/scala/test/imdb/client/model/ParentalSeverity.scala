package test.imdb.client.model

import enumeratum._

sealed trait ParentalSeverity extends EnumEntry

object ParentalSeverity extends Enum[ParentalSeverity] {
  val values: IndexedSeq[ParentalSeverity] = findValues

  case object None extends ParentalSeverity

  case object Mild extends ParentalSeverity

  case object Moderate extends ParentalSeverity

  case object Severe extends ParentalSeverity
}
