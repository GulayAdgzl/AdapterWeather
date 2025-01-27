import com.example.weatheradapter.data.api.WeatherStackService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherStackModule {
    
    @Provides
    @Singleton
    fun provideWeatherStackApi(): WeatherStackApi {
        return WeatherStackService.api
    }

    @Provides
    @Singleton
    fun provideWeatherStackRepository(
        weatherStackApi: WeatherStackApi,
        dispatchers: CoroutineDispatchers
    ): WeatherStackRepository {
        return WeatherStackRepository(weatherStackApi, dispatchers)
    }

    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return object : CoroutineDispatchers {
            override val main: CoroutineDispatcher = Dispatchers.Main
            override val io: CoroutineDispatcher = Dispatchers.IO
            override val default: CoroutineDispatcher = Dispatchers.Default
        }
    }
}

