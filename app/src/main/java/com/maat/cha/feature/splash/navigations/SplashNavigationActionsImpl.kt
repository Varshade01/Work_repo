package com.maat.cha.feature.splash.navigations

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.core.navigation.navigator.AppNavigator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import androidx.core.net.toUri

class SplashNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator,
    @ApplicationContext private val context: Context
) : SplashNavigationActions {

    override suspend fun navigateToMain() {
        Log.d("SplashNavActions", "Navigating to main")
        appNavigator.navigateTo(
            route = Destination.Main.fullRoute,
            popUpToRoute = Destination.Splash.fullRoute,
            inclusive = true,
            isSingleTop = true
        )
    }

    override suspend fun openWebView(url: String) {
        Log.d("SplashNavActions", "Opening WebView with URL: $url")
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override suspend fun closeApp() {
        Log.d("SplashNavActions", "Closing app")
        appNavigator.minimizeApp()
    }
} 