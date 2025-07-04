package com.maat.cha.feature.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun LogoItem(
    @DrawableRes logoRes: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = logoRes),
        contentDescription = "Logo img",
        modifier = modifier
    )
}