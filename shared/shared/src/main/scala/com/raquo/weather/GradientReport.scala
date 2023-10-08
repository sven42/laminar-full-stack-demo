package com.raquo.weather

import scala.scalajs.js.annotation.JSExportAll

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*

/**
  *
  * @param cities                  List of cities to display, in order
  * @param currentConditionsByCity cityId -> currentConditions.
  *                                Note: some records might be missing if current conditions are not available
  * @param forecastDays            list of day captions, in order to be displayed
  * @param forecastsByDay          (day_caption -> cityId -> forecast)
  */
@JSExportAll
case class GradientReport(
  cities: List[CityStation],
  currentConditionsByCity: Map[String, CityCurrentConditions],
  forecastDays: List[String],
  forecastsByDay: Map[String, Map[String, CityForecast]]
)

object GradientReport {

  given codec: Codec[GradientReport] = deriveCodec
}