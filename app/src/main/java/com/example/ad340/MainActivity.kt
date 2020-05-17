package com.example.ad340

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ad340.details.ForecastDetailsActivity

class MainActivity : AppCompatActivity() {

    private val forecastRepository = ForecastRepository()
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    // region Setup methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tempDisplaySettingManager = TempDisplaySettingManager(this)

        val editTextZipCode : EditText = findViewById(R.id.editTextZipCode)
        val enterButtonZipCode : Button = findViewById(R.id.buttonZipCode)

        enterButtonZipCode.setOnClickListener {
            val textZipCode : String = editTextZipCode.text.toString()
            if (textZipCode.length != 5) {
                Toast.makeText(this, R.string.invalid_zip_code, Toast.LENGTH_SHORT).show()
            } else {
                forecastRepository.loadForecast(textZipCode)
            }
        }

        val recyclerViewForecast: RecyclerView = findViewById(R.id.recyclerViewForecast)
        recyclerViewForecast.layoutManager = LinearLayoutManager(this)
        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager) {forecastItem ->
            showForecastDetails(forecastItem)
        }
        recyclerViewForecast.adapter = dailyForecastAdapter

        val weeklyForecastObserver = Observer<List<DailyForecast>> {forecastItems ->
            //update our weekly adapter
            dailyForecastAdapter.submitList(forecastItems)
        }
        forecastRepository.weeklyForecast.observe(this, weeklyForecastObserver)

    }

    private fun showForecastDetails(forecast : DailyForecast) {
        val forecastDetailsIntent = Intent(this, ForecastDetailsActivity::class.java)
        forecastDetailsIntent.putExtra("key_temp", forecast.temp)
        forecastDetailsIntent.putExtra("key_desc", forecast.description)
        startActivity(forecastDetailsIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.tempDisplaySetting -> {
                showTempDisplaySettingDialog(this, tempDisplaySettingManager)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
