package com.example.weatheradapter.model

data class WeatherStackResponse(
    val current: Current,
    val location: Location,
    val success: Boolean = true,
    val error: Error? = null
)

// data/model/Current.kt
data class Current(
    val temperature: Int,
    val humidity: Int,
    val weather_descriptions: List<String>,
    val wind_speed: Int,
    val observation_time: String,
    val pressure: Int,
    val visibility: Int,
    val is_day: String,
    val feelslike: Int,
    val uv_index: Int,
    val precipitation: Double
)

// data/model/Location.kt
data class Location(
    val name: String,
    val country: String,
    val region: String,
    val lat: String,
    val lon: String,
    val timezone_id: String,
    val localtime: String
)

// data/model/Error.kt
data class Error(
    val code: Int,
    val type: String,
    val info: String
)