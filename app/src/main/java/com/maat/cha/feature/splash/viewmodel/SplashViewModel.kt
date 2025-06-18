package com.maat.cha.feature.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.feature.splash.events.SplashEvents
import com.maat.cha.feature.splash.navigations.SplashNavigationActions
import com.maat.cha.feature.splash.state.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val navigationActions: SplashNavigationActions
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    fun onEvent(event: SplashEvents) {
        when (event) {
            is SplashEvents.OnDelayComplete -> {
                _state.update { it.copy(isLoading = false) }
                viewModelScope.launch {
                    navigationActions.navigateToMain()
                }
            }
        }
    }
} 