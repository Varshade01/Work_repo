package com.maat.cha.feature.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun BackgroundApp(
    @DrawableRes backgroundRes: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = backgroundRes),
        contentDescription = "Background App",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

