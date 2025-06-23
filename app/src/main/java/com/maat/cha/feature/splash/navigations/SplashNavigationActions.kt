package com.maat.cha.feature.splash.navigations

interface SplashNavigationActions {
    suspend fun navigateToMain()
    suspend fun openWebView(url: String)
    suspend fun closeApp()
} 