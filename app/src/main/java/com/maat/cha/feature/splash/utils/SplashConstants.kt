package com.maat.cha.feature.splash.utils

/**
 * Constants used throughout the splash feature
 */
object SplashConstants {
    // Timing constants
    const val SPLASH_DELAY_MS = 2000L
    const val BANNER_DELAY_MS = 1000L
    const val DOUBLE_TAP_TIMEOUT_MS = 2000L
    const val API_TIMEOUT_MS = 15000L
    
    // UI constants
    const val BANNER_BOTTOM_PADDING_DP = 124
    const val ERROR_SCREEN_PADDING_DP = 32
    const val ERROR_BUTTON_HEIGHT_DP = 48
    
    // Colors
    const val PRIMARY_COLOR = 0xFFFDB001
    const val ERROR_BUTTON_COLOR = 0xFFFDB001
    
    // Logging tags
    const val TAG_SPLASH_VIEWMODEL = "SplashViewModel"
    const val TAG_SPLASH_WEBVIEW = "SplashWebView"
    const val TAG_SPLASH_BANNER = "SplashBanner"
    
    // Error messages
    const val ERROR_NO_INTERNET = "No internet connection"
    const val ERROR_REQUEST_TIMEOUT = "Request timeout"
    const val ERROR_INVALID_URL = "Invalid URL provided"
    const val ERROR_WEBVIEW_LOAD_FAILED = "Failed to load content"
    const val ERROR_UNEXPECTED = "An unexpected error occurred"
    
    // Toast messages
    const val TOAST_PRESS_BACK_AGAIN = "Press back again to exit"
    
    // URL validation patterns
    val HTTP_URL_PATTERN = Regex("^https?://.*")
    val VALID_URL_PATTERN = Regex("^https?://[\\w\\-._~:/?#\\[\\]@!\\$&'\\(\\)\\*\\+,;=.]+$")
} 