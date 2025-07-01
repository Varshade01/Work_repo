package com.maat.cha.feature.game.events

import com.maat.cha.feature.game.model.FruitItem

sealed class GameEvents {
    object OnStartClick : GameEvents()
    object OnNextRoundClick : GameEvents()
    object OnAgainClick : GameEvents()
    object OnInfoClick : GameEvents()
    data class OnRoundSelect(val roundNumber: Int) : GameEvents()
    
    // Match 3 specific events
    data class OnFruitSelected(val fruit: FruitItem) : GameEvents()
    data class OnFruitMoved(val fromFruit: FruitItem, val toRow: Int, val toCol: Int) : GameEvents()
    object OnFruitDeselected : GameEvents()
    object OnMoveCompleted : GameEvents()
} 