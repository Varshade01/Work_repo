package com.maat.cha.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.feature.settings.navigation.SettingsNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigationActions: SettingsNavigationActions
) : ViewModel() {

    fun onBackClick() {
        viewModelScope.launch {
            navigationActions.navigateBack()
        }
    }
} 