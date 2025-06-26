package com.maat.cha.feature.appinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.core.datastore.utils.DataStorePreferences
import com.maat.cha.core.navigation.destinations.ReferenceInfoSource
import com.maat.cha.feature.appinfo.model.InfoScreenType
import com.maat.cha.feature.appinfo.navigation.ReferenceInfoNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReferenceInfoViewModel @Inject constructor(
    private val navigationActions: ReferenceInfoNavigationActions,
    private val dataStorePreferences: DataStorePreferences
) : ViewModel() {

    fun onGotItClick(screenType: InfoScreenType, source: ReferenceInfoSource) {
        viewModelScope.launch {
            when (screenType) {
                is InfoScreenType.HowToPlay -> {
                    if (source == ReferenceInfoSource.ONBOARDING) {
                        navigationActions.navigateToInfo(com.maat.cha.core.navigation.destinations.InfoType.TERMS_OF_USE)
                    } else {
                        navigationActions.navigateBack()
                    }
                }
                is InfoScreenType.TermsOfUse -> {
                    if (source == ReferenceInfoSource.ONBOARDING) {
                        navigationActions.navigateToInfo(com.maat.cha.core.navigation.destinations.InfoType.PRIVACY)
                    } else {
                        navigationActions.navigateBack()
                    }
                }
                is InfoScreenType.Privacy -> {
                    if (source == ReferenceInfoSource.ONBOARDING) {
                        // Save that user has read all info screens
                        dataStorePreferences.setHasReadAllInfo(true)
                        navigationActions.navigateToGame()
                    } else {
                        navigationActions.navigateBack()
                    }
                }
                is InfoScreenType.PrivacyPolicy -> {
                    navigationActions.navigateBack()
                }
                is InfoScreenType.TermsOfUsePolicy -> {
                    navigationActions.navigateBack()
                }
                is InfoScreenType.PrivacyPolicyWebView -> {
                    // This should not be called directly
                    navigationActions.navigateBack()
                }
            }
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            navigationActions.navigateBack()
        }
    }
} 