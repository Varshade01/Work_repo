package com.maat.cha.core.navigation.actions

import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.core.navigation.navigator.AppNavigator
import com.maat.cha.feature.appinfo.navigation.InfoNavigationActions
import javax.inject.Inject

class InfoNavActionsImpl @Inject constructor(
    private val navigator: AppNavigator
) : InfoNavigationActions {
    override suspend fun navigateToInfo(type: InfoType) {
        navigator.navigateTo(Destination.Info.createRoute(type))
    }

    override suspend fun navigateToGame() {
        navigator.navigateTo(Destination.Game.fullRoute)
    }

    override suspend fun navigateBack() {
        navigator.navigateBack()
    }
} 