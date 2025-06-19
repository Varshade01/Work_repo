package com.maat.cha.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.core.datastore.utils.DataStorePreferences
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.core.navigation.destinations.ReferenceInfoSource
import com.maat.cha.feature.settings.events.SettingsEvents
import com.maat.cha.feature.settings.navigation.SettingsNavigationActions
import com.maat.cha.feature.settings.state.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigationActions: SettingsNavigationActions,
    private val dataStorePreferences: DataStorePreferences
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        // Load settings from DataStore
        viewModelScope.launch {
            dataStorePreferences.musicEnabled.collect { musicEnabled ->
                _state.update { it.copy(musicEnabled = musicEnabled) }
            }
        }
        
        viewModelScope.launch {
            dataStorePreferences.vfxEnabled.collect { vfxEnabled ->
                _state.update { it.copy(vfxEnabled = vfxEnabled) }
            }
        }
    }

    fun onEvent(event: SettingsEvents) {
        when (event) {
            is SettingsEvents.OnMusicToggle -> {
                viewModelScope.launch {
                    dataStorePreferences.setMusicEnabled(event.enabled)
                }
            }
            is SettingsEvents.OnVfxToggle -> {
                viewModelScope.launch {
                    dataStorePreferences.setVfxEnabled(event.enabled)
                }
            }
            is SettingsEvents.OnBackClick -> {
                viewModelScope.launch {
                    navigationActions.navigateBack()
                }
            }
            is SettingsEvents.OnPrivacyPolicyClick -> {
                viewModelScope.launch {
                    navigationActions.navigateToReferenceInfo(InfoType.PRIVACY_POLICY, ReferenceInfoSource.SETTINGS)
                }
            }
            is SettingsEvents.OnTermsOfUseClick -> {
                viewModelScope.launch {
                    navigationActions.navigateToReferenceInfo(InfoType.TERMS_OF_USE_POLICY, ReferenceInfoSource.SETTINGS)
                }
            }
        }
    }
} 