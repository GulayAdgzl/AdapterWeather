interface WeatherAdapter {
    suspend fun getWeatherInfo(city: String): Weather
}