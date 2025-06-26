package com.maat.cha.feature.appinfo.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maat.cha.R
import com.maat.cha.feature.appinfo.model.InfoScreenType
import com.maat.cha.feature.appinfo.viewmodel.InfoViewModel
import com.maat.cha.feature.composable.BackgroundApp
import com.maat.cha.feature.composable.CardInfo
import com.maat.cha.feature.composable.CircularIconButton
import com.maat.cha.feature.composable.Title
import com.maat.cha.feature.splash.screen.WebViewComposable

@Composable
fun InfoScreen(
    screenType: InfoScreenType,
    viewModel: InfoViewModel = hiltViewModel()
) {
    InfoScreenUI(
        screenType = screenType,
        onMainButtonClick = { viewModel.onMainButtonClick(screenType) },
        onBottomTextClick = { viewModel.onBottomTextClick() },
        onBackClick = { viewModel.onBottomTextClick() }
    )
}

@Composable
fun InfoScreenUI(
    screenType: InfoScreenType,
    onMainButtonClick: () -> Unit = {},
    onBottomTextClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var isPrivacyExpanded by remember { mutableStateOf(false) }
    var showPrivacyWebView by remember { mutableStateOf(false) }
    var showPrivacyFallback by remember { mutableStateOf(false) }
    var hasReadPrivacyPolicy by remember { mutableStateOf(false) }

    val isPrivacy = screenType is InfoScreenType.Privacy
    val isPrivacyExpandedState = isPrivacy && isPrivacyExpanded

    // Handle Privacy Policy WebView
    if (showPrivacyWebView) {
        WebViewComposable(
            url = InfoScreenType.PrivacyPolicyWebView.PRIVACY_POLICY_URL,
            onBackPressed = { 
                showPrivacyWebView = false
                // Mark that user has read the privacy policy
                hasReadPrivacyPolicy = true
            },
            onExternalNavigation = { /* Handle external navigation if needed */ },
            onWebViewError = { 
                // If WebView fails to load, show fallback text
                showPrivacyWebView = false
                showPrivacyFallback = true
                hasReadPrivacyPolicy = true // Consider fallback as "read"
            }
        )
        return
    }

    val title = stringResource(screenType.titleRes)
    val content = when {
        isPrivacyExpandedState || showPrivacyFallback ->
            stringResource(R.string.privacy_policy_content)
        else -> stringResource(screenType.contentRes)
    }

    val mainButtonText = when {
        isPrivacyExpandedState || showPrivacyFallback ->
            stringResource(R.string.agree)
        else -> stringResource(screenType.mainButtonTextRes)
    }

    val bottomText = when {
        isPrivacyExpandedState || showPrivacyFallback ->
            stringResource(R.string.text_reject)
        isPrivacy && !isPrivacyExpanded && !showPrivacyFallback ->
            null
        screenType.bottomBtnTextRes != null ->
            stringResource(screenType.bottomBtnTextRes)
        else -> null
    }

    val handleBottomTextClick = when {
        isPrivacyExpandedState || showPrivacyFallback -> ({ 
            isPrivacyExpanded = false
            showPrivacyFallback = false
            hasReadPrivacyPolicy = false // Reset when going back
        })
        else -> onBottomTextClick
    }

    val handlePrivacyPolicyClick = when {
        isPrivacy && !isPrivacyExpanded && !showPrivacyFallback -> {
            { showPrivacyWebView = true }
        }
        else -> null
    }

    // For privacy screen, only allow accept if user has read the policy
    val canAcceptPrivacy = when {
        isPrivacy && !isPrivacyExpanded && !showPrivacyFallback -> hasReadPrivacyPolicy
        else -> true
    }

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

        CardInfo(
            textContent = content,
            bottomTitleText = bottomText,
            fontSizeTextContent = 18.sp,
            fontSizeBottomTitleText = 32.sp,
            modifier = Modifier.align(Alignment.Center),
            mainButtonText = mainButtonText,
            onMainButtonClick = if (canAcceptPrivacy) onMainButtonClick else { {} },
            onBottomTextClick = handleBottomTextClick,
            centerContent = isPrivacy && !isPrivacyExpanded && !showPrivacyFallback,
            showPrivacyPolicyLink = isPrivacy && !isPrivacyExpanded && !showPrivacyFallback,
            onPrivacyPolicyClick = handlePrivacyPolicyClick,
            mainButtonEnabled = canAcceptPrivacy
        )
    }
}


@Preview
@Composable
fun PreviewHowToPlay() {
    InfoScreenUI(screenType = InfoScreenType.HowToPlay)
}

@Preview
@Composable
fun PreviewTerms() {
    InfoScreenUI(screenType = InfoScreenType.TermsOfUse)
}

@Preview
@Composable
fun PreviewPrivacy() {
    InfoScreenUI(screenType = InfoScreenType.Privacy)
}

