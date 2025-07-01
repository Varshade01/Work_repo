package com.maat.cha.feature.game.model

import androidx.compose.ui.geometry.Offset

data class FruitItem(
    val id: Int,
    val type: FruitType,
    val row: Int,
    val col: Int,
    val isMatched: Boolean = false,
    val isAnimating: Boolean = false,
    val animationOffset: Offset = Offset.Zero,
    val isHighlighted: Boolean = false
) {
    val drawableRes: Int get() = type.drawableRes
    val color: Int get() = type.color
    val displayName: String get() = type.displayName
    
    fun copyWithPosition(newRow: Int, newCol: Int): FruitItem = copy(row = newRow, col = newCol)
    fun copyWithMatch(isMatched: Boolean): FruitItem = copy(isMatched = isMatched)
    fun copyWithAnimation(isAnimating: Boolean, offset: Offset = Offset.Zero): FruitItem = 
        copy(isAnimating = isAnimating, animationOffset = offset)
    fun copyWithHighlight(isHighlighted: Boolean): FruitItem = copy(isHighlighted = isHighlighted)
} 