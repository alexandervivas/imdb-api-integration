package test.imdb.client.model

case class ParentalGuide(
                          sexAndNudity: Option[ParentalSeverity],
                          violenceAndGore: Option[ParentalSeverity]
                        )
