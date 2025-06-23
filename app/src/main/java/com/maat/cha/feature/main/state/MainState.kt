package com.maat.cha.feature.main.state

data class MainState(
    val isLoading: Boolean = false,
    val hasReadAllInfo: Boolean = false,
    val totalCoins: Int = 0
) 