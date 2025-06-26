package com.maat.cha.feature.appinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.core.datastore.utils.DataStorePreferences
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.feature.appinfo.model.InfoScreenType
import com.maat.cha.feature.appinfo.navigation.InfoNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val navigationActions: InfoNavigationActions,
    private val dataStorePreferences: DataStorePreferences
) : ViewModel() {

    fun onMainButtonClick(screenType: InfoScreenType) {
        viewModelScope.launch {
            when (screenType) {
                is InfoScreenType.Privacy -> {
                    // Mark privacy as accepted and read
                    dataStorePreferences.setPrivacyAccepted(true)
                    dataStorePreferences.setPrivacyRead(true)
                    navigationActions.navigateToInfo(InfoType.TERMS_OF_USE)
                }
                is InfoScreenType.TermsOfUse -> {
                    // Mark terms as read and proceed to how to play
                    dataStorePreferences.setTermsOfUseRead(true)
                    navigationActions.navigateToInfo(InfoType.HOW_TO_PLAY)
                }
                is InfoScreenType.HowToPlay -> {
                    // Mark how to play as read and finish
                    dataStorePreferences.setHowToPlayRead(true)
                    // Check if all screens are now read, then set hasReadAllInfo
                    val privacyRead = dataStorePreferences.privacyRead.first()
                    val termsOfUseRead = dataStorePreferences.termsOfUseRead.first()
                    if (privacyRead && termsOfUseRead) {
                        dataStorePreferences.setHasReadAllInfo(true)
                    }
                    navigationActions.navigateToGame()
                }
                is InfoScreenType.PrivacyPolicy -> {
                    // For settings privacy policy, just go back
                    navigationActions.navigateBack()
                }
                is InfoScreenType.TermsOfUsePolicy -> {
                    // For settings terms of use, just go back
                    navigationActions.navigateBack()
                }
                is InfoScreenType.PrivacyPolicyWebView -> {
                    // This should not be called directly
                    navigationActions.navigateBack()
                }
            }
        }
    }

    fun onBottomTextClick() {
        viewModelScope.launch {
            // For privacy screen, do nothing - user must accept
            // For other screens, navigate back
            navigationActions.navigateBack()
        }
    }
} 