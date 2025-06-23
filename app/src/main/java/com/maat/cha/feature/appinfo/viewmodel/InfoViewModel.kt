package com.maat.cha.feature.appinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.core.datastore.utils.DataStorePreferences
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.feature.appinfo.model.InfoScreenType
import com.maat.cha.feature.appinfo.navigation.InfoNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
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
                is InfoScreenType.HowToPlay -> {
                    navigationActions.navigateToInfo(InfoType.TERMS_OF_USE)
                }
                is InfoScreenType.TermsOfUse -> {
                    navigationActions.navigateToInfo(InfoType.PRIVACY)
                }
                is InfoScreenType.Privacy -> {
                    // Save that user has read all info screens
                    dataStorePreferences.setHasReadAllInfo(true)
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
            }
        }
    }

    fun onBottomTextClick() {
        viewModelScope.launch {
            navigationActions.navigateBack()
        }
    }
} 