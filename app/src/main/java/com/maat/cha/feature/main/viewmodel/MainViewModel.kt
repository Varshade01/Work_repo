package com.maat.cha.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.core.datastore.utils.DataStorePreferences
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.core.navigation.destinations.ReferenceInfoSource
import com.maat.cha.feature.main.events.MainEvents
import com.maat.cha.feature.main.navigation.MainNavigationActions
import com.maat.cha.feature.main.state.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigationActions: MainNavigationActions,
    private val dataStorePreferences: DataStorePreferences
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            dataStorePreferences.hasReadAllInfo.collect { hasReadAllInfo ->
                _state.update { it.copy(hasReadAllInfo = hasReadAllInfo) }
            }
        }
        
        viewModelScope.launch {
            dataStorePreferences.totalCoins.collect { totalCoins ->
                _state.update { it.copy(totalCoins = totalCoins) }
            }
        }
    }

    fun onEvent(event: MainEvents) {
        when (event) {
            is MainEvents.OnBackClick -> viewModelScope.launch {
                navigationActions.minimizeApp()
            }

            is MainEvents.OnSettingsClick -> viewModelScope.launch {
                navigationActions.navigateToSettings()
            }

            is MainEvents.OnPlayClick -> viewModelScope.launch {
                if (state.value.hasReadAllInfo) {
                    navigationActions.navigateToGame()
                } else {
                    navigationActions.navigateToInfo(InfoType.HOW_TO_PLAY)
                }
            }

            is MainEvents.OnHowToPlayClick -> viewModelScope.launch {
                navigationActions.navigateToReferenceInfo(InfoType.HOW_TO_PLAY, ReferenceInfoSource.MAIN)
            }
        }
    }
}