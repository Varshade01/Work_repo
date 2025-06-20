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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.maat.cha.R
import com.maat.cha.feature.composable.BackgroundApp
import com.maat.cha.feature.composable.LogoItem
import com.maat.cha.feature.splash.events.SplashEvents
import com.maat.cha.feature.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel()
) {
    HideSystemBars()

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        delay(2000) // 2 second delay
        viewModel.onEvent(SplashEvents.OnDelayComplete)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundApp(
            backgroundRes = R.drawable.background_app,
            modifier = Modifier.matchParentSize()
        )

        when {
            state.isLoading -> {
                // Show splash screen
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 124.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LogoItem(
                        logoRes = R.drawable.logo_splash,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            state.isApiLoading -> {
                // Show loading indicator
                CircularProgressIndicator(
                    color = Color(0xFFFDB001),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.error != null -> {
                // Show error screen
                ErrorScreen(
                    errorMessage = state.error!!,
                    onRetry = { viewModel.onEvent(SplashEvents.OnRetryApi) }
                )
            }

            state.showWebView && state.webViewUrl != null -> {
                // Show WebView
                WebViewComposable(
                    url = state.webViewUrl!!,
                    onBackPressed = { viewModel.onEvent(SplashEvents.OnWebViewBackPressed) },
                    onExternalNavigation = { viewModel.onEvent(SplashEvents.OnExternalNavigation) },
                    onWebViewError = { viewModel.onEvent(SplashEvents.OnWebViewError) }
                )
            }

            state.showBanner && state.bannerUrl != null -> {
                // Show banner
                BannerComposable(
                    bannerUrl = state.bannerUrl!!,
                    onBannerClick = { viewModel.onEvent(SplashEvents.OnBannerClick) },
                    onBackPressed = { viewModel.onEvent(SplashEvents.OnBannerBackPressed) }
                )
            }

            else -> {
                // Show splash screen (fallback)
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 124.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LogoItem(
                        logoRes = R.drawable.logo_splash,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun HideSystemBars() {
    val window = (LocalView.current.context as? Activity)?.window
    DisposableEffect(window) {
        window?.let {
            val controller = WindowInsetsControllerCompat(it, it.decorView)
            WindowCompat.setDecorFitsSystemWindows(it, false)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        onDispose { }
    }
}

@Preview
@Composable
fun PreviewSplashScreen() {
    SplashScreen()
}