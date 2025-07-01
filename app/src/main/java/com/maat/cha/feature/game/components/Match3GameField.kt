package com.maat.cha.feature.game.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maat.cha.R
import com.maat.cha.feature.game.events.GameEvents
import com.maat.cha.feature.game.model.FruitItem
import com.maat.cha.feature.game.model.GameBoard
import com.maat.cha.feature.game.utils.GameUtils

@Composable
fun Match3GameField(
    gameBoard: GameBoard,
    isProcessingMove: Boolean,
    onFruitSelected: (FruitItem) -> Unit,
    onFruitMoved: (FruitItem, Int, Int) -> Unit,
    onFruitDeselected: () -> Unit,
    onMoveCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fieldSize = gameBoard.size
    val iconSize = GameUtils.getIconSizeForRound(1) // Use round 1 size for now
    
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
                        val fruit = gameBoard.fruits[row][col]
                        val isSelected = gameBoard.selectedFruit?.id == fruit.id
                        val isHighlighted = fruit.isHighlighted
                        
                        Match3FruitItem(
                            fruit = fruit,
                            iconSize = iconSize,
                            isSelected = isSelected,
                            isHighlighted = isHighlighted,
                            isProcessingMove = isProcessingMove,
                            onFruitSelected = onFruitSelected,
                            onFruitMoved = onFruitMoved,
                            onFruitDeselected = onFruitDeselected,
                            onMoveCompleted = onMoveCompleted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Match3FruitItem(
    fruit: FruitItem,
    iconSize: Float,
    isSelected: Boolean,
    isHighlighted: Boolean,
    isProcessingMove: Boolean,
    onFruitSelected: (FruitItem) -> Unit,
    onFruitMoved: (FruitItem, Int, Int) -> Unit,
    onFruitDeselected: () -> Unit,
    onMoveCompleted: () -> Unit
) {
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    
    // Animation for selected state
    val elevation by animateFloatAsState(
        targetValue = if (isSelected) 8f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "elevation"
    )
    
    // Animation for drag offset
    val animatedDragOffset by animateFloatAsState(
        targetValue = if (isDragging) dragOffset.x else 0f,
        animationSpec = tween(durationMillis = if (isDragging) 0 else 300),
        label = "drag_offset"
    )
    
    Box(
        modifier = Modifier
            .size(iconSize.dp)
            .graphicsLayer {
                translationX = animatedDragOffset
                translationY = if (isSelected) -elevation else 0f
                shadowElevation = elevation
            }
            .pointerInput(fruit.id) {
                detectDragGestures(
                    onDragStart = {
                        if (!isProcessingMove) {
                            isDragging = true
                            onFruitSelected(fruit)
                        }
                    },
                    onDragEnd = {
                        isDragging = false
                        dragOffset = Offset.Zero
                        if (!isProcessingMove) {
                            onFruitDeselected()
                        }
                        onMoveCompleted()
                    },
                    onDragCancel = {
                        isDragging = false
                        dragOffset = Offset.Zero
                        if (!isProcessingMove) {
                            onFruitDeselected()
                        }
                    },
                    onDrag = { change, dragAmount ->
                        if (!isProcessingMove) {
                            change.consume()
                            dragOffset += dragAmount
                            
                            // Check if drag distance is sufficient to trigger a move
                            val dragDistance = dragOffset.getDistance()
                            val threshold = iconSize / 2 // Half the icon size
                            
                            if (dragDistance > threshold) {
                                // Determine direction and target position
                                val direction = when {
                                    kotlin.math.abs(dragOffset.x) > kotlin.math.abs(dragOffset.y) -> {
                                        if (dragOffset.x > 0) "right" else "left"
                                    }
                                    else -> {
                                        if (dragOffset.y > 0) "down" else "up"
                                    }
                                }
                                
                                val targetRow = when (direction) {
                                    "up" -> fruit.row - 1
                                    "down" -> fruit.row + 1
                                    else -> fruit.row
                                }
                                
                                val targetCol = when (direction) {
                                    "left" -> fruit.col - 1
                                    "right" -> fruit.col + 1
                                    else -> fruit.col
                                }
                                
                                // Trigger the move
                                onFruitMoved(fruit, targetRow, targetCol)
                                isDragging = false
                                dragOffset = Offset.Zero
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Background for highlighted items
        if (isHighlighted) {
            Image(
                painter = painterResource(id = R.drawable.highlighted_items_background),
                contentDescription = null,
                modifier = Modifier.size((iconSize + 8).dp),
                contentScale = ContentScale.FillBounds
            )
        }
        
        // Fruit icon
        Image(
            painter = painterResource(id = fruit.drawableRes),
            contentDescription = fruit.displayName,
            modifier = Modifier
                .size((iconSize - 8).dp)
                .padding(4.dp),
            contentScale = ContentScale.Fit
        )
        
        // Selection indicator
        if (isSelected && !isDragging) {
            Box(
                modifier = Modifier
                    .size((iconSize + 4).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }
    }
} 