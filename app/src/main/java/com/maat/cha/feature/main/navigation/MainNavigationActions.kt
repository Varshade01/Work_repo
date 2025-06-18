package com.maat.cha.feature.main.navigation

import com.maat.cha.core.navigation.destinations.InfoType

interface MainNavigationActions {
    suspend fun navigateToSettings()
    suspend fun navigateToInfo(type: InfoType)
    suspend fun navigateToGame()
    suspend fun minimizeApp()
    suspend fun navigateToReferenceInfo(type: InfoType)
} 