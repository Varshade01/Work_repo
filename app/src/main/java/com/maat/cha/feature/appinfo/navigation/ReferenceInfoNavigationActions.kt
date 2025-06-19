package com.maat.cha.feature.appinfo.navigation

import com.maat.cha.core.navigation.destinations.InfoType

interface ReferenceInfoNavigationActions {
    suspend fun navigateBack()
    suspend fun navigateToInfo(type: InfoType)
    suspend fun navigateToGame()
} 