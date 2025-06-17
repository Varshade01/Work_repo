package com.maat.cha.feature.main.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maat.cha.R
import com.maat.cha.feature.composable.BackgroundApp
import com.maat.cha.feature.composable.CircularIconButton
import com.maat.cha.feature.composable.LogoItem
import com.maat.cha.feature.composable.MainButton
import com.maat.cha.ui.theme.Orange

@Composable
fun MainScreen() {
    MainScreenUI()
}

@Composable
fun MainScreenUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundApp(
            backgroundRes = R.drawable.background_app,
            modifier = Modifier.matchParentSize()
        )
        LogoItem(
            logoRes = R.drawable.logo_main,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 124.dp)
        )

        CircularIconButton(
            onClick = { /* TODO */ },
            iconRes = R.drawable.ic_btn_previous,
            contentDescription = "btn previous",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            btnText = "",
            textColor = Color.Yellow,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            contentScale = ContentScale.Crop,
            backgroundRes = null,
            iconSize = 56,
            sizeItem = 56,
            )
        CircularIconButton(
            onClick = { /* TODO */ },
            iconRes = R.drawable.ic_btn_settings,
            contentDescription = "button settings",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            btnText = null,
            textColor = Color.Yellow,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            contentScale = ContentScale.Crop,
            backgroundRes = R.drawable.ic_btn_circular_background,
            iconSize = 48,
            sizeItem = 56,

            )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 32.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainButton(
                onClick = { /* TODO */ },
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                btnText = "Play",
                textColor = Color.White,
                backgroundRes = R.drawable.background_btn,
                modifier = Modifier.padding(end = 12.dp),
                contentDescription = "Main screen btn",
                buttonWidth = 260.dp
            )
            CircularIconButton(
                onClick = { /* TODO */ },
                iconRes = null,
                contentDescription = "How to play",
                modifier = Modifier,
                btnText = "?",
                textColor = Orange,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                contentScale = ContentScale.Crop,
                backgroundRes = R.drawable.ic_btn_circular_background,
                iconSize = 48,
                sizeItem = 56,
            )
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen()
}