package com.maat.cha.feature.game.model

import com.maat.cha.R

enum class FruitType(
    val drawableRes: Int,
    val displayName: String,
    val color: Int
) {
    CHERRY(
        drawableRes = R.drawable.ic_chery,
        displayName = "Cherry",
        color = 0xFFE91E63.toInt()
    ),
    LEMON(
        drawableRes = R.drawable.ic_lemon,
        displayName = "Lemon",
        color = 0xFFFFEB3B.toInt()
    ),
    APPLE(
        drawableRes = R.drawable.ic_raspberry,
        displayName = "Apple",
        color = 0xFFF44336.toInt()
    ),
    ORANGE(
        drawableRes = R.drawable.ic_orange,
        displayName = "Orange",
        color = 0xFFFF9800.toInt()
    ),
    GRAPE(
        drawableRes = R.drawable.ic_plum,
        displayName = "Grape",
        color = 0xFF9C27B0.toInt()
    ),
    STRAWBERRY(
        drawableRes = R.drawable.ic_star,
        displayName = "Strawberry",
        color = 0xFFE91E63.toInt()
    ),
    WATERMELON(
        drawableRes = R.drawable.ic_watermelon,
        displayName = "Watermelon",
        color = 0xFF4CAF50.toInt()
    ),
    BANANA(
        drawableRes = R.drawable.ic_bananas,
        displayName = "Banana",
        color = 0xFFFFEB3B.toInt()
    );

    companion object {
        fun getRandom(): FruitType = FruitType.entries.toTypedArray().random()
    }
} 