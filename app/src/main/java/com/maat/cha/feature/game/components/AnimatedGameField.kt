package com.maat.cha.feature.game.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maat.cha.R
import com.maat.cha.feature.game.utils.GameUtils

@Composable
fun AnimatedGameField(
    gameField: List<List<Int>>,
    isSpinning: Boolean,
    currentRound: Int,
    matchedItems: List<List<Int>> = emptyList(),
    modifier: Modifier = Modifier
) {
    val fieldSize = GameUtils.getFieldSizeForRound(currentRound)
    val iconSize = GameUtils.getIconSizeForRound(currentRound)
    
    // Flatten matched items for easier checking
    val matchedPositions = matchedItems.flatten().toSet()
    
    Box(
        modifier = modifier
            .size(width = 340.dp, height = 340.dp),
        contentAlignment = Alignment.Center
    ) {
        // Use the original background
        Image(
            painter = painterResource(id = R.drawable.background_surface_game),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )
        
        // Game field grid
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0 until fieldSize) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (col in 0 until fieldSize) {
                        val position = row * fieldSize + col
                        val isMatched = matchedPositions.contains(position)
                        val iconRes = if (row < gameField.size && col < gameField[row].size) {
                            gameField[row][col]
                        } else {
                            R.drawable.ic_star // Default icon
                        }
                        
                        // Column spinning animation - horizontal scrolling effect
                        val columnOffset by animateFloatAsState(
                            targetValue = if (isSpinning) {
                                // Stagger the spinning - each column stops after the previous one
                                val spinDelay = col * 400L // Increased delay between columns
                                val totalSpinTime = 7000L // Increased total spin time (was 5000L)
                                val columnSpinTime = totalSpinTime - spinDelay
                                if (columnSpinTime > 0) 1440f else 0f // More rotations for longer effect (was 1080f)
                            } else 0f,
                            animationSpec = tween(
                                durationMillis = if (isSpinning) 7000 else 0, // Increased duration (was 5000)
                                delayMillis = if (isSpinning) col * 400 else 0
                            ),
                            label = "column_offset_$col"
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(iconSize.dp)
                                .rotate(columnOffset), // This creates the horizontal scrolling effect
                            contentAlignment = Alignment.Center
                        ) {
                            // Background for matched items - make it larger
                            if (isMatched) {
                                Image(
                                    painter = painterResource(id = R.drawable.highlighted_items_background),
                                    contentDescription = null,
                                    modifier = Modifier.size((iconSize + 8).dp), // Make background larger
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                            
                            Image(
                                painter = painterResource(id = iconRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .size((iconSize - 8).dp)
                                    .padding(4.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }
        }
    }
} 