package com.maat.cha.feature.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
    Box(
        modifier = modifier
            .width(360.dp)
            .height(640.dp)
    ) {
        // Картка з контентом
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = if (centerContent) Alignment.Center else Alignment.TopStart
            ) {
                Text(
                    text = textContent,
                    fontSize = fontSizeTextContent,
                    color = Color.Black,
                    modifier = Modifier.padding(24.dp),
                    textAlign = if (centerContent) TextAlign.Center else TextAlign.Start
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (showPrivacyPolicyLink && onPrivacyPolicyClick != null) {
                Text(
                    text = stringResource(R.string.privacy_policy),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Black,
                    modifier = Modifier
                        .clickable { onPrivacyPolicyClick() }
                )
                Spacer(modifier = Modifier.height(48.dp))
            }


            MainButton(
                onClick = onMainButtonClick,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                btnText = mainButtonText,
                textColor = Color.White,
                backgroundRes = R.drawable.background_btn,
                contentDescription = null,
                buttonWidth = 300.dp,
                buttonHeight = 56.dp
            )

            if (!bottomTitleText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = bottomTitleText,
                    fontSize = fontSizeBottomTitleText,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.clickable { onBottomTextClick() }
                )
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