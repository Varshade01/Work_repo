package com.maat.cha.core.navigation.actions

import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.core.navigation.destinations.ReferenceInfoSource
import com.maat.cha.core.navigation.navigator.AppNavigator
import com.maat.cha.feature.settings.navigation.SettingsNavigationActions
import javax.inject.Inject

class SettingsNavActionsImpl @Inject constructor(
    private val navigator: AppNavigator
) : SettingsNavigationActions {
    override suspend fun navigateBack() {
        navigator.navigateBack()
    }
    
    override suspend fun navigateToReferenceInfo(type: InfoType, source: ReferenceInfoSource) {
        navigator.navigateTo(Destination.ReferenceInfo.createRoute(type, source))
    }
} 