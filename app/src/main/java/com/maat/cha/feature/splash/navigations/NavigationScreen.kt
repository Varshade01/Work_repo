package com.maat.cha.feature.splash.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.feature.splash.screen.SplashScreen

fun NavGraphBuilder.splashScreen() {
    composable(route = Destination.Splash.fullRoute) {
        SplashScreen()
    }
}
