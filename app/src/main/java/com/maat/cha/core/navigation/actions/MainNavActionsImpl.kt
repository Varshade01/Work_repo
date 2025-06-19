package com.maat.cha.core.navigation.actions

import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.core.navigation.destinations.ReferenceInfoSource
import com.maat.cha.core.navigation.navigator.AppNavigator
import com.maat.cha.feature.main.navigation.MainNavigationActions
import javax.inject.Inject

class MainNavActionsImpl @Inject constructor(
    private val navigator: AppNavigator
) : MainNavigationActions {
    override suspend fun navigateToSettings() {
        navigator.navigateTo(Destination.Settings.fullRoute)
    }

    override suspend fun navigateToInfo(type: InfoType) {
        navigator.navigateTo(Destination.Info.createRoute(type))
    }

    override suspend fun navigateToGame() {
        navigator.navigateTo(Destination.Game.fullRoute)
    }

    override suspend fun minimizeApp() {
        navigator.minimizeApp()
    }

    override suspend fun navigateToReferenceInfo(type: InfoType, source: ReferenceInfoSource) {
        navigator.navigateTo(Destination.ReferenceInfo.createRoute(type, source))
    }
}