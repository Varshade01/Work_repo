package com.maat.cha.feature.splash.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.core.data.model.MatchViewResponse
import com.maat.cha.core.network.repository.MatchViewRepository
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
    private val navigationActions: SplashNavigationActions,
    private val matchViewRepository: MatchViewRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    fun onEvent(event: SplashEvents) {
        Log.d("SplashViewModel", "Received event: $event")
        when (event) {
            is SplashEvents.OnDelayComplete -> {
                Log.d("SplashViewModel", "Delay complete, starting API call")
                _state.update { it.copy(isLoading = false) }
                fetchMatchView()
            }

            is SplashEvents.OnRetryApi -> {
                Log.d("SplashViewModel", "Retrying API call")
                fetchMatchView()
            }

            is SplashEvents.OnBannerClick -> {
                Log.d("SplashViewModel", "Banner clicked")
                handleBannerClick()
            }

            is SplashEvents.OnWebViewBackPressed -> {
                Log.d("SplashViewModel", "WebView back pressed")
                handleWebViewBackPress()
            }

            is SplashEvents.OnBannerBackPressed -> {
                Log.d("SplashViewModel", "Banner back pressed")
                handleBannerBackPress()
            }

            is SplashEvents.OnExternalNavigation -> {
                Log.d("SplashViewModel", "External navigation detected")
                _state.update { it.copy(hasNavigatedExternally = true) }
            }

            is SplashEvents.OnReturnFromExternal -> {
                Log.d("SplashViewModel", "Return from external navigation")
                handleReturnFromExternal()
            }

            is SplashEvents.OnWebViewError -> {
                Log.d("SplashViewModel", "WebView error - showing No Internet screen")
                handleWebViewError()
            }
        }
    }

    private fun fetchMatchView() {
        Log.d("SplashViewModel", "Fetching match view")
        _state.update { it.copy(isApiLoading = true, error = null) }
        viewModelScope.launch {
            val result = matchViewRepository.getMatchView()
            result.fold(
                onSuccess = { response ->
                    Log.d("SplashViewModel", "API success: $response")
                    handleMatchViewResponse(response)
                },
                onFailure = { exception ->
                    Log.e("SplashViewModel", "API failure: ${exception.message}", exception)
                    // If API fails, go to main screen (not show No Internet)
                    viewModelScope.launch { navigationActions.navigateToMain() }
                }
            )
        }
    }

    private fun handleMatchViewResponse(response: MatchViewResponse) {
        // Store the original response for external navigation scenarios
        _state.update { it.copy(originalApiResponse = response) }

        // TEMPORARY: Test with Wikipedia URL
        val testUrl = "https://uk.wikipedia.org/"
        
        when {
            !response.launchLink.isNullOrBlank() && !response.matchImg.isNullOrBlank() -> {
                Log.d("SplashViewModel", "Both link and image - open WebView with Wikipedia")
                _state.update {
                    it.copy(
                        isApiLoading = false,
                        showWebView = true,
                        webViewUrl = response.launchLink // Use Wikipedia URL instead of response.launchLink
                    )
                }
            }

            response.launchLink.isNullOrBlank() && !response.matchImg.isNullOrBlank() -> {
                Log.d("SplashViewModel", "Only image - show banner")
                _state.update {
                    it.copy(
                        isApiLoading = false,
                        showBanner = true,
                        bannerUrl = response.matchImg
                    )
                }
            }

            !response.launchLink.isNullOrBlank() && response.matchImg.isNullOrBlank() -> {
                Log.d("SplashViewModel", "Only link - open WebView with Wikipedia")
                _state.update {
                    it.copy(
                        isApiLoading = false,
                        showWebView = true,
                        webViewUrl = response.launchLink // Use Wikipedia URL instead of response.launchLink
                    )
                }
            }

            else -> {
                Log.d("SplashViewModel", "No data - navigating to main")
                viewModelScope.launch { navigationActions.navigateToMain() }
            }
        }
    }

    private fun handleBannerClick() {
        // Banner click always goes to main screen
        // This is only for the case when only image comes (no link)
        viewModelScope.launch { navigationActions.navigateToMain() }
    }

    private fun handleWebViewBackPress() {
        // When exiting WebView, close the app (not go to main)
        viewModelScope.launch { navigationActions.closeApp() }
    }

    private fun handleBannerBackPress() {
        // Banner back press also closes the app
        viewModelScope.launch { navigationActions.closeApp() }
    }

    private fun handleReturnFromExternal() {
        val currentState = _state.value
        if (currentState.originalApiResponse?.matchImg != null) {
            _state.update {
                it.copy(
                    showBanner = true,
                    bannerUrl = currentState.originalApiResponse?.matchImg,
                    showWebView = false
                )
            }
        } else {
            viewModelScope.launch { navigationActions.closeApp() }
        }
    }

    private fun handleWebViewError() {
        // Show No Internet screen only when WebView fails to load
        _state.update {
            it.copy(
                showWebView = false,
                error = "No Internet connection"
            )
        }
    }
} 