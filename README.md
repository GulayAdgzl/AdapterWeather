# Weather App with Adapter Pattern

This project demonstrates the implementation of the Adapter Design Pattern in an Android weather application. The app integrates multiple weather service APIs (WeatherStack) using a unified interface through the Adapter pattern.

## Technical Stack

- **Language:** Kotlin
- **Architecture:** MVVM + Clean Architecture
- **Minimum SDK:** 24
- **Target SDK:** 34

## Key Technologies & Libraries

```gradle
dependencies {
    // Retrofit for network calls
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'
    
    // Hilt for dependency injection
    implementation 'com.google.dagger:hilt-android:2.48'
    kapt 'com.google.dagger:hilt-compiler:2.48'
    
    // Coroutines for async operations
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3'
    
    // Lifecycle components
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.6.2'
}
```

## Project Structure

```plaintext
com.example.weatheradapter/
├── data/
│   ├── api/
│   │   ├── WeatherStackApi.kt
│   │   └── WeatherStackService.kt
│   ├── model/
│   │   ├── Current.kt
│   │   ├── Location.kt
│   │   ├── Error.kt
│   │   └── WeatherStackResponse.kt
│   ├── repository/
│   │   └── WeatherStackRepository.kt
│   └── adapter/
│       └── WeatherAdapter.kt
├── domain/
│   └── model/
│       └── Weather.kt
├── di/
│   └── WeatherStackModule.kt
├── ui/
│   └── weather/
│       ├── WeatherActivity.kt
│       └── WeatherViewModel.kt
└── WeatherApplication.kt
```

## Design Pattern Implementation

### Adapter Pattern

```kotlin
interface WeatherAdapter {
    suspend fun getWeatherInfo(city: String): Weather
}

class WeatherStackAdapter : WeatherAdapter {
    override suspend fun getWeatherInfo(city: String): Weather {
        // Converts WeatherStack response to app's Weather model
    }
}
```

## Key Components

### 1. Data Layer

#### WeatherStackService
```kotlin
object WeatherStackService {
    private const val BASE_URL = "http://api.weatherstack.com/"
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) 
                HttpLoggingInterceptor.Level.BODY 
            else 
                HttpLoggingInterceptor.Level.NONE
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
```

#### Repository
```kotlin
class WeatherStackRepository @Inject constructor(
    private val weatherStackApi: WeatherStackApi
) {
    suspend fun getWeatherForCity(city: String): Result<Weather>
}
```

### 2. Domain Layer

#### Weather Model
```kotlin
data class Weather(
    val temperature: Double,
    val humidity: Int,
    val description: String,
    val windSpeed: Double,
    val city: String,
    val timestamp: Long = System.currentTimeMillis()
)
```

### 3. Presentation Layer

#### ViewModel
```kotlin
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherStackRepository: WeatherRepository
) : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()
}
```

## Setup Instructions

### 1. API Key Configuration

Add to `local.properties`:
```properties
WEATHER_STACK_API_KEY=your_api_key_here
```

### 2. Build Configuration

In `build.gradle`:
```gradle
android {
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    
    buildTypes {
        debug {
            buildConfigField("String", "WEATHER_STACK_API_KEY", "\"${properties['WEATHER_STACK_API_KEY'] ?: ""}\"")
        }
        release {
            buildConfigField("String", "WEATHER_STACK_API_KEY", "\"${properties['WEATHER_STACK_API_KEY'] ?: ""}\"")
        }
    }
}
```

### 3. Manifest Setup

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:name=".WeatherApplication"
        android:allowBackup="true"
        android:usesCleartextTraffic="true"
        ...>
    </application>
</manifest>
```

## Error Handling

```kotlin
sealed class WeatherUiState {
    object Initial : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weather: Weather) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
```

## Best Practices Implemented

### 1. Clean Architecture
- Separation of concerns
- Dependency inversion
- Single responsibility principle

### 2. SOLID Principles
- Interface segregation (WeatherAdapter)
- Open/closed principle
- Dependency inversion principle

### 3. Error Handling
- Comprehensive exception handling
- User-friendly error messages
- Network state management

### 4. Security
- API key protection
- Secure network communication
- Input validation

## Performance Considerations

### 1. Network Optimization
- OkHttp client configuration
- Connection pooling
- Timeout settings

### 2. Memory Management
- ViewModel for configuration changes
- Proper coroutine scope management
- Efficient data transformations

## Future Improvements

1. Offline caching support
2. Multiple weather service integration
3. Unit test coverage expansion
4. UI/UX enhancements
5. Weather data persistence
6. Location-based weather updates

## Contribution Guidelines

1. Fork the repository
2. Create feature branch
3. Follow coding standards
4. Include tests
5. Submit pull request

## License

```
MIT License

Copyright (c) 2024 Weather Adapter App

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---
For any questions or issues, please open an issue in the repository.

Happy coding! 🚀