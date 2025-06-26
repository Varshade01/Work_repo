package com.maat.cha.feature.splash.screen

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.maat.cha.R
import com.maat.cha.feature.composable.BackgroundApp
import com.maat.cha.feature.composable.CardInfo
import com.maat.cha.feature.composable.LogoItem
import com.maat.cha.feature.composable.Title
import com.maat.cha.feature.splash.events.SplashEvents
import com.maat.cha.feature.splash.state.SplashUiState
import com.maat.cha.feature.splash.utils.SplashConstants
import com.maat.cha.feature.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

/**
 * Main splash screen composable
 * Handles the initial app loading and API response processing
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel()
) {
    HideSystemBars()

    val state by viewModel.state.collectAsState()

    // Handle splash delay
    LaunchedEffect(key1 = true) {
        delay(SplashConstants.SPLASH_DELAY_MS)
        viewModel.onEvent(SplashEvents.OnSplashDelayComplete)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundApp(
            backgroundRes = R.drawable.background_app,
            modifier = Modifier.matchParentSize()
        )

        // Render UI based on current state
        when (val uiState = state.uiState) {
            is SplashUiState.Initial -> {
                SplashContent()
            }

            is SplashUiState.Loading -> {
                SplashContent()
            }

            is SplashUiState.ApiLoading -> {
                ApiLoadingContent()
            }

            is SplashUiState.Error -> {
                // This error state is now only used for non-WebView errors
                // WebView errors are handled within the WebViewComposable
                ErrorContent(
                    errorMessage = uiState.message,
                    isRetryable = uiState.isRetryable,
                    onRetry = { viewModel.onEvent(SplashEvents.OnRetryApiCall) }
                )
            }

            is SplashUiState.WebView -> {
                WebViewContent(
                    url = uiState.url,
                    onBackPressed = { viewModel.onEvent(SplashEvents.OnWebViewBackPressed) },
                    onExternalNavigation = { viewModel.onEvent(SplashEvents.OnExternalNavigationDetected) },
                    onWebViewError = { 
                        // WebView errors are now handled within the WebViewComposable
                        // This callback is kept for logging purposes
                        viewModel.onEvent(SplashEvents.OnWebViewLoadError) 
                    }
                )
            }

            is SplashUiState.Banner -> {
                BannerContent(
                    bannerUrl = uiState.imageUrl,
                    onBannerClick = { viewModel.onEvent(SplashEvents.OnBannerClicked) },
                    onBackPressed = { viewModel.onEvent(SplashEvents.OnBannerBackPressed) }
                )
            }
        }
    }
}

/**
 * Splash screen content with logo
 */
@Composable
private fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = SplashConstants.BANNER_BOTTOM_PADDING_DP.dp),
        contentAlignment = Alignment.Center
    ) {
        LogoItem(
            logoRes = R.drawable.logo_splash,
            modifier = Modifier
        )
    }
}

/**
 * API loading content with progress indicator
 */
@Composable
private fun ApiLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(SplashConstants.PRIMARY_COLOR)
        )
    }
}

/**
 * Error content with retry functionality
 */
@Composable
private fun ErrorContent(
    errorMessage: String,
    isRetryable: Boolean,
    onRetry: () -> Unit
) {
    ErrorScreen(
        errorMessage = errorMessage,
        onRetry = if (isRetryable) onRetry else null
    )
}

/**
 * WebView content
 */
@Composable
private fun WebViewContent(
    url: String,
    onBackPressed: () -> Unit,
    onExternalNavigation: () -> Unit,
    onWebViewError: () -> Unit
) {
    WebViewComposable(
        url = url,
        onBackPressed = onBackPressed,
        onExternalNavigation = onExternalNavigation,
        onWebViewError = onWebViewError
    )
}

/**
 * Banner content
 */
@Composable
private fun BannerContent(
    bannerUrl: String,
    onBannerClick: () -> Unit,
    onBackPressed: () -> Unit
) {
    BannerComposable(
        bannerUrl = bannerUrl,
        onBannerClick = onBannerClick,
        onBackPressed = onBackPressed
    )
}

/**
 * Hides system bars for full-screen experience
 */
@Composable
private fun HideSystemBars() {
    val view = LocalView.current
    DisposableEffect(view) {
        val activity = view.context as? Activity
        activity?.let {
            WindowCompat.setDecorFitsSystemWindows(it.window, false)
            WindowInsetsControllerCompat(it.window, it.window.decorView).apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        onDispose { }
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    SplashContent()
}