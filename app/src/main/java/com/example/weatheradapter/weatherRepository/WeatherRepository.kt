class WeatherRepository(
    private val weatherAdapter: WeatherAdapter
) {
    suspend fun getWeatherForCity(city: String): Weather {
        return weatherAdapter.getWeatherInfo(city)
    }
}