class WeatherStackAdapter(
    private val weatherStackApi: WeatherStackApi
) : WeatherAdapter {
    override suspend fun getWeatherInfo(city: String): Weather {
        val response = weatherStackApi.getCurrentWeather(city)
        
        return Weather(
            temperature = response.current.temperature.toDouble(),
            humidity = response.current.humidity,
            description = response.current.weather_descriptions.firstOrNull() ?: "",
            windSpeed = response.current.wind_speed.toDouble(),
            city = response.location.name
        )
    }
}