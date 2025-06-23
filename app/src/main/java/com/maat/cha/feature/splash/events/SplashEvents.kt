package com.maat.cha.feature.splash.events

import com.maat.cha.core.data.model.MatchViewResponse
import com.maat.cha.feature.splash.state.SplashError

/**
 * Represents all possible events that can be triggered in the splash screen
 */
sealed class SplashEvents {
    // Lifecycle events
    object OnSplashDelayComplete : SplashEvents()
    object OnRetryApiCall : SplashEvents()
    
    // User interaction events
    object OnBannerClicked : SplashEvents()
    object OnWebViewBackPressed : SplashEvents()
    object OnBannerBackPressed : SplashEvents()
    
    // Navigation events
    object OnExternalNavigationDetected : SplashEvents()
    object OnReturnFromExternalNavigation : SplashEvents()
    
    // Error events
    object OnWebViewLoadError : SplashEvents()
    
    // Data events
    data class OnApiResponseReceived(val response: MatchViewResponse) : SplashEvents()
    data class OnApiError(val error: SplashError) : SplashEvents()
} 