package com.maat.cha.feature.appinfo.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maat.cha.R
import com.maat.cha.core.navigation.destinations.ReferenceInfoSource
import com.maat.cha.feature.appinfo.model.InfoScreenType
import com.maat.cha.feature.appinfo.viewmodel.ReferenceInfoViewModel
import com.maat.cha.feature.composable.BackgroundApp
import com.maat.cha.feature.composable.CircularIconButton
import com.maat.cha.feature.composable.MainButton
import com.maat.cha.feature.composable.Title

@Composable
fun ReferenceInfoScreen(
    screenType: InfoScreenType,
    source: ReferenceInfoSource = ReferenceInfoSource.ONBOARDING,
    viewModel: ReferenceInfoViewModel = hiltViewModel()
) {
    ReferenceInfoScreenUI(
        screenType = screenType,
        onMainButtonClick = { viewModel.onGotItClick(screenType, source) },
        onBackClick = { viewModel.onBackClick() }
    )
}

@Composable
fun ReferenceInfoScreenUI(
    screenType: InfoScreenType,
    onMainButtonClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val title = stringResource(screenType.titleRes)
    val content = stringResource(screenType.contentRes)
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundApp(
            backgroundRes = R.drawable.background_app,
            modifier = Modifier.matchParentSize()
        )

        CircularIconButton(
            onClick = onBackClick,
            iconRes = R.drawable.ic_btn_previous,
            contentDescription = "btn previous",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .align(Alignment.TopStart),
            btnText = "",
            textColor = Color.Yellow,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            contentScale = ContentScale.Crop,
            backgroundRes = null,
            iconSize = 56,
            sizeItem = 56,
        )

        Title(
            text = title,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 32.dp, vertical = 16.dp),
            textColor = Color.White,
            borderColor = Color.White,
            icon = null,
            widthItem = 240.dp,
        )

        // Custom card for reference info with proper scrolling
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(360.dp)
                .padding(horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Scrollable content area
                    Text(
                        text = content,
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    MainButton(
                        onClick = onMainButtonClick,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        btnText = stringResource(screenType.mainButtonTextRes),
                        textColor = Color.White,
                        backgroundRes = R.drawable.background_btn,
                        contentDescription = "Got it button",
                        buttonWidth = 300.dp,
                        buttonHeight = 56.dp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewReferenceHowToPlay() {
    ReferenceInfoScreenUI(screenType = InfoScreenType.HowToPlay)
} 