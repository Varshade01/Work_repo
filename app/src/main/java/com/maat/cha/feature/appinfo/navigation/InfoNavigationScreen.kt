package com.maat.cha.feature.appinfo.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.feature.appinfo.model.InfoScreenType
import com.maat.cha.feature.appinfo.screen.InfoScreen

fun NavGraphBuilder.infoScreen() {
    composable(route = Destination.Info.fullRoute) { backStackEntry ->
        val typeString = backStackEntry.arguments?.getString("type")
        val infoType = when (typeString) {
            InfoType.HOW_TO_PLAY.name -> InfoScreenType.HowToPlay
            InfoType.TERMS_OF_USE.name -> InfoScreenType.TermsOfUse
            InfoType.PRIVACY.name -> InfoScreenType.Privacy
            else -> InfoScreenType.HowToPlay
        }
        InfoScreen(screenType = infoType)
    }
} 