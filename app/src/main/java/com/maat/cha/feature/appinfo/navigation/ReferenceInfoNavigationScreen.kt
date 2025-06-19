package com.maat.cha.feature.appinfo.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.maat.cha.core.navigation.destinations.Destination
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.core.navigation.destinations.ReferenceInfoSource
import com.maat.cha.feature.appinfo.model.InfoScreenType
import com.maat.cha.feature.appinfo.screen.ReferenceInfoScreen

fun NavGraphBuilder.referenceInfoScreen() {
    composable(route = Destination.ReferenceInfo.fullRoute) { backStackEntry ->
        val typeString = backStackEntry.arguments?.getString("type")
        val sourceString = backStackEntry.arguments?.getString("source") ?: ReferenceInfoSource.ONBOARDING.name
        val infoType = when (typeString) {
            InfoType.HOW_TO_PLAY.name -> InfoScreenType.HowToPlay
            InfoType.TERMS_OF_USE.name -> InfoScreenType.TermsOfUse
            InfoType.PRIVACY.name -> InfoScreenType.Privacy
            InfoType.PRIVACY_POLICY.name -> InfoScreenType.PrivacyPolicy
            InfoType.TERMS_OF_USE_POLICY.name -> InfoScreenType.TermsOfUsePolicy
            else -> InfoScreenType.HowToPlay
        }
        val source = ReferenceInfoSource.valueOf(sourceString)
        ReferenceInfoScreen(screenType = infoType, source = source)
    }
} 