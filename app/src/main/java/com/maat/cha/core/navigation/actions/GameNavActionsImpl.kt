package com.maat.cha.core.navigation.actions

import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.core.navigation.destinations.ReferenceInfoSource
import com.maat.cha.core.navigation.navigator.AppNavigator
import com.maat.cha.feature.game.navigation.GameNavigationActions
import javax.inject.Inject

class GameNavActionsImpl @Inject constructor(
    private val navigator: AppNavigator
) : GameNavigationActions {
    override suspend fun navigateBack() {
        navigator.navigateBack()
    }

    override suspend fun navigateToMain() {
        navigator.navigateTo(
            route = Destination.Main.fullRoute,
            popUpToRoute = Destination.Main.fullRoute,
            inclusive = true
        )
    }

    override suspend fun navigateToInfo(type: InfoType) {
        navigator.navigateTo(Destination.Info.createRoute(type))
    }

    override suspend fun navigateToReferenceInfo(type: InfoType, source: ReferenceInfoSource) {
        navigator.navigateTo(Destination.ReferenceInfo.createRoute(type, source))
    }
} 