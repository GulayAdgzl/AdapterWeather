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

