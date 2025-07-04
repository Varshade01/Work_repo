package com.maat.cha.core.navigation.navigator

import kotlinx.coroutines.flow.MutableSharedFlow

interface AppNavigator {
    val navigationFlow: MutableSharedFlow<NavigationIntent>

    suspend fun navigateBack(
        route: String? = null,
        inclusive: Boolean = false,
    )

    suspend fun navigateTo(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = false,
        isSingleTop: Boolean = false,
    )

    suspend fun minimizeApp()
}