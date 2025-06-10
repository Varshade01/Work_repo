package com.maat.cha.core.navigation.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.maat.cha.core.navigation.LocalNavController
import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.core.navigation.getNavController
import com.maat.cha.core.navigation.navigator.NavigationIntent
import com.maat.cha.core.navigation.viewmodel.NavigationViewModel
import com.maat.cha.feature.splash.navigations.splashScreen
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun Root(
    navigationViewModel: NavigationViewModel = hiltViewModel(),
) {
    CompositionLocalProvider(
        LocalNavController provides getNavController()
    ) {
        NavigationEffects(
            navHostController = getNavController(),
            navigationFlow = navigationViewModel.navigationFlow,
        )

        NavHost(
            navController = getNavController(),
            startDestination = Destination.Splash.fullRoute
        ) {
            splashScreen()
        }
    }

}

@Composable
fun NavigationEffects(
    navHostController: NavHostController,
    navigationFlow: SharedFlow<NavigationIntent>
) {
    LaunchedEffect(navHostController) {
        navigationFlow.collect { intent ->
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }

                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                        }
                    }
                }
            }
        }
    }
}