package com.maat.cha.feature.game.dialogs.model

sealed class GameDialogType {
    data class RoundFinished(val roundNumber: Int) : GameDialogType()
    data class TotalWin(val points: Int) : GameDialogType()
    data class CollectRuiles(val columns: List<List<Int>>) : GameDialogType()
}