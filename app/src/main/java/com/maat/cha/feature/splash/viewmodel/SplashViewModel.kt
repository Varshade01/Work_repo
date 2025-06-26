package com.maat.cha.feature.splash.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.core.data.model.MatchViewResponse
import com.maat.cha.core.network.repository.MatchViewRepository
import com.maat.cha.feature.splash.events.SplashEvents
import com.maat.cha.feature.splash.navigations.SplashNavigationActions
import com.maat.cha.feature.splash.state.SplashError
import com.maat.cha.feature.splash.state.SplashState
import com.maat.cha.feature.splash.state.SplashUiState
import com.maat.cha.feature.splash.utils.SplashConstants
import com.maat.cha.feature.splash.utils.SplashUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the splash screen feature
 * Handles API calls, state management, and user interactions
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val navigationActions: SplashNavigationActions,
    private val matchViewRepository: MatchViewRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    /**
     * Handles all events from the UI
     */
    fun onEvent(event: SplashEvents) {
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "Received event: $event")
        
        when (event) {
            is SplashEvents.OnSplashDelayComplete -> {
                handleSplashDelayComplete()
            }
            is SplashEvents.OnRetryApiCall -> {
                handleRetryApiCall()
            }
            is SplashEvents.OnBannerClicked -> {
                handleBannerClick()
            }
            is SplashEvents.OnWebViewBackPressed -> {
                handleWebViewBackPress()
            }
            is SplashEvents.OnBannerBackPressed -> {
                handleBannerBackPress()
            }
            is SplashEvents.OnExternalNavigationDetected -> {
                handleExternalNavigation()
            }
            is SplashEvents.OnReturnFromExternalNavigation -> {
                handleReturnFromExternal()
            }
            is SplashEvents.OnWebViewLoadError -> {
                handleWebViewError()
            }
            is SplashEvents.OnApiResponseReceived -> {
                handleApiResponse(event.response)
            }
            is SplashEvents.OnApiError -> {
                handleApiError(event.error)
            }
        }
    }

    /**
     * Handles splash delay completion
     */
    private fun handleSplashDelayComplete() {
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "Splash delay complete, starting API call")
        updateUiState(SplashUiState.ApiLoading)
        fetchMatchView()
    }

    /**
     * Handles API retry
     */
    private fun handleRetryApiCall() {
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "Retrying API call")
        updateUiState(SplashUiState.ApiLoading)
        fetchMatchView()
    }

    /**
     * Handles banner click
     */
    private fun handleBannerClick() {
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "Banner clicked - navigating to main")
        navigateToMain()
    }

    /**
     * Handles WebView back press
     */
    private fun handleWebViewBackPress() {
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "WebView back pressed - closing app")
        closeApp()
    }

    /**
     * Handles banner back press
     */
    private fun handleBannerBackPress() {
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "Banner back pressed - closing app")
        closeApp()
    }

    /**
     * Handles external navigation detection
     */
    private fun handleExternalNavigation() {
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "External navigation detected")
        _state.update { it.copy(hasNavigatedExternally = true) }
    }

    /**
     * Handles return from external navigation
     */
    private fun handleReturnFromExternal() {
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "Return from external navigation")
        val currentState = _state.value
        
        if (currentState.originalApiResponse?.matchImg != null) {
            updateUiState(SplashUiState.Banner(imageUrl = currentState.originalApiResponse.matchImg))
        } else {
            closeApp()
        }
    }

    /**
     * Handles WebView load error - shows error screen only for WebView
     */
    private fun handleWebViewError() {
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "WebView load error - showing error screen")
        updateUiState(
            SplashUiState.Error(
                message = SplashError.WebViewLoadError.toUserMessage(),
                isRetryable = SplashError.WebViewLoadError.isRetryable()
            )
        )
    }

    /**
     * Handles API response
     */
    private fun handleApiResponse(response: MatchViewResponse) {
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "API response received: $response")
        
        // Store original response for external navigation scenarios
        _state.update { it.copy(originalApiResponse = response) }
        
        // Determine UI state based on response
        val uiState = SplashUtils.determineUiState(response)
        
        when (uiState) {
            is SplashUiState.WebView -> {
                Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "Showing WebView with URL: ${uiState.url}")
                updateUiState(uiState)
            }
            is SplashUiState.Banner -> {
                Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "Showing banner with image: ${uiState.imageUrl}")
                updateUiState(uiState)
            }
            is SplashUiState.Error -> {
                Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "No valid content - navigating to main")
                navigateToMain()
            }
            else -> {
                Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "Unexpected UI state - navigating to main")
                navigateToMain()
            }
        }
    }

    /**
     * Handles API error - never block navigation, always proceed to main app
     */
    private fun handleApiError(error: SplashError) {
        Log.e(SplashConstants.TAG_SPLASH_VIEWMODEL, "API error: ${error.toUserMessage()}")
        
        // Always navigate to main app regardless of API error
        // This ensures users can always access the app even without internet
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "API error occurred - navigating to main app")
        navigateToMain()
    }

    /**
     * Fetches match view data from API
     */
    private fun fetchMatchView() {
        Log.d(SplashConstants.TAG_SPLASH_VIEWMODEL, "Fetching match view data")
        
        viewModelScope.launch {
            try {
                val result = matchViewRepository.getMatchView()
                result.fold(
                    onSuccess = { response ->
                        onEvent(SplashEvents.OnApiResponseReceived(response))
                    },
                    onFailure = { exception ->
                        val error = SplashUtils.mapExceptionToError(exception)
                        onEvent(SplashEvents.OnApiError(error))
                    }
                )
            } catch (exception: Exception) {
                Log.e(SplashConstants.TAG_SPLASH_VIEWMODEL, "Unexpected error in fetchMatchView", exception)
                val error = SplashUtils.mapExceptionToError(exception)
                onEvent(SplashEvents.OnApiError(error))
            }
        }
    }

    /**
     * Updates the UI state
     */
    private fun updateUiState(uiState: SplashUiState) {
        _state.update { it.copy(uiState = uiState) }
    }

    /**
     * Navigates to main screen
     */
    private fun navigateToMain() {
                viewModelScope.launch {
            try {
                    navigationActions.navigateToMain()
            } catch (exception: Exception) {
                Log.e(SplashConstants.TAG_SPLASH_VIEWMODEL, "Error navigating to main", exception)
            }
        }
    }

    /**
     * Closes the app
     */
    private fun closeApp() {
        viewModelScope.launch {
            try {
                navigationActions.closeApp()
            } catch (exception: Exception) {
                Log.e(SplashConstants.TAG_SPLASH_VIEWMODEL, "Error closing app", exception)
            }
        }
    }
} 