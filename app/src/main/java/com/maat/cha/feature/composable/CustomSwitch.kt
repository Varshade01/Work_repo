package com.maat.cha.feature.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maat.cha.ui.theme.Orange_light
import com.maat.cha.ui.theme.Pink

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 120.dp,
    height: Dp = 42.dp,
    checkedTrackColor: Color = Pink,
    uncheckedTrackColor: Color = Orange_light,
    checkedThumbColor: Color = Orange_light,
    uncheckedThumbColor: Color = Pink,
    padding: Dp = 4.dp,
    borderColor: Color = Color.White,
    borderWidth: Dp = 1.dp
) {
    val thumbSize = height - padding * 2
    val offset by animateDpAsState(
        targetValue = if (checked) (width - thumbSize - padding) else padding,
        animationSpec = tween(durationMillis = 200)
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(height))
            .width(width)
            .height(height)
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.Switch
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(height)
                )
                .background(
                    color = if (checked) checkedTrackColor else uncheckedTrackColor,
                    shape = RoundedCornerShape(height)
                )
        )
        Box(
            modifier = Modifier
                .offset(x = offset)
                .size(thumbSize)
                .background(
                    color = if (checked) checkedThumbColor else uncheckedThumbColor,
                    shape = CircleShape
                )
        )
    }
}