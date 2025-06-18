package com.maat.cha.feature.game.navigation

import com.maat.cha.core.navigation.destinations.InfoType

interface GameNavigationActions {
    suspend fun navigateBack()
    suspend fun navigateToMain()
    suspend fun navigateToInfo(type: InfoType)
    suspend fun navigateToReferenceInfo(type: InfoType)
} 