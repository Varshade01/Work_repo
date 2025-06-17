package com.maat.cha.feature.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun CircularIconButton(
    onClick: () -> Unit,
    @DrawableRes backgroundRes: Int? = null,
    @DrawableRes iconRes: Int? = null,
    iconSize: Int,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    btnText: String? = null,
    textColor: Color = Color.White,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    sizeItem: Int,
    contentScale: ContentScale = ContentScale.Crop
) {
    Box(
        modifier = modifier
            .size(sizeItem.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        if (backgroundRes != null) {
            Image(
                painter = painterResource(id = backgroundRes),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = contentScale
            )
        }

        if (iconRes != null) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(iconSize.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit
            )
        } else if (!btnText.isNullOrEmpty()) {
            Text(
                text = btnText,
                color = textColor,
                fontSize = fontSize,
                fontWeight = fontWeight,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}