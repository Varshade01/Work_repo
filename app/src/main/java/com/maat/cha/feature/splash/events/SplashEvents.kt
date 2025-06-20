package com.maat.cha.feature.splash.events

sealed class SplashEvents {
    object OnDelayComplete : SplashEvents()
    object OnRetryApi : SplashEvents()
    object OnBannerClick : SplashEvents()
    object OnWebViewBackPressed : SplashEvents()
    object OnBannerBackPressed : SplashEvents()
    object OnExternalNavigation : SplashEvents()
    object OnReturnFromExternal : SplashEvents()
    object OnWebViewError : SplashEvents()
} 