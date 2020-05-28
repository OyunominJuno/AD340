package com.example.ad340.forecast

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.ad340.R
import kotlin.Float
import kotlin.Int
import kotlin.String

class CurrentForecastFragmentDirections private constructor() {
  private data class ActionCurrentForecastFragmentToForecastDetailsFragment(
    val temp: Float,
    val description: String
  ) : NavDirections {
    override fun getActionId(): Int = R.id.action_currentForecastFragment_to_forecastDetailsFragment

    override fun getArguments(): Bundle {
      val result = Bundle()
      result.putFloat("temp", this.temp)
      result.putString("description", this.description)
      return result
    }
  }

  companion object {
    fun actionCurrentForecastFragmentToLocationEntryFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_currentForecastFragment_to_locationEntryFragment)

    fun actionCurrentForecastFragmentToForecastDetailsFragment(temp: Float, description: String):
        NavDirections = ActionCurrentForecastFragmentToForecastDetailsFragment(temp, description)
  }
}
