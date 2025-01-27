@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {
    @Provides
    @Singleton
    fun provideWeatherAdapter(): WeatherAdapter {
        // Hangi API'yi kullanacağımızı burada seçiyoruz
        val openWeatherApi = OpenWeatherApiImpl()
        return OpenWeatherAdapter(openWeatherApi)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherAdapter: WeatherAdapter
    ): WeatherRepository {
        return WeatherRepository(weatherAdapter)
    }
}