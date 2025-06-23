package com.maat.cha.feature.settings.navigation

import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.core.navigation.destinations.ReferenceInfoSource

interface SettingsNavigationActions {
    suspend fun navigateBack()
    suspend fun navigateToReferenceInfo(type: InfoType, source: ReferenceInfoSource = ReferenceInfoSource.ONBOARDING)
} 