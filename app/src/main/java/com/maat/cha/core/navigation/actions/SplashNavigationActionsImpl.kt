package com.maat.cha.core.navigation.actions

import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.core.navigation.navigator.AppNavigator
import com.maat.cha.feature.splash.navigations.SplashNavigationActions
import javax.inject.Inject

class SplashNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator,
) : SplashNavigationActions {

    override suspend fun navigateToMain() {
        appNavigator.navigateTo(
            route = Destination.Main.route,
            popUpToRoute = Destination.Splash.route,
            inclusive = true,
            isSingleTop = true
        )
    }
}