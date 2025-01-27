package com.example.weatheradapter.WeatherActivity

import Weather
import WeatherViewModel
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.weatheradapter.R
import com.example.weatheradapter.databinding.ActivityWeatherBinding
import kotlinx.coroutines.launch


class WeatherActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeWeather()
    }

    private fun setupUI() {
        binding.searchButton.setOnClickListener {
            val city = binding.cityEditText.text.toString()
            viewModel.fetchWeather(city)
        }
    }

    private fun observeWeather() {
        lifecycleScope.launch {
            viewModel.weatherState.collect { state ->
                when (state) {
                    is WeatherUiState.Initial -> {
                        // Başlangıç durumu
                    }
                    is WeatherUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is WeatherUiState.Success -> {
                        binding.progressBar.isVisible = false
                        showWeather(state.weather)
                    }
                    is WeatherUiState.Error -> {
                        binding.progressBar.isVisible = false
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun showWeather(weather: Weather) {
        binding.apply {
            temperatureText.text = "${weather.temperature}°C"
            humidityText.text = "${weather.humidity}%"
            descriptionText.text = weather.description
            windSpeedText.text = "${weather.windSpeed} m/s"
            cityText.text = weather.city
        }
    }

    private fun showError(message: String) {
        binding.errorText.text = message
        binding.errorText.isVisible = true
    }
}