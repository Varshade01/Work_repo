package com.maat.cha.feature.game.model

data class GameBoard(
    val size: Int,
    val fruits: List<List<FruitItem>>,
    val selectedFruit: FruitItem? = null,
    val isAnimating: Boolean = false
) {
    fun getFruitAt(row: Int, col: Int): FruitItem? {
        return if (row in 0 until size && col in 0 until size) {
            fruits[row][col]
        } else null
    }
    
    fun updateFruitAt(row: Int, col: Int, fruit: FruitItem): GameBoard {
        val newFruits = fruits.mapIndexed { r, rowFruits ->
            if (r == row) {
                rowFruits.mapIndexed { c, f ->
                    if (c == col) fruit else f
                }
            } else rowFruits
        }
        return copy(fruits = newFruits)
    }
    
    fun swapFruits(row1: Int, col1: Int, row2: Int, col2: Int): GameBoard {
        val fruit1 = getFruitAt(row1, col1) ?: return this
        val fruit2 = getFruitAt(row2, col2) ?: return this
        
        val newFruits = fruits.mapIndexed { r, rowFruits ->
            rowFruits.mapIndexed { c, fruit ->
                when {
                    r == row1 && c == col1 -> fruit2.copyWithPosition(row1, col1)
                    r == row2 && c == col2 -> fruit1.copyWithPosition(row2, col2)
                    else -> fruit
                }
            }
        }
        return copy(fruits = newFruits)
    }
    
    fun highlightMatchedFruits(matchedPositions: List<Pair<Int, Int>>): GameBoard {
        val newFruits = fruits.mapIndexed { row, rowFruits ->
            rowFruits.mapIndexed { col, fruit ->
                val isHighlighted = matchedPositions.contains(Pair(row, col))
                fruit.copyWithHighlight(isHighlighted)
            }
        }
        return copy(fruits = newFruits)
    }
    
    fun clearHighlights(): GameBoard {
        val newFruits = fruits.map { rowFruits ->
            rowFruits.map { fruit ->
                fruit.copyWithHighlight(false)
            }
        }
        return copy(fruits = newFruits)
    }
    
    fun setSelectedFruit(fruit: FruitItem?): GameBoard = copy(selectedFruit = fruit)
    fun setAnimating(isAnimating: Boolean): GameBoard = copy(isAnimating = isAnimating)
} 