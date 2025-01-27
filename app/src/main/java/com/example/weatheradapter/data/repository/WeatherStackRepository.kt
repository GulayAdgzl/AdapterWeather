class WeatherStackRepository @Inject constructor(
    private val weatherStackApi: WeatherStackApi,
    private val dispatchers: CoroutineDispatchers
) {
    suspend fun getWeatherForCity(city: String): Result<Weather> {
        return withContext(dispatchers.io) {
            try {
                val response = weatherStackApi.getCurrentWeather(
                    apiKey = BuildConfig.WEATHER_STACK_API_KEY,
                    city = city
                )

                if (!response.success) {
                    val error = response.error
                    when (error?.code) {
                        615 -> Result.failure(CityNotFoundException())
                        601 -> Result.failure(InvalidApiKeyException())
                        else -> Result.failure(
                            WeatherStackException(error?.info ?: "Unknown error")
                        )
                    }
                } else {
                    Result.success(response.toWeather())
                }
            } catch (e: IOException) {
                Result.failure(NetworkException())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun WeatherStackResponse.toWeather(): Weather {
        return Weather(
            temperature = current.temperature.toDouble(),
            humidity = current.humidity,
            description = current.weather_descriptions.firstOrNull() ?: "",
            windSpeed = current.wind_speed.toDouble(),
            city = location.name,
            country = location.country,
            timestamp = System.currentTimeMillis(),
            feelsLike = current.feelslike.toDouble(),
            pressure = current.pressure,
            visibility = current.visibility,
            isDay = current.is_day == "yes",
            uvIndex = current.uv_index,
            precipitation = current.precipitation
        )
    }
}