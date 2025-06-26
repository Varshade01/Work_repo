package com.maat.cha.feature.splash.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maat.cha.feature.splash.utils.SplashConstants

/**
 * Error screen composable
 * Displays error messages with optional retry functionality
 */
@Composable
fun ErrorScreen(
    errorMessage: String,
    onRetry: (() -> Unit)?
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SplashConstants.ERROR_SCREEN_PADDING_DP.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "No Internet Connection",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = errorMessage,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            // Show reload button only if onRetry is provided
            onRetry?.let { retryAction ->
                Button(
                    onClick = retryAction,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(SplashConstants.ERROR_BUTTON_COLOR)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SplashConstants.ERROR_BUTTON_HEIGHT_DP.dp)
                ) {
                    Text(
                        text = "Reload",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
} 