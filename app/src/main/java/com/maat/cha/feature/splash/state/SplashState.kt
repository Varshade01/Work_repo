package com.maat.cha.feature.splash.state

import com.maat.cha.core.data.model.MatchViewResponse

/**
 * Represents the current state of the splash screen
 */
data class SplashState(
    val uiState: SplashUiState = SplashUiState.Initial,
    val originalApiResponse: MatchViewResponse? = null,
    val errorMessage: String = "",
    val isRetryable: Boolean = false
)

/**
 * Represents the UI state of the splash screen
 */
sealed class SplashUiState {
    object Initial : SplashUiState()
    object Loading : SplashUiState()
    object ApiLoading : SplashUiState()
    data class WebView(
        val url: String
    ) : SplashUiState()
    data class Banner(
        val imageUrl: String
    ) : SplashUiState()
    data class Error(
        val message: String,
        val isRetryable: Boolean = false
    ) : SplashUiState()
}

/**
 * Represents different types of errors that can occur
 */
sealed class SplashError {
    object NetworkError : SplashError()
    object TimeoutError : SplashError()
    object InvalidUrlError : SplashError()
    object WebViewLoadError : SplashError()
    data class ApiError(val message: String) : SplashError()
    data class UnknownError(val message: String) : SplashError()
    
    fun toUserMessage(): String = when (this) {
        is NetworkError -> "No internet connection"
        is TimeoutError -> "Request timeout"
        is InvalidUrlError -> "Invalid URL provided"
        is WebViewLoadError -> "Failed to load content"
        is ApiError -> message
        is UnknownError -> "An unexpected error occurred"
    }
    
    fun isRetryable(): Boolean = when (this) {
        is NetworkError, is TimeoutError, is ApiError -> true
        is InvalidUrlError, is WebViewLoadError, is UnknownError -> false
    }
} 