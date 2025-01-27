class CityNotFoundException : Exception("City not found")

class InvalidApiKeyException : Exception("Invalid API key")

class NetworkException : Exception("Network error occurred")

class WeatherStackException(message: String) : Exception(message)