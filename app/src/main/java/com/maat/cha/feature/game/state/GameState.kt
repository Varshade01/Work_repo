package com.maat.cha.feature.game.state

import com.maat.cha.feature.game.dialogs.model.GameDialogType
import com.maat.cha.feature.game.model.GameBoard
import com.maat.cha.feature.game.model.FruitItem

data class GameState(
    val currentRound: Int = 1,
    val totalRounds: Int = 4,
    val isSpinning: Boolean = false,
    val isGameStarted: Boolean = false,
    val currentDialog: GameDialogType? = null,
    val totalCoins: Int = 0,
    val roundCoins: Int = 0,
    val gameField: List<List<Int>> = emptyList(), // Legacy field for backward compatibility
    val matchedItems: List<List<Int>> = emptyList(), // Legacy field for backward compatibility
    val isDialogVisible: Boolean = false,
    val collectedFruitsPerRound: Map<Int, List<Int>> = emptyMap(), // Round -> List of collected fruit icons
    
    // New Match 3 specific fields
    val gameBoard: GameBoard? = null,
    val isMatch3Mode: Boolean = true, // Flag to enable Match 3 mechanics
    val lastMove: Pair<FruitItem, FruitItem>? = null, // Track last move for animation
    val isProcessingMove: Boolean = false
) 