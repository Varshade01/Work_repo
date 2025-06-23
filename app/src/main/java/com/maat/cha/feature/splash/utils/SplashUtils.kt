package com.maat.cha.feature.splash.utils

import androidx.core.net.toUri
import com.maat.cha.core.data.model.MatchViewResponse
import com.maat.cha.feature.splash.state.SplashError
import com.maat.cha.feature.splash.state.SplashUiState

/**
 * Utility functions for the splash feature
 */
object SplashUtils {

    /**
     * Validates if a URL is valid and safe to load
     */
    fun isValidUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false

        return try {
            val uri = url.toUri()
            uri.scheme?.let { scheme ->
                scheme.equals("http", true) || scheme.equals("https", true)
            } == true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Determines the appropriate UI state based on API response
     */
    fun determineUiState(response: MatchViewResponse): SplashUiState {
        val hasLink = !response.launchLink.isNullOrBlank() && isValidUrl(response.launchLink)
        val hasImage = !response.matchImg.isNullOrBlank()

        return when {
            hasLink && hasImage -> {
                // Both link and image - show WebView
                SplashUiState.WebView(url = response.launchLink)
            }

            hasLink && !hasImage -> {
                // Only link - show WebView
                SplashUiState.WebView(url = response.launchLink)
            }

            !hasLink && hasImage -> {
                // Only image - show banner
                SplashUiState.Banner(imageUrl = response.matchImg)
            }

            else -> {
                // No valid data - this should be handled by the ViewModel
                SplashUiState.Error(
                    message = "No valid content available",
                    isRetryable = false
                )
            }
        }
    }

    /**
     * Maps exceptions to appropriate error types
     */
    fun mapExceptionToError(exception: Throwable): SplashError {
        return when (exception) {
            is java.net.UnknownHostException,
            is java.net.ConnectException,
            is java.net.SocketTimeoutException -> {
                SplashError.NetworkError
            }

            is kotlinx.coroutines.TimeoutCancellationException -> {
                SplashError.TimeoutError
            }

            is IllegalArgumentException -> {
                SplashError.InvalidUrlError
            }

            else -> {
                SplashError.UnknownError(exception.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Sanitizes URL for safe loading
     */
    fun sanitizeUrl(url: String): String {
        return url.trim().let { trimmedUrl ->
            if (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://")) {
                "https://$trimmedUrl"
            } else {
                trimmedUrl
            }
        }
    }
} 