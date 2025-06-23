package com.maat.cha.feature.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun MainButton(
    onClick: () -> Unit,
    fontWeight: FontWeight,
    fontSize: TextUnit,
    btnText: String,
    textColor: Color,
    @DrawableRes backgroundRes: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    buttonHeight: Dp = 56.dp,
    buttonWidth: Dp,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .width(buttonWidth)
            .height(buttonHeight)
            .clip(RoundedCornerShape(34.dp))
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = contentDescription,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = btnText,
                color = if (enabled) textColor else textColor.copy(alpha = 0.5f),
                fontWeight = fontWeight,
                fontSize = fontSize,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(1f, 1f),
                        blurRadius = 3f
                    )
                )
            )
        }
    }
}

