package com.example.ad340.forecast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ad340.*
import com.example.ad340.api.CurrentWeather
import com.example.ad340.api.DailyForecast

import com.example.ad340.details.ForecastDetailsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_current_forecast.*
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 */
class CurrentForecastFragment : Fragment() {

    private val forecastRepository = ForecastRepository()
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val zipcode = arguments?.getString(KEY_ZIPCODE) ?: ""

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)
        val locationName = view.findViewById<TextView>(R.id.locationName)
        val tempText: TextView = view.findViewById(R.id.tempText)
        val emptyText = view.findViewById<TextView>(R.id.emptyText)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        val locationEntryFloatingButton : FloatingActionButton = view.findViewById(R.id.locationEntryFloatingButton)
        locationEntryFloatingButton.setOnClickListener {
            showLocationEntry()
        }

        //observer that updates the UI when forecast changes
        val currentWeatherObserver = Observer<CurrentWeather> {weather ->
            emptyText.visibility = View.GONE
            progressBar.visibility = View.GONE
            locationName.visibility = View.VISIBLE
            tempText.visibility = View.VISIBLE

            locationName.text = weather.name
            tempText.text = formatTempForDisplay(weather.forecast.temp, tempDisplaySettingManager.getTempDisplaySetting())
        }
        forecastRepository.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> {
                    progressBar.visibility = View.VISIBLE
                    forecastRepository.loadCurrentForecast(savedLocation.zipcode)
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        return view
    }
    private fun showLocationEntry() {
        val action = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }

    companion object {
        private const val KEY_ZIPCODE = "key_zipcode"

        fun newInstance(zipcode: String) : CurrentForecastFragment {
            val fragment = CurrentForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments = args

            return fragment
        }
    }

}
