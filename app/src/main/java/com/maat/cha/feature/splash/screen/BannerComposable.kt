package com.maat.cha.feature.splash.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.maat.cha.feature.splash.utils.SplashConstants
import kotlinx.coroutines.delay

/**
 * Banner composable for displaying promotional images
 * Shows banner after a delay and handles back button with double-tap exit
 */
@Composable
fun BannerComposable(
    bannerUrl: String,
    onBannerClick: () -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    var lastBackPressTime by remember { mutableLongStateOf(0L) }
    var isVisible by remember { mutableStateOf(false) }

    // Show banner after delay
    LaunchedEffect(Unit) {
        delay(SplashConstants.BANNER_DELAY_MS)
        isVisible = true
    }

    // Handle back button with double-tap exit
    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < SplashConstants.DOUBLE_TAP_TIMEOUT_MS) {
            // Double tap - exit
            onBackPressed()
        } else {
            // Single tap - show toast
            lastBackPressTime = currentTime
            Toast.makeText(context, SplashConstants.TOAST_PRESS_BACK_AGAIN, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isVisible) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(bannerUrl)
                        .build()
                ),
                contentDescription = "Banner",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onBannerClick() },
                contentScale = ContentScale.FillBounds
            )
        } else {
            // Show loading while waiting for banner
            CircularProgressIndicator(
                color = Color(SplashConstants.PRIMARY_COLOR),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
} 