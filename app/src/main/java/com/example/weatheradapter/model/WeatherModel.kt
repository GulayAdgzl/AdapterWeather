data class Weather(
    val temperature: Double,
    val humidity: Int,
    val description: String,
    val windSpeed: Double,
    val city: String,
    val timestamp: Long = System.currentTimeMillis()
)