package com.maat.cha.feature.splash.screen

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.maat.cha.R
import com.maat.cha.feature.composable.BackgroundApp
import com.maat.cha.feature.composable.LogoItem

@Composable
fun SplashScreen() {
    HideSystemBars()
    SplashScreenUI()
}

@Composable
fun SplashScreenUI() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundApp(
            backgroundRes = R.drawable.background_app,
            modifier = Modifier.matchParentSize()
        )
        LogoItem(
            logoRes = R.drawable.logo_splash,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 124.dp)
        )
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