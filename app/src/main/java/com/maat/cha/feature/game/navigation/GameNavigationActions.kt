package com.maat.cha.feature.game.navigation

import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.core.navigation.destinations.ReferenceInfoSource

interface GameNavigationActions {
    suspend fun navigateBack()
    suspend fun navigateToMain()
    suspend fun navigateToInfo(type: InfoType)
    suspend fun navigateToReferenceInfo(type: InfoType, source: ReferenceInfoSource = ReferenceInfoSource.ONBOARDING)
} 