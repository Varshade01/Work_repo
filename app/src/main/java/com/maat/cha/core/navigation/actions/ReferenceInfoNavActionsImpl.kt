package com.maat.cha.core.navigation.actions

import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.core.navigation.navigator.AppNavigator
import com.maat.cha.feature.appinfo.navigation.ReferenceInfoNavigationActions
import javax.inject.Inject

class ReferenceInfoNavActionsImpl @Inject constructor(
    private val navigator: AppNavigator
) : ReferenceInfoNavigationActions {
    override suspend fun navigateBack() {
        navigator.navigateBack()
    }
    
    override suspend fun navigateToInfo(type: InfoType) {
        navigator.navigateTo(Destination.Info.createRoute(type))
    }
    
    override suspend fun navigateToGame() {
        navigator.navigateTo(Destination.Game.fullRoute)
    }
} 