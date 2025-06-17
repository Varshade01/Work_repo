package com.maat.cha.feature.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maat.cha.R

@Composable
fun Title(
    text: String,
    icon: Int?,
    modifier: Modifier = Modifier,
    widthItem: Dp,
    textColor: Color,
    borderColor: Color,
    fontSize: TextUnit = 24.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    cornerRadius: Dp = 28.dp
) {
    Row(
        modifier = modifier
            .width(widthItem)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = "icon img",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(42.dp)
            )
        }

        Text(
            modifier = Modifier.padding(12.dp),
            text = text,
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    }
}

@Composable
@Preview
fun TitleUIPreview() {
    Title(
        text = "500000",
        modifier = Modifier,
        textColor = Color.White,
        borderColor = Color.White,
        widthItem = 180.dp,
        icon = R.drawable.ic_coin
    )
}
