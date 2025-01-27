interface WeatherStackApi {
    @GET("current")
    suspend fun getCurrentWeather(
        @Query("access_key") apiKey: String,
        @Query("query") city: String,
        @Query("units") units: String = "m"  // m for metric
    ): WeatherStackResponse
}