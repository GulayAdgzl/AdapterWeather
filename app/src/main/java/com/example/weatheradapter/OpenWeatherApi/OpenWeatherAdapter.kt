class OpenWeatherAdapter(
    private val openWeatherApi: OpenWeatherApi
) : WeatherAdapter {
    override suspend fun getWeatherInfo(city: String): Weather {
        val response = openWeatherApi.getWeather(city)
        
        return Weather(
            temperature = response.main.temp,
            humidity = response.main.humidity,
            description = response.weather.firstOrNull()?.description ?: "",
            windSpeed = response.wind.speed,
            city = response.name
        )
    }
}