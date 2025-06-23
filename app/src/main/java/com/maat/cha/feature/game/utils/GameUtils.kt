package com.maat.cha.feature.game.utils

import com.maat.cha.R

object GameUtils {
    
    val FRUIT_ICONS = listOf(
        R.drawable.ic_raspberry,
        R.drawable.ic_plum,
        R.drawable.ic_lemon,
        R.drawable.ic_star,
        R.drawable.ic_watermelon,
        R.drawable.ic_bananas,
        R.drawable.ic_chery,
        R.drawable.ic_orange
    )
    
    fun getFieldSizeForRound(round: Int): Int {
        return when (round) {
            1 -> 4
            2 -> 5
            3 -> 6
            4 -> 6
            else -> 4
        }
    }
    
    fun generateGameField(round: Int): List<List<Int>> {
        val size = getFieldSizeForRound(round)
        return List(size) { row ->
            List(size) { col ->
                FRUIT_ICONS.random()
            }
        }
    }
    
    fun calculateMatchedItems(gameField: List<List<Int>>): List<List<Int>> {
        val matchedItems = mutableListOf<List<Int>>()
        
        // Check horizontal lines
        for (row in gameField.indices) {
            val rowItems = gameField[row]
            val matchedRow = findConsecutiveMatches(rowItems)
            if (matchedRow.isNotEmpty()) {
                val positions = matchedRow.map { col -> row * gameField[0].size + col }
                matchedItems.add(positions)
            }
        }
        
        // Check vertical lines
        for (col in gameField[0].indices) {
            val colItems = gameField.map { it[col] }
            val matchedCol = findConsecutiveMatches(colItems)
            if (matchedCol.isNotEmpty()) {
                val positions = matchedCol.map { row -> row * gameField[0].size + col }
                matchedItems.add(positions)
            }
        }
        
        // Check diagonals (top-left to bottom-right)
        val diagonal1 = (0 until minOf(gameField.size, gameField[0].size)).map { gameField[it][it] }
        val matchedDiag1 = findConsecutiveMatches(diagonal1)
        if (matchedDiag1.isNotEmpty()) {
            val positions = matchedDiag1.map { index -> index * gameField[0].size + index }
            matchedItems.add(positions)
        }
        
        // Check diagonals (top-right to bottom-left)
        val diagonal2 = (0 until minOf(gameField.size, gameField[0].size)).map { gameField[it][gameField[0].size - 1 - it] }
        val matchedDiag2 = findConsecutiveMatches(diagonal2)
        if (matchedDiag2.isNotEmpty()) {
            val positions = matchedDiag2.map { index -> index * gameField[0].size + (gameField[0].size - 1 - index) }
            matchedItems.add(positions)
        }
        
        // Return the best match (longest match)
        return if (matchedItems.isNotEmpty()) {
            listOf(matchedItems.maxByOrNull { it.size } ?: emptyList())
        } else {
            emptyList()
        }
    }
    
    private fun findConsecutiveMatches(items: List<Int>): List<Int> {
        if (items.size < 3) return emptyList()
        
        var maxLength = 0
        var maxStart = 0
        var currentLength = 1
        var currentStart = 0
        
        for (i in 1 until items.size) {
            if (items[i] == items[i - 1]) {
                currentLength++
            } else {
                if (currentLength >= 3 && currentLength > maxLength) {
                    maxLength = currentLength
                    maxStart = currentStart
                }
                currentLength = 1
                currentStart = i
            }
        }
        
        // Check the last sequence
        if (currentLength >= 3 && currentLength > maxLength) {
            maxLength = currentLength
            maxStart = currentStart
        }
        
        return if (maxLength >= 3) {
            (maxStart until maxStart + maxLength).toList()
        } else {
            emptyList()
        }
    }
    
    fun calculateCoins(matchedItems: List<List<Int>>, round: Int): Int {
        if (matchedItems.isEmpty()) return 0
        
        val baseCoins = when (round) {
            1 -> 50
            2 -> 100
            3 -> 150
            4 -> 200
            else -> 50
        }
        
        val matchLength = matchedItems.first().size
        val multiplier = when (matchLength) {
            3 -> 1
            4 -> 2
            5 -> 3
            6 -> 5
            else -> 1
        }
        
        return baseCoins * multiplier
    }
    
    fun getIconSizeForRound(round: Int): Float {
        return when (round) {
            1 -> 72f  // 4x4 - larger icons
            2 -> 60f  // 5x5 - medium icons
            3 -> 48f  // 6x6 - smaller icons
            4 -> 48f  // 6x6 - smaller icons
            else -> 72f
        }
    }
} 