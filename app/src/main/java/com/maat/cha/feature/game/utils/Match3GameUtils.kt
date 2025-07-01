package com.maat.cha.feature.game.utils

import com.maat.cha.feature.game.model.FruitItem
import com.maat.cha.feature.game.model.FruitType
import com.maat.cha.feature.game.model.GameBoard

object Match3GameUtils {
    
    fun generateMatch3Board(size: Int): GameBoard {
        val fruits = List(size) { row ->
            List(size) { col ->
                val id = row * size + col
                FruitItem(
                    id = id,
                    type = FruitType.getRandom(),
                    row = row,
                    col = col
                )
            }
        }
        
        // Ensure at least one match is possible
        val boardWithMatch = ensureMatchPossible(GameBoard(size, fruits))
        return boardWithMatch
    }
    
    private fun ensureMatchPossible(board: GameBoard): GameBoard {
        val size = board.size
        val fruits = board.fruits
        
        // Check if there's already a possible match
        for (row in 0 until size) {
            for (col in 0 until size) {
                val currentFruit = fruits[row][col]
                
                // Check horizontal swaps
                if (col < size - 1) {
                    val rightFruit = fruits[row][col + 1]
                    if (wouldCreateMatch(fruits, row, col, row, col + 1)) {
                        return board
                    }
                }
                
                // Check vertical swaps
                if (row < size - 1) {
                    val bottomFruit = fruits[row + 1][col]
                    if (wouldCreateMatch(fruits, row, col, row + 1, col)) {
                        return board
                    }
                }
            }
        }
        
        // If no match is possible, create one by modifying a row
        val modifiedFruits = fruits.toMutableList()
        val randomFruitType = FruitType.getRandom()
        val randomRow = (0 until size).random()
        val startCol = (0..(size - 3)).random()
        
        // Create a match of 3 in a row
        for (col in startCol until startCol + 3) {
            val id = randomRow * size + col
            modifiedFruits[randomRow] = modifiedFruits[randomRow].toMutableList().apply {
                this[col] = FruitItem(
                    id = id,
                    type = randomFruitType,
                    row = randomRow,
                    col = col
                )
            }
        }
        
        return GameBoard(size, modifiedFruits)
    }
    
    fun isValidMove(board: GameBoard, fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val size = board.size
        
        // Check bounds
        if (fromRow !in 0 until size || fromCol !in 0 until size ||
            toRow !in 0 until size || toCol !in 0 until size) {
            return false
        }
        
        // Check if adjacent (only allow moves to adjacent cells)
        val rowDiff = kotlin.math.abs(toRow - fromRow)
        val colDiff = kotlin.math.abs(toCol - fromCol)
        
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)
    }
    
    fun wouldCreateMatch(
        fruits: List<List<FruitItem>>,
        fromRow: Int,
        fromCol: Int,
        toRow: Int,
        toCol: Int
    ): Boolean {
        // Create a temporary board with the swap
        val tempFruits = fruits.map { it.toMutableList() }.toMutableList()
        val tempFruit1 = tempFruits[fromRow][fromCol]
        val tempFruit2 = tempFruits[toRow][toCol]
        
        tempFruits[fromRow][fromCol] = tempFruit2.copyWithPosition(fromRow, fromCol)
        tempFruits[toRow][toCol] = tempFruit1.copyWithPosition(toRow, toCol)
        
        // Check for matches
        val matches = findMatches(tempFruits)
        return matches.isNotEmpty()
    }
    
    fun findMatches(fruits: List<List<FruitItem>>): List<List<Pair<Int, Int>>> {
        val matches = mutableListOf<List<Pair<Int, Int>>>()
        val size = fruits.size
        
        // Check horizontal matches
        for (row in 0 until size) {
            var currentType = fruits[row][0].type
            var currentLength = 1
            var startCol = 0
            
            for (col in 1 until size) {
                if (fruits[row][col].type == currentType) {
                    currentLength++
                } else {
                    if (currentLength >= 3) {
                        val match = (startCol until startCol + currentLength).map { Pair(row, it) }
                        matches.add(match)
                    }
                    currentType = fruits[row][col].type
                    currentLength = 1
                    startCol = col
                }
            }
            
            // Check the last sequence
            if (currentLength >= 3) {
                val match = (startCol until startCol + currentLength).map { Pair(row, it) }
                matches.add(match)
            }
        }
        
        // Check vertical matches
        for (col in 0 until size) {
            var currentType = fruits[0][col].type
            var currentLength = 1
            var startRow = 0
            
            for (row in 1 until size) {
                if (fruits[row][col].type == currentType) {
                    currentLength++
                } else {
                    if (currentLength >= 3) {
                        val match = (startRow until startRow + currentLength).map { Pair(it, col) }
                        matches.add(match)
                    }
                    currentType = fruits[row][col].type
                    currentLength = 1
                    startRow = row
                }
            }
            
            // Check the last sequence
            if (currentLength >= 3) {
                val match = (startRow until startRow + currentLength).map { Pair(it, col) }
                matches.add(match)
            }
        }
        
        return matches
    }
    
    fun calculateCoinsForMatches(matches: List<List<Pair<Int, Int>>>, round: Int): Int {
        if (matches.isEmpty()) return 0
        
        val baseCoins = when (round) {
            1 -> 50
            2 -> 100
            3 -> 150
            4 -> 200
            else -> 50
        }
        
        val totalMatchedFruits = matches.sumOf { it.size }
        val multiplier = when (totalMatchedFruits) {
            3 -> 1
            4 -> 2
            5 -> 3
            6 -> 5
            else -> 1
        }
        
        return baseCoins * multiplier
    }
    
    fun getCollectedFruitsFromMatches(
        board: GameBoard,
        matches: List<List<Pair<Int, Int>>>
    ): List<Int> {
        if (matches.isEmpty()) return emptyList()
        
        val collectedFruits = mutableListOf<Int>()
        val matchedPositions = matches.flatten().toSet()
        
        // Group matched items by fruit type
        val fruitGroups = mutableMapOf<FruitType, Int>()
        
        for (row in board.fruits.indices) {
            for (col in board.fruits[row].indices) {
                if (matchedPositions.contains(Pair(row, col))) {
                    val fruit = board.fruits[row][col]
                    fruitGroups[fruit.type] = fruitGroups.getOrDefault(fruit.type, 0) + 1
                }
            }
        }
        
        // Add fruits that have 3 or more items (show 3 of the same fruit)
        fruitGroups.forEach { (fruitType, count) ->
            if (count >= 3) {
                // Add the fruit 3 times to show 3 icons
                repeat(3) {
                    collectedFruits.add(fruitType.drawableRes)
                }
            }
        }
        
        return collectedFruits
    }
} 