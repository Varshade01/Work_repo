package com.maat.cha.feature.game.state

import com.maat.cha.feature.game.dialogs.model.GameDialogType

data class GameState(
    val currentRound: Int = 1,
    val totalRounds: Int = 4,
    val isSpinning: Boolean = false,
    val isGameStarted: Boolean = false,
    val currentDialog: GameDialogType? = null,
    val totalCoins: Int = 0,
    val roundCoins: Int = 0,
    val gameField: List<List<Int>> = emptyList(),
    val matchedItems: List<List<Int>> = emptyList(),
    val isDialogVisible: Boolean = false,
    val collectedFruitsPerRound: Map<Int, List<Int>> = emptyMap() // Round -> List of collected fruit icons
) 