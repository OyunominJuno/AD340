package com.example.ad340.forecast

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.ad340.R
import kotlin.Float
import kotlin.Int
import kotlin.String

class WeekForecastFragmentDirections private constructor() {
  private data class ActionWeekForecastFragment3ToForecastDetailsFragment(
    val temp: Float,
    val description: String
  ) : NavDirections {
    override fun getActionId(): Int = R.id.action_weekForecastFragment3_to_forecastDetailsFragment

    override fun getArguments(): Bundle {
      val result = Bundle()
      result.putFloat("temp", this.temp)
      result.putString("description", this.description)
      return result
    }
  }

  companion object {
    fun actionWeekForecastFragment3ToForecastDetailsFragment(temp: Float, description: String):
        NavDirections = ActionWeekForecastFragment3ToForecastDetailsFragment(temp, description)

    fun actionWeekForecastFragment3ToLocationEntryFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_weekForecastFragment3_to_locationEntryFragment)
  }
}
