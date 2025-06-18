package com.maat.cha.feature.game.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.feature.game.screens.GameScreen

fun NavGraphBuilder.gameScreen() {
    composable(route = Destination.Game.fullRoute) {
        GameScreen()
    }
} 