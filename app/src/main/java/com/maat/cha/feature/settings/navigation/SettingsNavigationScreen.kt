package com.maat.cha.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.feature.settings.screen.SettingsScreen

fun NavGraphBuilder.settingsScreen() {
    composable(route = Destination.Settings.fullRoute) {
        SettingsScreen()
    }
} 