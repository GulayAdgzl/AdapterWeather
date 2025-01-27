data class OpenWeatherResponse(
    val main: Main,
    val weather: List<WeatherInfo>,
    val wind: Wind,
    val name: String
) {
    data class Main(val temp: Double, val humidity: Int)
    data class WeatherInfo(val description: String)
    data class Wind(val speed: Double)
}

interface OpenWeatherApi {
    suspend fun getWeather(city: String): OpenWeatherResponse
}

// API implementasyonu
class OpenWeatherApiImpl : OpenWeatherApi {
    override suspend fun getWeather(city: String): OpenWeatherResponse {
        // Retrofit veya başka bir HTTP client ile API çağrısı
        return OpenWeatherResponse(
            OpenWeatherResponse.Main(25.0, 50),
            listOf(OpenWeatherResponse.WeatherInfo("Clear sky")),
            OpenWeatherResponse.Wind(5.0),
            "Istanbul"
        )
    }
}