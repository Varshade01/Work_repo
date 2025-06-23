package com.maat.cha.feature.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maat.cha.R

@Composable
fun CardInfo(
    textContent: String,
    mainButtonText: String,
    bottomTitleText: String?,
    fontSizeTextContent: TextUnit,
    fontSizeBottomTitleText: TextUnit,
    modifier: Modifier = Modifier,
    onMainButtonClick: () -> Unit = {},
    onBottomTextClick: () -> Unit = {},
    onPrivacyPolicyClick: (() -> Unit)? = null,
    showPrivacyPolicyLink: Boolean = false,
    centerContent: Boolean = false
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .heightIn(min = 320.dp, max = 480.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                    contentAlignment = if (centerContent) Alignment.Center else Alignment.TopStart
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = if (centerContent) Alignment.CenterHorizontally else Alignment.Start
                    ) {
                        Text(
                            text = textContent,
                            fontSize = fontSizeTextContent,
                            color = Color.Black,
                            textAlign = if (centerContent) TextAlign.Center else TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (showPrivacyPolicyLink && onPrivacyPolicyClick != null) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = stringResource(R.string.privacy_policy),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline,
                                color = Color.Black,
                                modifier = Modifier
                                    .clickable { onPrivacyPolicyClick() }
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MainButton(
                        onClick = onMainButtonClick,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        btnText = mainButtonText,
                        textColor = Color.White,
                        backgroundRes = R.drawable.background_btn,
                        contentDescription = null,
                        buttonWidth = 300.dp,
                        buttonHeight = 56.dp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    if (!bottomTitleText.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = bottomTitleText,
                            fontSize = fontSizeBottomTitleText,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .clickable { onBottomTextClick() }
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun CardAppInfoPreview() {
    CardInfo(
        textContent = stringResource(R.string.how_to_play),
        bottomTitleText = "", fontSizeTextContent = 18.sp, fontSizeBottomTitleText = 32.sp,
        mainButtonText = stringResource(R.string.got_it)
    )
}