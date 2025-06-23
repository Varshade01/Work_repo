package com.maat.cha.core.navigation.navigator

import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor() : AppNavigator {

    override val navigationFlow = MutableSharedFlow<NavigationIntent>()

    override suspend fun navigateBack(route: String?, inclusive: Boolean) {
        navigationFlow.emit(
            NavigationIntent.NavigateBack(
                route = route,
                inclusive = inclusive
            )
        )
    }

    override suspend fun navigateTo(
        route: String,
        popUpToRoute: String?,
        inclusive: Boolean,
        isSingleTop: Boolean
    ) {
        navigationFlow.emit(
            NavigationIntent.NavigateTo(
                route = route,
                popUpToRoute = popUpToRoute,
                inclusive = inclusive,
                isSingleTop = isSingleTop,
            )
        )
    }

    override suspend fun minimizeApp() {
        navigationFlow.emit(NavigationIntent.MinimizeApp)
    }
}