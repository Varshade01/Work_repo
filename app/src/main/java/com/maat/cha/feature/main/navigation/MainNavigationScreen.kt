package com.maat.cha.feature.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.feature.main.screens.MainScreen

fun NavGraphBuilder.mainScreen() {
    composable(route = Destination.Main.fullRoute) {
        MainScreen()
    }
} 