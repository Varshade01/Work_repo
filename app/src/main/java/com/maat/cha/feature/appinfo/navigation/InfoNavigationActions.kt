package com.maat.cha.feature.appinfo.navigation

import com.maat.cha.core.navigation.destinations.InfoType

interface InfoNavigationActions {
    suspend fun navigateToInfo(type: InfoType)
    suspend fun navigateToGame()
    suspend fun navigateBack()
} 