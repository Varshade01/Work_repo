package com.maat.cha.feature.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maat.cha.R
import com.maat.cha.ui.theme.Orange_light

@Composable
fun CollectionItemsSection(
    modifier: Modifier = Modifier,
    totalRounds: Int = 4,
    collectedFruitsPerRound: Map<Int, List<Int>> = emptyMap(),
    selectedColor: Color = Orange_light,
    unselectedColor: Color = Color.Transparent,
    borderColor: Color = Color.White,
    cornerRadiusSelection: Dp = 16.dp,
    boxWidth: Dp = 92.dp,
    boxHeight: Dp = 132.dp,
    backgroundColor: Color = Color(0xC6FF6AC3),
    iconSize: Dp = 32.dp,
    cornerRadiusFruits: Dp = 12.dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Selection row showing round numbers and collected counts
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (round in 1..totalRounds) {
                val collectedFruits = collectedFruitsPerRound[round] ?: emptyList()
                val collectedCount = collectedFruits.size / 3 // Count unique fruit types (3 icons per fruit)
                Box(
                    modifier = Modifier
                        .size(width = 76.dp, height = 32.dp)
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(cornerRadiusSelection)
                        )
                        .background(
                            color = if (collectedCount > 0) selectedColor else unselectedColor,
                            shape = RoundedCornerShape(cornerRadiusSelection)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = collectedCount.toString(),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Fruit columns showing collected fruits for each round
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            for (round in 1..totalRounds) {
                val collectedFruits = collectedFruitsPerRound[round] ?: emptyList()
                Box(
                    modifier = Modifier
                        .size(width = boxWidth, height = boxHeight)
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(cornerRadiusFruits)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (collectedFruits.isNotEmpty()) {
                            // Show collected fruits (always show 3 slots)
                            repeat(3) { index ->
                                if (index < collectedFruits.size) {
                                    // Show collected fruit
                                    Image(
                                        painter = painterResource(id = collectedFruits[index]),
                                        contentDescription = null,
                                        modifier = Modifier.size(iconSize),
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    // Show empty slot
                                    Box(modifier = Modifier.size(iconSize))
                                }
                            }
                        } else {
                            // Show empty slots
                            repeat(3) {
                                Box(modifier = Modifier.size(iconSize))
                            }
                        }
                    }
                }
            }
        }
    }
} 