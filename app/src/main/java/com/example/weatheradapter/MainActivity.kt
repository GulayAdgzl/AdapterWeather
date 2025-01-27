package com.example.weatheradapter

import Weather
import WeatherViewModel
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.weatheradapter.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        observeWeather()
    }
    
    private fun setupUI() {
        with(binding) {
            searchButton.setOnClickListener {
                val city = cityEditText.text.toString()
                if (city.isNotBlank()) {
                    viewModel.fetchWeather(city)
                    hideKeyboard()
                } else {
                    showSnackbar("Please enter a city name")
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                val city = cityEditText.text.toString()
                if (city.isNotBlank()) {
                    viewModel.fetchWeather(city)
                } else {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }
    
    private fun observeWeather() {
        lifecycleScope.launch {
            viewModel.weatherState.collect { state ->
                binding.swipeRefreshLayout.isRefreshing = false
                
                when (state) {
                    is WeatherUiState.Initial -> handleInitialState()
                    is WeatherUiState.Loading -> handleLoadingState()
                    is WeatherUiState.Success -> handleSuccessState(state.weather)
                    is WeatherUiState.Error -> handleErrorState(state.message)
                }
            }
        }
    }
    
    private fun handleInitialState() {
        with(binding) {
            weatherCardView.isVisible = false
            errorLayout.isVisible = false
            loadingLayout.isVisible = false
        }
    }
    
    private fun handleLoadingState() {
        with(binding) {
            loadingLayout.isVisible = true
            weatherCardView.isVisible = false
            errorLayout.isVisible = false
        }
    }
    
    private fun handleSuccessState(weather: Weather) {
        with(binding) {
            loadingLayout.isVisible = false
            errorLayout.isVisible = false
            weatherCardView.isVisible = true
            
            cityNameText.text = weather.city
            temperatureText.text = getString(R.string.temperature_format, weather.temperature)
            humidityText.text = getString(R.string.humidity_format, weather.humidity)
            descriptionText.text = weather.description
            windSpeedText.text = getString(R.string.wind_speed_format, weather.windSpeed)
            
            // Update last updated time
            lastUpdatedText.text = getString(
                R.string.last_updated,
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(weather.timestamp))
            )
        }
    }
    
    private fun handleErrorState(errorMessage: String) {
        with(binding) {
            loadingLayout.isVisible = false
            weatherCardView.isVisible = false
            errorLayout.isVisible = true
            errorText.text = errorMessage
            
            retryButton.setOnClickListener {
                val city = cityEditText.text.toString()
                if (city.isNotBlank()) {
                    viewModel.fetchWeather(city)
                }
            }
        }
    }
    
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}

// WeatherApplication.kt
@HiltAndroidApp
class WeatherApplication : Application()