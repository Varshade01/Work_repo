package com.maat.cha.feature.game.events

sealed class GameEvents {
    object OnStartClick : GameEvents()
    object OnNextRoundClick : GameEvents()
    object OnAgainClick : GameEvents()
    object OnInfoClick : GameEvents()
    data class OnRoundSelect(val roundNumber: Int) : GameEvents()
} 