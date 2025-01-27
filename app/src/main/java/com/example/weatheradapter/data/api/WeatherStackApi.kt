
import com.example.weatheradapter.model.WeatherStackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherStackApi {
    @GET("current")
    suspend fun getCurrentWeather(
        @Query("access_key") apiKey: String,
        @Query("query") city: String,
        @Query("units") units: String = "m"  // m for metric
    ): WeatherStackResponse
}