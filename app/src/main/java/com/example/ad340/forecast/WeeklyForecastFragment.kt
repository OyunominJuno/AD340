package com.example.ad340.forecast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.ad340.*
import com.example.ad340.api.DailyForecast
import com.example.ad340.api.WeeklyForecast

import com.example.ad340.details.ForecastDetailsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class WeekForecastFragment : Fragment() {

    private val forecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val zipcode = arguments?.getString(KEY_ZIPCODE) ?: ""

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)

        val locationEntryFloatingButton : FloatingActionButton = view.findViewById(R.id.locationEntryFloatingButton)
        locationEntryFloatingButton.setOnClickListener {
            showLocationEntry()
        }

        val recyclerViewForecast: RecyclerView = view.findViewById(R.id.recyclerViewForecast)
        recyclerViewForecast.layoutManager = LinearLayoutManager(requireContext())
        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager) {forecastItem ->
            showForecastDetails(forecastItem)
        }
        recyclerViewForecast.adapter = dailyForecastAdapter

        val weeklyForecastObserver = Observer<WeeklyForecast> { weeklyForecast ->
            //update our weekly adapter
            dailyForecastAdapter.submitList(weeklyForecast.daily)
        }
        forecastRepository.weeklyForecast.observe(viewLifecycleOwner, weeklyForecastObserver)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> forecastRepository.loadWeeklyForecast(savedLocation.zipcode)
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        return view
    }

    private fun showLocationEntry() {
        val action = WeekForecastFragmentDirections.actionWeekForecastFragment3ToLocationEntryFragment()
        findNavController().navigate(action)
    }

    private fun showForecastDetails(forecast : DailyForecast) {
        val temp = forecast.temp.max
        val description = forecast.weather[0].description
        val date = forecast.date * 1000

        val icon = forecast.weather[0].icon
        //forecast.load("http://openweathermap.org/img/wn/${iconId}@2x.png")
        //val icon = "temp icon"
        val action = WeekForecastFragmentDirections.actionWeekForecastFragment3ToForecastDetailsFragment(temp, description, date, icon)
        findNavController().navigate(action)
    }

    companion object {
        private const val KEY_ZIPCODE = "key_zipcode"

        fun newInstance(zipcode: String) : WeekForecastFragment {
            val fragment = WeekForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments = args

            return fragment
        }
    }

}
