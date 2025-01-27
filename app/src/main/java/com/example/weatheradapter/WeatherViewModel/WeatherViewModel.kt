@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherStackRepository: WeatherStackRepository
) : ViewModel() {
    
    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            _weatherState.value = WeatherUiState.Loading
            
            weatherStackRepository.getWeatherForCity(city)
                .onSuccess { weather ->
                    _weatherState.value = WeatherUiState.Success(weather)
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        is CityNotFoundException -> "City not found"
                        is NetworkException -> "Check your internet connection"
                        is InvalidApiKeyException -> "API key error"
                        else -> error.message ?: "Unknown error occurred"
                    }
                    _weatherState.value = WeatherUiState.Error(errorMessage)
                }
        }
    }
}

sealed class WeatherUiState {
    object Initial : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weather: Weather) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}